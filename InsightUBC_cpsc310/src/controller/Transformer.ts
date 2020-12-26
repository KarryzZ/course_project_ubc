import * as assert from "assert";
import {ResultTooLargeError} from "./IInsightFacade";
import {outputFile} from "fs-extra";
import Decimal from "decimal.js";
import { CSField, RSField } from "./EBNFSyntaxHelper";

// results : Array of (dataObject)
// GROUP: [term1,term2,...]

// grouping: {
//     "term1.value,term2,vale_..." : [
//         section,
//         section
//     ]
// }
interface IGroupedData {
    [gkey: string]: [IPairedData];
}

interface IPairedData {
    [applyname: string]: number;
}

export function makeGroups (results: any[], group: any) {
    let groupings: any = {};
    results.forEach((section: any) => {
        let groupingKey: string = "";
        // sectionInfo is an object, the key is a field (e.g. "dept"), the value is the field value of a single section

        // make a group key;
        group.forEach((singleKey: string) => {
            groupingKey += "`";
            let field = singleKey.split("_")[1];
            groupingKey += field + "%" + section[field];
        });
        // get rid of the comma at the front
        groupingKey = groupingKey.slice(1);

        // grouping is an object, the key is a grouping key (e.g. "dept,pass,avg"), the value is an array of sections
        // with the corresponding grouping key
        if (groupings[groupingKey]) {
            groupings[groupingKey].push(section);
        } else {
            groupings[groupingKey] = [];
            groupings[groupingKey].push(section);
        }
    });
    return groupings;
}

export function checkTooLarge (anyarray: any[]) {
    if (anyarray.length > 5000) {
        throw new ResultTooLargeError("The result is too large");
    }
}

function interpretCOUNT (group: any[], field: string) {
    let noduplication = new Set();
    group.forEach((element) => {
        noduplication.add(element[field]);
    });
    return noduplication.size;
}

// REFERENCE for Spread syntax (...):
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Spread_syntax
function interpretMAX (group: any[], field: string) {
    let numberarr: number[] = group.map((item: any) => item[field]);
    return Math.max(...numberarr);
}

function interpretMIN (group: any[], field: string) {
    let numberarr: number[] = group.map((item: any) => item[field]);
    return Math.min(...numberarr);
}

// REFERENCE for Array.reduce():
// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/Reduce
function interpretSUM (group: any[], field: string) {
    let total = 0;
    group.forEach((data) => {
        total += data[field];
    });
    return Number(total.toFixed(2));
}

function interpretAVG (group: any[], field: string) {
    // use decimal as required
    let total: Decimal = new Decimal(0);
    group.forEach((data) => {
        total = new Decimal(total.add(new Decimal(data[field])));
    });
    let avg = total.toNumber() / group.length;
    let res = Number(avg.toFixed(2));
    return res;
}

function interpretSingleAPPLY (grouping: any, [rule, field]: any) {
    let temp: any = {};
    Object.keys(grouping).forEach((mapkey: string) => {
        if (rule === "COUNT") {
            temp[mapkey] = interpretCOUNT(grouping[mapkey], field);
        } else if (rule === "MAX") {
            temp[mapkey] = interpretMAX(grouping[mapkey], field);
        } else if (rule === "MIN") {
            temp[mapkey] = interpretMIN(grouping[mapkey], field);
        } else if (rule === "SUM") {
            temp[mapkey] = interpretSUM(grouping[mapkey], field);
        } else {
            temp[mapkey] = interpretAVG(grouping[mapkey], field);
        }
    });
    return temp;
}

export function interpretAPPLY (grouping: any, apply: any[]) {


    if (!apply.length) {
        let outputs: any[] = [];
        Object.keys(grouping).forEach((gkey) => {
            let output: any = {};
            parseGroupKeyContent(gkey, output);
            outputs.push(output);
        });
        return outputs;
    }
    let rulesInfo: any[] = [];
    apply.forEach((onerule: any) => {
        let name: string = Object.keys(onerule)[0];
        let token: string = Object.keys(onerule[name])[0];
        let field: string = onerule[name][token].split("_")[1];
        rulesInfo.push([name, token, field]);
    });

    let applys: any[] = [];

    rulesInfo.forEach(([name, token, field]: any) => {
        applys.push([name, interpretSingleAPPLY(grouping, [token, field])]);
    });


    let groupdata: IGroupedData = {} ;
    applys.forEach((ruleWithResult: any) => {
        let singleApplyName: string = ruleWithResult[0];
        // groupsWithResult is an object, the keys are the groupingKey, the values are the result for a single group
        let groupsWithResult: any = ruleWithResult[1];
        Object.keys(groupsWithResult).forEach((groupingKey: string) => {
            let pairedData: IPairedData = {};
            // groupingKey is the primary key we identify a unique group of sections
            // groupsWithResult[groupingKey] is the result of a single group applying the given applyRule
            pairedData[singleApplyName] = groupsWithResult[groupingKey];
            if (!groupdata[groupingKey]) {
                groupdata[groupingKey] = [pairedData];
            } else {
                groupdata[groupingKey].push(pairedData);
            }
        });
    });

    return formOutputData(groupdata);
// wait for operating Columns
}

export function formOutputData(data: IGroupedData) {
    let outputs: any[] = [];
    Object.keys(data).forEach((gkey) => {
        let output: any = {};
        parseGroupKeyContent(gkey, output);
        data[gkey].forEach((pairedData) => {
        // only one pair of data (e.g. { Avg : 99.18})
        let key = Object.keys(pairedData)[0];
        output[key] = pairedData[key];
        if (!outputs.includes(output)) {
            outputs.push(output);
        }
        });
    });
    return outputs;
}


export function parseGroupKeyContent(str: string, result: any) {
    if (str.includes("`")) {
        for (let key of str.split("`")) {
            let groupItem = key.split("%");
            parseSingleContent(groupItem, result);
        }
    } else {
        let groupItem = str.split("%");
        parseSingleContent(groupItem, result);
    }
}

function parseSingleContent(item: string[], result: any) {
    if (CSField.includes(item[0]) || RSField.includes(item[0])) {
        result[item[0]] = item[1];
    } else {
        result[item[0]] = Number(item[1]);
    }
}


