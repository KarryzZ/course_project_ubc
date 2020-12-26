import { assert } from "chai";

export const CMField: string [] = ["avg", "pass", "fail", "audit", "year"];

export const RMField: string[] = ["lat", "lon", "seats"];

export const CSField: string[] = [
    "dept", "id", "instructor", "title", "uuid"
];

export const RSField: string[] = ["fullname", "shortname",
"number", "name", "address", "type", "furniture", "href"];

export const Logic: string[] = ["AND", "OR"];

export const MComparator: string[]  = ["GT", "EQ", "LT"];

export const SComparator: string[]  = ["IS"];

export const Negation: string[] = ["NOT"];

export const ApplyToken: string [] = ["MAX", "MIN", "AVG", "COUNT", "SUM"];

export const DirToken: string[] = ["UP", "DOWN"];

export function isValidId(id: string): boolean {
    if (id === null || id === undefined) {
        return false;
    }
    // test id should not contain underscore or be whitespace only.
    return /^[^_]+$/.test(id) && /^(?!^ +$)^.+$/.test(id);
}

export const assertType = (val: any, type: string) => {
    return assert(typeof (val) === type, val.toString() + "Not an object");
};

export const isType = (val: any, type: string) => {
    return typeof (val) === type;
};


