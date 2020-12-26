import * as chai from "chai";
import { expect } from "chai";
import * as fs from "fs-extra";
import * as chaiAsPromised from "chai-as-promised";
import {
    InsightDataset,
    InsightDatasetKind,
    InsightError,
    NotFoundError,
} from "../src/controller/IInsightFacade";
import InsightFacade from "../src/controller/InsightFacade";
import Log from "../src/Util";
import TestUtil from "./TestUtil";

// This should match the schema given to TestUtil.validate(..) in TestUtil.readTestQueries(..)
// except 'filename' which is injected when the file is read.
export interface ITestQuery {
    title: string;
    query: any; // make any to allow testing structurally invalid queries
    isQueryValid: boolean;
    result: any;
    filename: string; // This is injected when reading the file
}

describe("InsightFacade Add/Remove/List Dataset", function () {
    // Reference any datasets you've added to test/data here and they will
    // automatically be loaded in the 'before' hook.
    const datasetsToLoad: { [id: string]: string } = {
        courses: "./test/data/courses.zip",
        coursesSmall: "./test/data/courses-small.zip",
        CoursesSmall: "./test/data/courses-small.zip",
        emptyCourses: "./test/data/coursesFolderEmpty.zip",
        allInvalidFiles: "./test/data/invalidCourses.zip",
        coursesFileUnderScore: "./test/data/coursesFolderUnderscore.zip",
        notZip: "./test/data/not-zip.txt",
        OneInvalidCourses: "./test/data/oneInvalid.zip",
        whiteSpaceOnly: "./test/data/whitespace.zip",
        coursesTwoCpsc: "./test/data/Courses10numRows.zip",
        rooms: "./test/data/rooms.zip",
    };
    let datasets: { [id: string]: string } = {};
    let insightFacade: InsightFacade;
    const cacheDir = __dirname + "/../data";

    before(function () {
        // This section runs once and loads all datasets specified in the datasetsToLoad object
        // into the datasets object
        Log.test(`Before all`);
        chai.use(chaiAsPromised);
        if (!fs.existsSync(cacheDir)) {
            fs.mkdirSync(cacheDir);
        }
        for (const id of Object.keys(datasetsToLoad)) {
            datasets[id] = fs
                .readFileSync(datasetsToLoad[id])
                .toString("base64");
        }

        try {
            insightFacade = new InsightFacade();
        } catch (err) {
            Log.error(err);
        }
    });

    beforeEach(function () {
        Log.test(`BeforeTest: ${this.currentTest.title}`);
    });

    after(function () {
        Log.test(`After: ${this.test.parent.title}`);
    });

    afterEach(function () {
        // This section resets the data directory (removing any cached data) and resets the InsightFacade instance
        // This runs after each test, which should make each test independent from the previous one
        Log.test(`AfterTest: ${this.currentTest.title}`);
        try {
            fs.removeSync(cacheDir);
            fs.mkdirSync(cacheDir);
            insightFacade = new InsightFacade();
        } catch (err) {
            Log.error(err);
        }
    });
    // AddData unit tests starts.

    it("Should add a valid room", function () {
        const id: string = "rooms";
        const expected: string[] = [id];
        const futureResult: Promise<string[]> = insightFacade.addDataset(
            id,
            datasets[id],
            InsightDatasetKind.Rooms,
        );
        return expect(futureResult).to.eventually.deep.equal(expected);
    });

//     // This is a unit test. You should create more like this!
//     it("Should add a valid dataset", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.deep.equal(expected);
//     });

//     it("Should add a valid dataset coursesSmall", function () {
//         const id: string = "coursesSmall";
//         const expected: string[] = [id];
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.deep.equal(expected);
//     });

//     it("should add zip files with one invalid file others good", function () {
//         const id: string = "OneInvalidCourses";
//         const expected: string[] = [id];
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.deep.equal(expected);
//     });

//     it("should add both coursesSmall and CourseSmall ids (check for case sensitive)", function () {
//         const id1: string = "coursesSmall";
//         const id2: string = "CoursesSmall";
//         const expected1: string[] = [id1];
//         const expected2: string[] = [id1, id2];
//         return insightFacade
//             .addDataset(id1, datasets[id1], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected1);
//                 const futRes: Promise<string[]> = insightFacade.addDataset(
//                     id2,
//                     datasets[id2],
//                     InsightDatasetKind.Courses,
//                 );
//                 return expect(futRes).to.eventually.deep.equal(expected2);
//             })
//             .catch(() => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("Should reject a dataset with id has underscore", function () {
//         const id: string = "courses_";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset with empty string", function () {
//         const id: string = "";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset id with white space only", function () {
//         const id: string = "    ";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset with id be null", function () {
//         const id: string = null;
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset id be undefined", function () {
//         const id: string = undefined;
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset since the contained file name is not courses", function () {
//         const id: string = "coursesFileUnderScore";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset since the courses file is empty", function () {
//         const id: string = "emptyCourses";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("Should reject a dataset since the contained file name is not courses", function () {
//         const id: string = "whiteSpaceOnly";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("should reject the dataset that is previously added", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureRes: Promise<string[]> = insightFacade.addDataset(
//                     id,
//                     datasets[id],
//                     InsightDatasetKind.Courses,
//                 );
//                 return expect(futureRes).to.eventually.be.rejectedWith(
//                     InsightError,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("should reject with all invalid files in courses", function () {
//         const id: string = "allInvalidFiles";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });

//     it("should reject with not a zip file", function () {
//         const id: string = "notZip";
//         const futureResult: Promise<string[]> = insightFacade.addDataset(
//             id,
//             datasets[id],
//             InsightDatasetKind.Courses,
//         );
//         return expect(futureResult).to.eventually.be.rejectedWith(InsightError);
//     });


//     // tests for removeDataset
//     it("should remove a valid id", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     string
//                 > = insightFacade.removeDataset(id);
//                 return expect(futureResult).to.eventually.deep.equal(id);
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
    });

//     it("should throw notFoundError since remove an valid id that is not found", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     string
//                 > = insightFacade.removeDataset("COURSES");
//                 return expect(futureResult).to.eventually.be.rejectedWith(
//                     NotFoundError,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("should throw InsightError since remove an invalid id (underscore)", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     string
//                 > = insightFacade.removeDataset("courses_");
//                 return expect(futureResult).to.eventually.be.rejectedWith(
//                     InsightError,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("should throw InsightError since remove an invalid id (whitespace)", function () {
//         const id: string = "courses";
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     string
//                 > = insightFacade.removeDataset("    ");
//                 return expect(futureResult).to.eventually.be.rejectedWith(
//                     InsightError,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("should remove a valid dataset with CoursesSmall and courseSmall (case sensitive)", function () {
//         const id1: string = "coursesSmall";
//         const id2: string = "CoursesSmall";
//         const expected: string[] = [id1];
//         const expected2: string[] = [id1, id2];

//         return insightFacade
//             .addDataset(id1, datasets[id1], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 return insightFacade
//                     .addDataset(id2, datasets[id2], InsightDatasetKind.Courses)
//                     .then((res2) => {
//                         expect(res2).to.deep.equal(expected2);
//                         const futureResult: Promise<
//                             string
//                         > = insightFacade.removeDataset("coursesSmall");
//                         return expect(futureResult).to.eventually.deep.equal(
//                             id1,
//                         );
//                     })
//                     .catch(() => {
//                         expect.fail("should not have rejected");
//                     });
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     // Tests for listDatasets
//     it("return empty if no data in datasets", function () {
//         return insightFacade
//             .listDatasets()
//             .then((res) => {
//                 return expect(res).to.has.lengthOf(0);
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected.");
//             });
//     });

//     it("One dataset", function () {
//         const id: string = "coursesSmall";
//         const insightDataset: InsightDataset[] = [
//             {
//                 id: "coursesSmall",
//                 kind: InsightDatasetKind.Courses,
//                 numRows: 57,
//             },
//         ];
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Courses)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     InsightDataset[]
//                 > = insightFacade.listDatasets();
//                 return expect(futureResult).to.eventually.deep.equal(
//                     insightDataset,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("One dataset for room", function () {
//         const id: string = "rooms";
//         const insightDataset: InsightDataset[] = [
//             {
//                 id: "rooms",
//                 kind: InsightDatasetKind.Rooms,
//                 numRows: 364,
//             },
//         ];
//         const expected: string[] = [id];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Rooms)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 const futureResult: Promise<
//                     InsightDataset[]
//                 > = insightFacade.listDatasets();
//                 return expect(futureResult).to.eventually.deep.equal(
//                     insightDataset,
//                 );
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });

//     it("two datasets", function () {
//         const id: string = "rooms";
//         const id2: string = "courses";
//         const insightDataset: InsightDataset[] = [
//             {
//                 id: "rooms",
//                 kind: InsightDatasetKind.Rooms,
//                 numRows: 364,
//             },
//             {
//                 id: "courses",
//                 kind: InsightDatasetKind.Courses,
//                 numRows: 64612,
//             },
//         ];
//         const expected: string[] = [id];
//         const expected2: string[] = [id, id2];
//         return insightFacade
//             .addDataset(id, datasets[id], InsightDatasetKind.Rooms)
//             .then((res) => {
//                 expect(res).to.deep.equal(expected);
//                 return insightFacade
//                     .addDataset(id2, datasets[id2], InsightDatasetKind.Courses)
//                     .then((res2) => {
//                         expect(res2).to.deep.equal(expected2);
//                         const futureResult: Promise<
//                             InsightDataset[]
//                         > = insightFacade.listDatasets();
//                         return expect(futureResult).to.eventually.deep.equal(
//                             insightDataset,
//                         );
//                     })
//                     .catch((err) => {
//                         return expect.fail("should not have rejected");
//                     });
//             })
//             .catch((err) => {
//                 return expect.fail("should not have rejected");
//             });
//     });
// });

/*
 * This test suite dynamically generates tests from the JSON files in test/queries.
 * You should not need to modify it; instead, add additional files to the queries directory.
 * You can still make tests the normal way, this is just a convenient tool for a majority of queries.
 */
describe("InsightFacade PerformQuery", () => {
    const datasetsToQuery: {
        [id: string]: { path: string; kind: InsightDatasetKind };
    } = {
        courses: {
            path: "./test/data/courses.zip",
            kind: InsightDatasetKind.Courses,
        },
        rooms: {
            path: "./test/data/rooms.zip",
            kind: InsightDatasetKind.Rooms,
        },
    };
    let insightFacade: InsightFacade;
    let testQueries: ITestQuery[] = [];

    // Load all the test queries, and call addDataset on the insightFacade instance for all the datasets
    before(function () {
        Log.test(`Before: ${this.test.parent.title}`);

        // Load the query JSON files under test/queries.
        // Fail if there is a problem reading ANY query.
        try {
            testQueries = TestUtil.readTestQueries();
        } catch (err) {
            expect.fail(
                "",
                "",
                `Failed to read one or more test queries. ${err}`,
            );
        }

        // Load the datasets specified in datasetsToQuery and add them to InsightFacade.
        // Will fail* if there is a problem reading ANY dataset.
        const loadDatasetPromises: Array<Promise<string[]>> = [];
        insightFacade = new InsightFacade();
        for (const id of Object.keys(datasetsToQuery)) {
            const ds = datasetsToQuery[id];
            const data = fs.readFileSync(ds.path).toString("base64");
            loadDatasetPromises.push(
                insightFacade.addDataset(id, data, ds.kind),
            );
        }
        return Promise.all(loadDatasetPromises);
    });

    beforeEach(function () {
        Log.test(`BeforeTest: ${this.currentTest.title}`);
    });

    after(function () {
        Log.test(`After: ${this.test.parent.title}`);
    });

    afterEach(function () {
        Log.test(`AfterTest: ${this.currentTest.title}`);
    });

    // Dynamically create and run a test for each query in testQueries
    // Creates an extra "test" called "Should run test queries" as a byproduct. Don't worry about it
    it("Should run test queries", function () {
        describe("Dynamic InsightFacade PerformQuery tests", function () {
            for (const test of testQueries) {
                it(`[${test.filename}] ${test.title}`, function () {
                    const futureResult: Promise<
                        any[]
                    > = insightFacade.performQuery(test.query);
                    return TestUtil.verifyQueryResult(futureResult, test);
                });
            }
        });
    });
});
