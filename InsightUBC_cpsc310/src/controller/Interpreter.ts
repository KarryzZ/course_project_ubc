import {Logic, MComparator, Negation, SComparator} from "./EBNFSyntaxHelper";
import {IInsightFacade, ResultTooLargeError} from "./IInsightFacade";
import {checkTooLarge, interpretAPPLY, makeGroups} from "./Transformer";
import * as assert from "assert";
import Log from "../Util";


export const MAX_RESULT = 5000;

// Source: https://stackoverflow.com/questions/19837916/creating-object-with-dynamic-keys
interface IOutputQuery {
    [mskey: string]: string | number;
}

export class InterpretQuery {
    public data: any;

    constructor(data: any) {
        this.data = data;
    }

    public evaluate(query: any) {
        const OPTIONS = query["OPTIONS"];
        // const columns = query["OPTIONS"]["COLUMNS"];
        let options = Object.entries(OPTIONS);
        let [key, val] = options[0]; // colval is the fields we want
        let results: any[] = [];
        for (let section of this.data) {
            if (this.checkFilter(query["WHERE"], section)) {
                results.push(section);
            }
        }
        if (results.length === 0) {
            return [];
        }
        if (!query["TRANSFORMATIONS"]) {
            checkTooLarge(results);
            // if result > 1 and order exists we do sort
        } else {
            const group: any = query["TRANSFORMATIONS"]["GROUP"];
            // GroupedResults data
            let groupings: any = makeGroups(results, group);
            // checkTooLarge(Object.keys(groupings));
            const apply = query["TRANSFORMATIONS"]["APPLY"];
            // results in TRAMSFORMATION:
            // the resulted data to output with transformation.
            results = interpretAPPLY(groupings, apply);
            checkTooLarge(results);
        }

        // sort
        if (results.length > 1) {
            const order = query["OPTIONS"]["ORDER"];
            this.mysort(order, results);
        }

        let outputs: any[] = [];  // for each section in results, get colval
        results.forEach((section: any) => {
                let secres: IOutputQuery = {};
                let colval = val as string[];
                colval.forEach((colkey) => {
                    if (colkey.includes("_")) {
                        let mskey = colkey.split("_")[1];
                        secres[colkey] = section[mskey];
                    } else {
                        secres[colkey] = section[colkey];
                    }
                });
                outputs.push(secres);
            });
        return outputs;
    }


    private checkFilter(query: any, section: any): boolean {
        // if query where:{}, we include everything
        if (Object.keys(query).length === 0) {
            return true;
        }

        let key = Object.keys(query)[0]; // get the key of our query
        if (Logic.includes(key)) {
            return this.checkLogic(query[key], key, section);
        } else if (Negation.includes(key)) {
            return this.checkNeg(query[key], section);
        } else if (SComparator.includes(key)) {
            return this.checkScomp(query[key], section);
        } else if (MComparator.includes(key)) {
            return this.checkMcomp(query[key], key, section);
        } else {
            return false;
        }
    }

    private checkScomp(query: any, section: any): boolean {
        let key = Object.keys(query)[0];
        let value = query[key] as string;
        let sfield = key.split("_")[1];
        let SecString: string = section[sfield] as string; //
        if (!value.includes("*")) {
            return SecString === value;
        } else {
            if (value.startsWith("*") && value.endsWith("*")) {
                return SecString.includes(value.slice(1, value.length - 1));
            } else if (value.startsWith("*")) {
                return SecString.endsWith(value.slice(1));
            } else if (value.endsWith("*")) {
                return SecString.startsWith(value.slice(0, value.length - 1));
            }
        }
    }

    private checkNeg(query: any, section: any): boolean {
        return !this.checkFilter(query, section);
    }

    private checkLogic(query: any[], key: string, section: any): boolean {
        if (key === "AND") {
            for (let filter of query) {
                if (!this.checkFilter(filter, section)) {
                    return false;
                }
            }
            return true;
        } else {
            for (let filter of query) {
                if (this.checkFilter(filter, section)) {
                    return true;
                }
            }
            return false;
        }
    }

    private checkMcomp(query: any, key: string, section: any): boolean {
        let mkey = Object.keys(query)[0];
        let value = query[mkey];
        let mfield = mkey.split("_")[1]; // get the mkey's field
        if (key === "GT") {
            return section[mfield] > value;
        }
        if (key === "EQ") {
            return section[mfield] === value;
        }
        if (key === "LT") {
            return section[mfield] < value;
        }
    }

    private mysort(order: any, results: any[]): any[] {
        if (order === null || order === undefined) {
            return results;
        }
        if (typeof order === "string") {
            results.sort(this.comparator([order], "UP"));
        } else {
            let dir: string = order["dir"];
            let keys: string[] = order["keys"];
            results.sort(this.comparator(keys, dir));
        }
        return results;
    }

    private comparator(keys: string[], dir: string) {
        return (a: any, b: any) => this.comparatorHelper(keys, dir, a, b);
    }

    private comparatorHelper(keys: string[], dir: string, a: any, b: any): number {
        // retrive the first key
        const key = keys[0];
        let left = this.getKeyValue(a, key);
        let right = this.getKeyValue(b, key);

        if (left < right) {
            return dir === "UP" ? -1 : 1;
        } else if (left > right) {
            return dir === "UP" ? 1 : -1;
        } else if (keys.slice(1).length) { // left == right && has other keys ==> keep sorting
            return this.comparatorHelper(keys.slice(1), dir, a, b);
        }
        // return if no other keys:
        return 0;
    }

    private getKeyValue(a: any, key: string) {
        // check if it is a applykey
        if (key.includes("_")) {
            let field = key.split("_")[1];
            let res = a[field];
            return res;
        } else { // value from applykey are all numbers
            return a[key];
        }
    }
}
