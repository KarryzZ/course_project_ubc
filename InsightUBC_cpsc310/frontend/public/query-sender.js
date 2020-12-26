/**
 * Receives a query object as parameter and sends it as Ajax request to the POST /query REST endpoint.
 *
 * @param query The query object
 * @returns {Promise} Promise that must be fulfilled if the Ajax request is successful and be rejected otherwise.
 */
CampusExplorer.sendQuery = (query) => {
    return new Promise((resolve, reject) => {
        let xmlhttprequest = new XMLHttpRequest();
        xmlhttprequest.open("POST", "http://localhost:4321/query", true);
        xmlhttprequest.onload = function () {
            if (this.status === 400) { // 400: rejects.
                reject(this.responseText);
            } else if (this.status === 200) { // 200: resolves.
                resolve(JSON.parse(this.responseText));
            }
        }
        xmlhttprequest.send(JSON.stringify(query));
    });
};
