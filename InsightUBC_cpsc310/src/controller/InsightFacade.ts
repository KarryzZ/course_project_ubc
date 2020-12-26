import Log from "../Util";
import {isValidId} from "./EBNFSyntaxHelper";
import {
    IInsightFacade,
    InsightDataset,
    InsightDatasetKind,
    InsightError,
    NotFoundError,
    ResultTooLargeError,
} from "./IInsightFacade";
import {InterpretQuery} from "./Interpreter";
import {ValidateQuery} from "./QueryHelper";
import {RoomHelper} from "./RoomHelper";
import * as fs from "fs";

/**
 * This is the main programmatic entry point for the project.
 * Method documentation is in IInsightFacade
 *
 */
export default class InsightFacade implements IInsightFacade {
    private ds: RoomHelper;
    constructor() {
        Log.trace("InsightFacadeImpl::init()");
        this.ds = new RoomHelper();
    }

    public addDataset(id: string, content: string, kind: InsightDatasetKind, ): Promise<string[]> {
        if (!isValidId(id)) {
            return Promise.reject(new InsightError("invalid ids"));
        }

        if (id in this.ds.datasets) {
            return Promise.reject(new InsightError(id + "' is already added"));
        }
        if (kind === InsightDatasetKind.Courses) {
            return this.ds.loadCourses(id, content);
        } else if (kind === InsightDatasetKind.Rooms) {
            return this.ds.loadRooms(id, content);
        } else {
            return Promise.reject(
                new InsightError("this kind does not exists."),
            );
        }
    }

    public removeDataset(id: string): Promise<string> {
        if (!isValidId(id)) {
            return Promise.reject(new InsightError("invalid ids"));
        }

        if (!(id in this.ds.datasets)) {
            return Promise.reject(new NotFoundError("id not in the dataset."));
        }
        return this.ds.delete(id);
    }

    public performQuery(query: any): Promise<any[]> {
        let cachedata = fs.readdirSync(this.ds.path);
        // load data from cache
        for (let file of cachedata) {
            this.loadCacheData(file);
        }
        // reference: Piazza @1157: no datasets added yet
        if (this.ds.insDatasets.length === 0) {
            return Promise.reject(new InsightError("no datasets added yet"));
        }

        let validIds = Object.keys(this.ds.datasets);
        let vq: ValidateQuery = new ValidateQuery(this.ds.insDatasets);

        let queryids: string[] = [];
        let idtopass: string;
        try {
           vq.validateQuery(query);
           queryids = vq.ids;
        } catch (err) {
            return Promise.reject(new InsightError(err));
        }

        if (queryids.length > 1) {
            return Promise.reject(new InsightError("can not query more than one dataset"));
        } else {
            idtopass = queryids[0];
        }

        // load idtopass data from the cache


        if (!validIds.includes(idtopass)) {
            return Promise.reject(new InsightError("can not query with ids not added"));
        }

        let interpQuery: InterpretQuery;
        let outputs;
        interpQuery = new InterpretQuery(this.ds.datasets[idtopass]);

        try {
            outputs = interpQuery.evaluate(query);
        } catch (error) {
            return Promise.reject(new ResultTooLargeError(error.message));
        }
        return Promise.resolve(outputs);
    }

    public listDatasets(): Promise<InsightDataset[]> {
        return Promise.resolve(this.ds.insDatasets);
    }

    private loadCacheData(file: string) {
        let filename = file.split(".")[0];
        if (this.ds.datasets[filename] === null || this.ds.datasets[filename] === undefined) {
            let datalist = JSON.parse(fs.readFileSync(this.ds.path + "/" + file).toString("utf8"));
            this.ds.datasets[filename] = datalist;
            let insData: InsightDataset;
            if (filename === "courses") {
                insData = {id: "courses", kind: InsightDatasetKind.Courses, numRows: datalist.length};
            } else {
                insData = {id: "rooms", kind: InsightDatasetKind.Rooms, numRows: datalist.length};
            }
            this.ds.insDatasets.push(insData);
        }
    }
}
