import http = require("http");
import { consoleTestResultHandler } from "tslint/lib/test";

const URL: string = "http://cs310.students.cs.ubc.ca:11316/api/v1/project_team145/";

interface GeoResponse {
    lat?: number;
    lon?: number;
    error?: string;
}

export function getGeoLocation(address: any) {
    let url = URL + encodeURI(address);
    return new Promise<GeoResponse>((res, rej) => {
        http.get(url, (result) => {
            // references: http pakcage documents
            result.setEncoding("utf8");
            let rawData = "";
            result.on("data", (chunk) => {
                rawData += chunk;
            });

            result.on("end", () => {
                const { lat, lon, error } = JSON.parse(rawData);
                if (error) {
                    rej({error});
                } else {
                    res({ lat, lon });
                }
            });
        });
    });
}
