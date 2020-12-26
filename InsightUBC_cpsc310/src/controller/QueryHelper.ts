import * as assert from "assert";
import { isDoStatement } from "typescript";
import Log from "../Util";
import {
    ApplyToken, assertType, CMField, CSField, DirToken, isValidId, Logic,
    MComparator, Negation, RMField, RSField, SComparator
} from "./EBNFSyntaxHelper";
import { InsightDataset, InsightDatasetKind } from "./IInsightFacade";
/**
 * Determine if a query match the given EBNF format.
 */

export class ValidateQuery {
    private groupKeys: string[];
    public ids: string[];
    private insDatas: InsightDataset[];

    constructor(data: InsightDataset[]) {
        this.groupKeys = [];
        this.ids = [];
        this.insDatas = data;
    }

    public validateQuery(query: any) {
        assertType(query, "object");
        let queryEn = Object.entries(query);
        assert(queryEn.length !== 0, "Invalid: Empty query");
        assert(queryEn[0][0] === "WHERE", "Invalid: MISSING WHERE");
        assert(queryEn.length !== 1, "Invalid: MISSING OPTIONS");
        assert(queryEn[1][0] === "OPTIONS", "Invalid: MISSING OPTIONS");
        this.validateBody(queryEn[0][1]);

        // there can be no TRANSFORMATIONS
        if (queryEn.length > 2) {
            assert(queryEn[2][0] === "TRANSFORMATIONS", "Invalid: MISSING TRANSFORMATIONS");
            assert(queryEn.length === 3, "Invalid: invalid number of keys in the query");
            this.validateTransformation(queryEn[2][1]);
        }

        this.validateOption(queryEn[1][1]);
    }

    private validateBody(where: any) {
        assertType(where, "object");
        assert(!Array.isArray(where), "where is not an array");
        let keys = Object.keys(where);
        assert(keys.length === 1 || keys.length === 0, "Invalid: WHERE should only have 0 or 1 key");
        if (keys.length > 0) {
            this.validateFilter(where);
        }
    }

    private validateTransformation(transformation: any) {
        assertType(transformation, "object");
        const TRANSFORMATIONS = Object.entries(transformation);


        assert((TRANSFORMATIONS.length === 2), "Invalid: TRANSFORMATIONS must have two parts");

        let [group, groupVal] = TRANSFORMATIONS[0];
        assert(group === "GROUP", "Invalid: Missing GROUP");
        assert(Array.isArray(groupVal), "Invalid GROUP: not an array");
        let ArraygroupVal = groupVal as string[];
        ArraygroupVal.forEach((key: string) => {
            this.groupKeys.push(key);
        });
        assert(this.groupKeys.length !== 0, "Invalid: GROUP must be a non-empty array");
        for (let g of this.groupKeys) {
            assert(this.isValidKey(g), "Invalid: GROUP choices not fields in COURSE");
        }
        // if (TRANSFORMATIONS.length === 2) {
        let [app, appVal] = TRANSFORMATIONS[1];
        assert(app === "APPLY", "Invalid: APPLY Key");
        assert(Array.isArray(appVal), "Invalid APPLY: not an array");
        let appRules: any[] = appVal as any[];
        // assert(appRules.length === 1 || appRules.length === 0, "Invalid: APPLY should only have 0 or 1 key");
        if (appRules.length !== 0) {
            appRules.forEach((appRule) => {
                assertType(appRule, "object");
                assert(Object.keys(appRule).length === 1, "Invalid: two or more applykey in one applyRule");
                let [appKey, appContent] = Object.entries(appRule)[0];
                assert(appContent, "object");
                assert(Object.keys(appContent).length === 1, "Invalid: Apply body should only have 1 key");
                assert(this.isValidApplyKey(appKey), "Invalid: not a valid apply key");
                assert(!this.groupKeys.includes(appKey), "Invalid: ApplyRule should be unique");
                this.groupKeys.push(appKey);
                assert(Object.keys(appContent).length === 1, "Invalid: more than one apply tokens");
                let [apptoken, appContentVal] = Object.entries(appContent)[0];

                assert(ApplyToken.includes(apptoken), "Invalid: invalid APPLY Token");
                if (apptoken !== "COUNT") {
                    assert(this.isValidMKey(appContentVal), "Invalid: should be a numeric Key");
                } else {
                    assert(this.isValidKey(appContentVal), "Invalid: not a valid key after count");
                }
            });
        }
        // }
    }

    private validateOption(option: any) {
        assertType(option, "object");
        const OPTIONS = Object.entries(option);

        assert(OPTIONS.length !== 0, "Invalid: OPTIONS is empty", );

        assert((OPTIONS.length === 1 || OPTIONS.length === 2),
            "Invalid: OPTIONS can only have two parts"
        );

        // test for COLUMNS
        let [col, colVal] = OPTIONS[0];
        assert(col === "COLUMNS", "Invalid: Missing COLUMN");
        assert(Array.isArray(colVal), "Invalid Columns: not an array");
        let columnKeys = colVal as string[];
        assert(columnKeys.length !== 0, "Invalid COLUMNS must be a non-empty array");
        for (let c of columnKeys) {
            assert(this.isValidAnyKey(c), "Invalid: invalid column keys");
            if (this.groupKeys.length !== 0) {
                assert(this.groupKeys.includes(c), "Invalid: Columns keys must be either group or apply keys");
            } else {
                // groupKeys is empty at this spot => Old query, and this should not have any applykeys.
                assert(this.isValidKey(c), "Invalid: Column not an apply key");
            }
        }

        if (OPTIONS.length === 2) {
            let [ord, ordVal] = OPTIONS[1];
            assert(ord === "ORDER", "Invalid: ORDER Key");
            if (typeof ordVal === "string") {
                let orderKey = ordVal as string;
                assert(this.isValidAnyKey(orderKey), "Invalid : keys in order are not any key");
                assert(columnKeys.includes(orderKey), "Invalid ORDER Key: not in the Columns");
            } else {
                assert(ordVal, "object");
                assert(Object.keys(ordVal).length === 2, "Invalid ORDER Key: have two parts");
                let [dir, dirval] = Object.entries(ordVal)[0];
                assert(dir === "dir", "Invalid: second entry should be DIRECTION");
                assert(DirToken.includes(dirval), "Invalid first: not in the Columns");

                let [key, keyval] = Object.entries(ordVal)[1];
                assert(key === "keys", "Invalid: second entry should be KEYS");
                assert(Array.isArray(keyval), "Invalid: value in KEYS should be array");
                assert(keyval.length !== 0, "Invalid: KEYS must be non-empty array");
                for (let item of keyval) {
                    let sortkey = item as string;
                    assert(columnKeys.includes(sortkey), "Invalid sort Keys: not in the Columns");
                }
            }
        }
    }

    private validateFilter(filter: any) {
        assertType(filter, "object");
        let [key, value] = Object.entries(filter)[0];
        assert(this.isValidFilter(key), "Invalid FILTER: Key's not a filter key");

        if (Logic.includes(key)) {
            this.validateLogic(value);
        } else if (MComparator.includes(key)) {
            this.validateMComparison(value);
        } else if (SComparator.includes(key)) {
            this.validateSComparison(value);
        } else if (Negation.includes(key)) {
            // recursive call to solve multiple filter keys;
            this.validateNegation(value);
        } else {
            this.validateFilter(value);
        }
    }

    private isValidFilter(key: string) {
        return (
            Logic.includes(key) ||
            MComparator.includes(key) ||
            SComparator.includes(key) ||
            Negation.includes(key));
    }

    private validateLogic(logic: any) {
        // with one empty object in the array after Logic operator.
        assert(Array.isArray(logic), "Invalid value of LOGIC KEY: Not an array");
        assert(logic.length !== 0, "Invalid LOGIC: Empty array");
        assert(logic.every((res: any) => {
            return typeof (res) === "object" && res && Object.keys(res).length === 1;
        }), "Invalid LOGIC: Some items are not objects");
        logic.forEach((res: any) => {
            this.validateFilter(res);
        });
    }

    private validateMComparison(mcomp: any) {
        assertType(mcomp, "object");
        assert(Object.keys(mcomp).length === 1, "Invalid MCOMPARTOR: should have only 1 key");
        let [mkey, value] = Object.entries(mcomp)[0];
        Log.trace(typeof mkey);
        assert(this.isValidMKey(mkey), "Invalid MCOMPARISON: Not an mkey");
        assertType(value, "number");
    }

    private validateSComparison(scomp: any) {
        assertType(scomp, "object");
        assert(Object.keys(scomp).length === 1, "Invalid MCOMPARTOR: should have only 1 key");

        let [skey, value] = Object.entries(scomp)[0];
        assert(this.isValidSKey(skey), "Invalid SCOMPARISON: Not an skey");
        assertType(value, "string");
        let str = value as string;
        assert(this.isValidInpStr(str), "Invalid SCOMPARISON: Invalid input string");
    }

    private validateNegation(not: any) {
        assertType(not, "object");
        assert(Object.keys(not).length === 1, "Invalid Not: should have only 1 key");
        this.validateFilter(not);
    }

    private isValidAnyKey(key: string) {
        return this.isValidKey(key) || this.isValidApplyKey(key);
    }

    private isValidApplyKey(key: string) {
        if (key === null || key === undefined) {
            return false;
        }
        return /^[^_]+$/.test(key); // apply key can be whitespace only
    }

    private isValidKey(key: string) {
        return this.isValidMKey(key) || this.isValidSKey(key);
    }

    private isValidMKey(key: string) {
        let split = key.split("_");
        let id = split[0];
        let mField = split[1];
        if (this.isAddedId(id)) {
            if (!this.ids.includes(id)) {
                this.ids.push(id);
            }
        } else {
            return false;
        }
        return this.findKind(id) === InsightDatasetKind.Courses ?
        CMField.includes(mField) : RMField.includes(mField);
    }

    private isValidSKey(key: string) {
        let split = key.split("_");
        let id = split[0];
        let sField = split[1];
        if (this.isAddedId(id)) {
            if (!this.ids.includes(id)) {
                this.ids.push(id);
            }
        } else {
            return false;
        }
        return this.findKind(id) === InsightDatasetKind.Courses ?
        CSField.includes(sField) : RSField.includes(sField);
    }

    private isValidInpStr(key: string) {
        if (key === null || key === undefined) {
            return false;
        }
        if (key.length === 0) {
            return true;
        }
        if (key.charAt(0) === "*") {
            key = key.slice(1);
        }
        if (key.length === 0) {
            return true;
        } else if (key.charAt(key.length - 1) === "*") {
            key = key.slice(0, key.length - 1);
        }

        if (key.includes("*")) {
            return false;
        }
        return true;
    }

    private isAddedId(id: string) {
        return this.insDatas.some((insData) => {
            return insData.id === id;
        });
    }

    // invariant: id is a valid
    private findKind(id: string) {
        return this.insDatas.find((insData) => {
            return insData.id === id;
        }).kind;
    }
}
