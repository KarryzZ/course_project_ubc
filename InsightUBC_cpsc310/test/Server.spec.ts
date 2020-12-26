import Server from "../src/rest/Server";

import InsightFacade from "../src/controller/InsightFacade";
import chai = require("chai");
import chaiHttp = require("chai-http");
import * as fs from "fs";
import Response = ChaiHttp.Response;
import {expect} from "chai";
import Log from "../src/Util";
import { InsightDatasetKind } from "../src/controller/IInsightFacade";


const SERVER_URL = `http://localhost:4321`;

describe("Facade D3", function () {

    let facade: InsightFacade = null;
    let server: Server = null;

    chai.use(chaiHttp);

    before(function () {
        facade = new InsightFacade();
        server = new Server(4321);
        // TODO: start server here once and handle errors properly
        return server.start().catch((error) => {
            expect.fail("fail to start with" + error.message);
        });
    });

    after(function () {
        return server.stop();
    });

    beforeEach(function () {
        // might want to add some process logging here to keep track of what"s going on

    });

    afterEach(function () {
        // might want to add some process logging here to keep track of what"s going on
    });
    // helper functions
    const addDatasetTest = (
        id: string,
        kind: InsightDatasetKind,
        content: any,
    ) => {
        return chai.request(SERVER_URL)
        .put(`/dataset/${id}/${kind}`)
        .send(content)
        .set("Content-Type", "application/x-zip-compressed");
    };

    const deleteDatasetTest = (id: string) => {
        return chai.request(SERVER_URL)
        .del(`/dataset/${id}`);
    };

    let courses = fs.readFileSync("./test/data/courses.zip");
    let rooms = fs.readFileSync("./test/data/rooms.zip");

    const testValidquery = {
        WHERE: {
          IS: {
            courses_dept: "cp*"
          }
        },
        OPTIONS: {
          COLUMNS: [
            "courses_dept",
            "minAverage"
          ]
        },
        TRANSFORMATIONS: {
          GROUP: [
            "courses_dept"
          ],
          APPLY: [
            {
              minAverage: {
                MAX: "courses_avg"
              }
            }
          ]
        }
      };

    const testInvalidquery = { OPTIONS: {} };

    const testResults = [{courses_dept: "cpen", minAverage: 86.9}, {courses_dept: "cpsc", minAverage: 95}];
    // Sample on how to format PUT requests
    // it("PUT test for courses dataset", function () {
    //     try {
    //         return chai.request(SERVER_URL)
    //             .put(ENDPOINT_URL)
    //             .send(ZIP_FILE_DATA)
    //             .set("Content-Type", "application/x-zip-compressed")
    //             .then(function (res: Response) {
    //                 // some logging here please!
    //                 expect(res.status).to.be.equal(204);
    //             })
    //             .catch(function (err) {
    //                 // some logging here please!
    //                 expect.fail();
    //             });
    //     } catch (err) {
    //         // and some more logging here!
    //     }
    // });

    // The other endpoints work similarly. You should be able to find all instructions at the chai-http documentation
    it("PUT test for courses dataset", function () {
        try {
            addDatasetTest("courses", InsightDatasetKind.Courses, courses)
                .then(function (res: Response) {
                    // some logging here please!
                    expect(res.status).to.be.equal(200);
                })
                .catch(function (err) {
                    // some logging here please!
                    expect.fail("should get a valid courses;" + err);
                });
        } catch (err) {
            // and some more logging here!
            Log.trace(err);
        }
    });

    it("PUT test for invalid data dataset", function () {
        try {
            addDatasetTest("course1", InsightDatasetKind.Courses, courses)
                .then(function (res: Response) {
                    // some logging here please!
                    expect.fail("should not resolve here");
                })
                .catch(function (err) {
                    expect(err.status).to.be.equal(400);
                });
        } catch (err) {
            Log.trace(err);
        }
    });

    it("DELETE test for data dataset", function () {
        try {
            addDatasetTest("courses", InsightDatasetKind.Courses, courses)
                .then(function (res: Response) {
                    // some logging here please!
                    deleteDatasetTest("courses").then((resolve: Response) => {
                        expect(resolve.status).to.be.equal(200);
                    });
                })
                .catch(function (err) {
                    expect.fail("should not failed;" + err);
                });
        } catch (err) {
            Log.trace(err);
        }
    });

    // it("DELETE test for InsightError", function () {
    //     return  deleteDatasetTest("course")
    //         .then((res) => {
    //             expect.fail("", "", `Should not have fulfilled: ${res}`);
    //         })
    //         .catch((res) => {
    //             expect(res.status).to.be.equal(400);
    //         });
    // });

    it("DELETE test for NotFound", function () {
        return deleteDatasetTest("courses")
            .then((res) => {
                expect.fail(`Should not have fulfilled: ${res}`);
            })
            .catch((res) => {
                expect(res.status).to.be.equal(404);
            });
    });

    it("DELETE test for insightError", function () {
        return deleteDatasetTest("_")
            .then((res) => {
                expect.fail(`Should not have fulfilled: ${res}`);
            })
            .catch((res) => {
                expect(res.status).to.be.equal(400);
            });
    });


    it("POST query test with one valid query and valid datasets", function () {
        try {
            addDatasetTest("courses", InsightDatasetKind.Courses, courses).then( function () {
                return chai.request(SERVER_URL)
                .post("/query")
                .send(testValidquery);
            }).then(function (res: Response) {
                    const { result } = res.body;
                    expect(res.status).to.be.equal(200);
                    expect(result).to.deep.equal(testResults);
                }).catch(function (err) {
                    // some logging here please!
                    expect.fail();
                });

        } catch (err) {
            Log.trace("error...");
        }
    });


    it("POST query test with one invalid query and valid datasets", function () {
        try {
            addDatasetTest("courses", InsightDatasetKind.Courses, courses).then( function () {
                return chai.request(SERVER_URL)
                .post("/query")
                .send(testInvalidquery);
            }).then(function (res: Response) {
                    expect.fail(" should not resolved");
                }).catch(function (err) {
                    // some logging here please!
                    expect(err.status).to.be.equal(400);
                });
        } catch (err) {
            Log.trace("error...");
        }
    });


    it("GET Test for an empty datasets (ONLY RESOLVES NO REJECT)", function () {
        try {
            return chai.request(SERVER_URL)
                .get("/datasets") // /dataset/:id/:kind
                .set("Content-Type", "application/x-zip-compressed")
                .then(function (res: Response) {
                    // some logging here please!
                    expect(res.status).to.be.equal(200);
                })
                .catch(function (err) {
                    // some logging here please!
                    Log.trace(err.message);
                    expect.fail("should get a valid courses;" + err);
                });
        } catch (err) {
            // and some more logging here!
            Log.trace(err);
        }
    });

    const expected = [
        {
            id: "courses",
            kind: InsightDatasetKind.Courses,
            numRows: 64612,
        },
        {
            id: "rooms",
            kind: InsightDatasetKind.Rooms,
            numRows: 364,
        },
    ];

    it("Get Test for valid list of datasets (rooms and courses)", function () {
        let promises: any = [];
        promises.push(addDatasetTest("courses", InsightDatasetKind.Courses, courses));
        promises.push(addDatasetTest("rooms", InsightDatasetKind.Rooms, rooms));
        Promise.all(promises).then((res: any) => {
                const { result } = res.body;
                expect(res.status).to.be.equal(200);
                expect(result).to.deep.equal(expected);
            })
            .catch((err: any) => {
                expect.fail("", "", `Should not have failed: ${err}`);
            });
    });
});
