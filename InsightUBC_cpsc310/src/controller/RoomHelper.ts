import assert = require("assert");
import { resolve } from "dns";
import JSZip = require("jszip");
import parse5 = require("parse5");
import { parse, relative } from "path";
import Log from "../Util";
import { DatasetHelper } from "./DatasetHelper";
import { getGeoLocation } from "./GeoHandler";
import { InsightDataset, InsightDatasetKind, InsightError} from "./IInsightFacade";
import { findTBody, getAttrVal,
    getChildNodeByClassName,
    getTrimedText, requiredBField,
    requiredRField, requiredRules } from "./myDocuemnt";


// the data structure for building:
// we use href as a primary key because
// each valid building has a unique href.
// and it is easier to load the rooms in it.
// {
//     "href" : {
//     fullname: string;
//     shortname: string;
//     address: string;
//     lat: number;
//     lon: number;
//     }
// }

// all the the data need to load
interface Room {
    fullname: string;
    shortname: string;
    number: string;
    name: string;
    address: string;
    lat: number;
    lon: number;
    seats: number;
    type: string;
    furniture: string;
    href: string;
}

export class RoomHelper extends DatasetHelper {
    private buildings: any;

    constructor() {
        super();
        this.buildings = {};
    }

    public loadRooms(id: string, content: string): Promise<string[]> {
        let newzip = JSZip();
        return new Promise<any[]>((res, rej) => {
             newzip.loadAsync(content, {base64: true}).then((zip) => {
                // if (Object.keys(zip.folder(/rooms/)).length !== 1 || zip.file(/index.htm/).length !== 1) {
                //     rej(new InsightError("not single rooms folder and single index.htm presented."));
                // }
                if (Object.keys(zip.folder(/^rooms\/$/)).length !== 1) {
                    rej(new InsightError("no rooms folders"));
                }
                if (zip.folder("rooms/").file(/^index.htm$/).length !== 1) {
                    rej(new InsightError("more than one index.htm files"));
                }
                let results: Room[] = [];
                // load the index.htm right now
                zip.file("rooms/index.htm").async("text").then ((rawdata: any) => {
                    this.processrawData(zip, rawdata, results, rej, id, res);
                });
            }).catch((err) => {
                rej(new InsightError(err.message));
            });
        });
    }

    private processrawData (zip: JSZip, rawdata: any, results: Room[], rej: (reason?: any) => void,
                            id: string, res: (value?: (any[] | PromiseLike<any[]>)) => void) {
        this.parseBuildings(rawdata).then((datas) => {
            // Log.trace(datas);
            this.buildings = datas;
            if (Object.keys(this.buildings).length === 0) {
                rej(new InsightError("no valid Buildings in the given datasets"));
            }
            let promises: any[] = [];
            let orderedData: string[] = [];

            this.loadpromises(zip, promises, orderedData);
            this.processPromise(promises, orderedData, results, rej, id, res);

        }).catch((err) => {
            rej(new InsightError(err.message));
        });
    }

    private loadpromises (zip: JSZip, promises: any[], orderedData: string[]) {
        zip.folder("rooms").forEach((relativePath, file) => {
            // only process the buildings in the index.htm
            if (this.buildings[relativePath]) {
                orderedData.push(relativePath);
                promises.push(file.async("text"));
            }
        });
    }

    private processPromise(promises: any[], orderedData: string[], results: Room[], rej: (reason?: any) => void,
                           id: string, res: (value?: (any[] | PromiseLike<any[]>)) => void) {
        Promise.all(promises).then((rowRoomdatas) => {
            for (let i = 0; i < rowRoomdatas.length; i++) {
                this.parseRooms(this.buildings[orderedData[i]], rowRoomdatas[i], results);
            }
            if (results.length === 0) {
                rej(new InsightError("No valid rooms in the data"));
            }
            let kind = InsightDatasetKind.Rooms;
            let insData: InsightDataset = {id, kind, numRows: results.length};
            this.insDatasets.push(insData);
            this.datasets[id] = results;
            this.writeToDisk(id, results);
            res(Object.keys(this.datasets));
        });
    }


    private parseBuildings(content: any): Promise<any> {
        return new Promise<any>((res, rej) => {
            try {
                let tree = parse5.parse(content);
                let tbody = findTBody(tree, requiredRules, requiredBField);
                if (!tbody) {
                    rej("No valid buildings data: not a valid index.htm");
                }
                let trs = tbody.childNodes.filter((result: any) => {
                    return result.nodeName === "tr";
                });
                let buildings: any = {};
                let promises: Array<Promise<any>> = [];
                for (let tr of trs) {
                    promises.push(this.parseSingleBuilding(tr));
                }

                Promise.all(promises).then((bdatas: any) => {
                    for (let bdata of bdatas) {
                        if (bdata !== null) {
                            buildings[bdata.bhref] = bdata;
                        }
                    }
                    res(buildings);
                });
            } catch (err) {
                rej(err.message);
            }
        });
    }


    private parseSingleBuilding(tr: any): Promise<any> {
        let bcode = getChildNodeByClassName(tr, requiredBField[0]);

        let title = getChildNodeByClassName(tr, requiredBField[1])
            .childNodes.find((node: any) => node.tagName === "a");

        let baddress = getChildNodeByClassName(tr, requiredBField[2]);

        // invalid if some of the value is missing
        if (!bcode || !title || !baddress) {
            return Promise.reject({ isValid: false });
        }

        let code = getTrimedText(bcode);
        let name = getTrimedText(title);
        let address = getTrimedText(baddress);
        let path: string = getAttrVal(title, "href").split("./")[1];

        return getGeoLocation(address).then((res) => {
            // if it is a valid response:
            if (Object.keys(res).length === 2) {
                return {
                    fullname: name,
                    shortname: code,
                    address: address,
                    bhref : path,
                    lat : res.lat,
                    lon : res.lon
                };
            }
            return null;
        });
    }

    // bdata is the valid building data;
    // row Room data is the unparsed building info;
    // results is the accumulator for final answer.
    private parseRooms(bdata: any, rowRoomsData: any, results: any[]) {
            let parsedRoomData = parse5.parse(rowRoomsData);
            let tbody = findTBody(parsedRoomData, requiredRules, requiredRField);
            // check if it is valid room with the required content fields.
            if (tbody) {
                let trs = tbody.childNodes.filter((result: any) => {
                    return result.nodeName === "tr";
                });
                for (let tr of trs) {
                    let room = this.parseSingleRoom(bdata, tr, bdata.shortname);
                    results.push(room);
                }
            }
    }

    private parseSingleRoom(bdata: any, roomshtm: any, code: string): Room {
        let rnumber = getChildNodeByClassName(roomshtm, requiredRField[0])
            .childNodes.find((node: any) => node.tagName === "a");

        let rcapacity = getChildNodeByClassName(roomshtm, requiredRField[1]);

        let rfurniture = getChildNodeByClassName(roomshtm, requiredRField[2]);

        let rtype = getChildNodeByClassName(roomshtm, requiredRField[3]);

        let rhref = getChildNodeByClassName(roomshtm, requiredRField[4])
            .childNodes.find((node: any) => node.tagName === "a");

        // CAPACITY COULD BE MISSING
        if (!rnumber || !rfurniture || !rtype || !rhref) {
            return null;
        }

        return {
            fullname: bdata.fullname,
            shortname: bdata.shortname,
            number: getTrimedText(rnumber),
            name: code + "_" + getTrimedText(rnumber),
            address: bdata.address,
            lat: bdata.lat,
            lon: bdata.lon,
            seats: rcapacity ? Number(getTrimedText(rcapacity)) : 0,
            type: getTrimedText(rtype),
            furniture: getTrimedText(rfurniture),
            href: getAttrVal(rhref, "href")
        };
    }
}
