import {InsightDataset, InsightDatasetKind, InsightError, NotFoundError} from "./IInsightFacade";
import * as fs from "fs-extra";
import JSZip = require("jszip");
import Log from "../Util";
import { isType } from "./EBNFSyntaxHelper";


export class DatasetHelper {

    public insDatasets: InsightDataset[];
    public datasets: any;
    public path: string;

    constructor() {
        this.insDatasets = [];
        this.datasets = [];
        this.path = "./data";
    }

    public loadCourses(id: string, content: string): Promise<string[]> {
        let newzip = JSZip();
        return new Promise<string[]>((res, rej) => {
            newzip.loadAsync(content, {base64: true}).then((zip) => {
                if (zip.folder(/courses/).length !== 1) {
                    rej(new InsightError("not single courses folder presented."));
                }
                // parse the given json representation of data.
                // files are the array of the parsed course file (in the form of json object).
                this.processZip(zip).then((files) => {
                    let sectionDataList: any = [];
                    for (let file of files) {
                        if ( typeof(file) !== "object" || file === null || file === undefined) {
                            continue;
                        }
                        // check if it is a valid course file.
                        if (Object.keys(file).includes("result")) {
                            sectionDataList = this.loadSectionData(file["result"], sectionDataList);
                        }
                    }
                    if (sectionDataList.length !== 0) {
                        let kind = InsightDatasetKind.Courses;
                        let insData: InsightDataset = {id, kind, numRows: sectionDataList.length};
                        this.insDatasets.push(insData);
                        this.datasets[id] = sectionDataList;
                        this.writeToDisk(id, sectionDataList);
                        res(Object.keys(this.datasets));
                    } else {
                        rej(new InsightError("no valid sections in the given datasets"));
                    }
                });
            }).catch((err) => {
                rej(new InsightError(" no valid files1"));
            });
        });
    }

    private processZip (zip: JSZip): Promise<string[]> {
        let promises: Array<Promise<any>> = [];
        zip.folder("courses/").forEach((relativePath, file) => {
                // should skip the hidden file ds_store for mac user.
                if (!file.name.match(/\.DS\_Store/)) {
                    let prom: Promise<any> = file.async("string").then((str: string) => {
                        return JSON.parse(str);
                    }).catch((err) => {
                        Log.trace("this file is invalid.");
                    });
                    promises.push(prom);
                }
            });

        // if (promises.length === 0) {
        //         return Promise.reject(new InsightError ("no valid files123"));
        //     }

        return Promise.all(promises);
    }


    // get the required data from the parsed json file.
    public loadSectionData(parsedResults: any[], data: any[]): any[] {
        if (parsedResults === null || parsedResults === undefined) {
            return data;
        }

        let requiredKeys = ["Subject", "Course", "Avg", "Professor", "Title",
        "Pass", "Fail", "Audit", "id", "Year", "Section"];
        for (let parsedRes of parsedResults) {
            let sectiondata: any = {};
            let contentKeys: string[] = Object.keys(parsedRes);

            if (requiredKeys.every((str) => {
                return contentKeys.includes(str);
            })) {
                sectiondata["dept"] = parsedRes["Subject"];
                sectiondata["id"] = parsedRes["Course"];
                sectiondata["avg"] = parsedRes["Avg"];
                sectiondata["instructor"] = parsedRes["Professor"];
                sectiondata["title"] = parsedRes["Title"];
                sectiondata["pass"] = parsedRes["Pass"];
                sectiondata["fail"] = parsedRes["Fail"];
                sectiondata["audit"] = parsedRes["Audit"];
                sectiondata["uuid"] = parsedRes["id"].toString(); // note that this required to be string.
                sectiondata["year"] = Number((parsedRes["Year"]));

                // set the year for overall to 1900 a`s required
                if (parsedRes["Section"] === "overall") {
                    sectiondata["year"] = 1900;
                }

                // add type checking before push to sectiondata.
                if (isType(sectiondata["dept"], "string") && isType(sectiondata["id"], "string") &&
                isType(sectiondata["avg"], "number") && isType(sectiondata["instructor"], "string") &&
                isType(sectiondata["title"], "string") && isType(sectiondata["pass"], "number") &&
                isType(sectiondata["fail"], "number") && isType(sectiondata["audit"], "number") &&
                isType(sectiondata["uuid"], "string") && isType(sectiondata["year"], "number")) {
                    data.push(sectiondata);
                }
            }
        }
        return data;
    }

    public writeToDisk(id: string, data: any[]): any {
        // solve the problem that if cache dir exists
        if (!fs.existsSync(this.path)) {
            fs.mkdirSync(this.path);
        }
        fs.writeFile(this.path + "/" + id + ".json", JSON.stringify(data));
    }


    // delete the dataset given the valid id.
    public delete(id: string): Promise<string> {
        return new Promise ((res, rej) => {
            delete this.datasets[id];
            let toDelete = this.insDatasets.find((insDataset) => {
                return insDataset.id === id;
            });
            let index = this.insDatasets.indexOf(toDelete);
            if (index > -1) {
                this.insDatasets.splice(index, 1);
            }
            fs.remove(this.path + "/" + id + ".json").then(() => {
                return res(id);
            }).catch((err) => {
                return rej(new NotFoundError("dataset removed failed"));
            });
        });
    }
}
