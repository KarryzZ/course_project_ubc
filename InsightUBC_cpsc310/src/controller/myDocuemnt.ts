// Write my own Document api for html tree

// process each tr, accroding to each attr.value
// return the td with the given class name.

export const requiredBField: string[] = [
    "views-field views-field-field-building-code",
    "views-field views-field-title",
    "views-field views-field-field-building-address",
];

export const requiredRField: string[] = [
    "views-field views-field-field-room-number",
    "views-field views-field-field-room-capacity",
    "views-field views-field-field-room-furniture",
    "views-field views-field-field-room-type",
    "views-field views-field-nothing",
];

// find the attributes among parents attributs
export function getAttrVal(parent: any, attrname: string) {
    if (!parent.attrs) {
        return null;
    }
    // check if the attribute is valid
    let attrsArray = parent.attrs;
    if (attrsArray.length !== 0) {

        let attr = attrsArray.find((res: any) => res.name === attrname);
        if (attr) {
            return attr.value;
        }
    }
    // return null if the the given attr not found or parent's length is 0
    return null;
}

export function getChildNodeByClassName(tr: any, str: string) {
    if (!tr.childNodes) {
        return null;
    }
    // return the td with the given class name
    return tr.childNodes.find((child: any) => hasClassName(child, str));
}

// helper function to get the td with the given class name
export function hasClassName(node: any, str: string): boolean {
    if (node.attrs && node) { // a valid td
        for (let attr of node.attrs) {
            if (attr.name === "class") {
                if (attr.value) {
                    return attr.value === str;
                }
                return false;
            }
        }
    }
    return false;
}


export function getTrimedText(parent: any) {
    // find the #text under a.
    let text = parent.childNodes.find((node: any) => node.nodeName === "#text");
    // call trim to handle all the empty strings
    return text.value.trim();
}


   // recersively traverse to find the tbody
export function findTBody(root: any, fun: (str: string) => (node: any) => boolean, fields: string[]): any {
    if (!root || !root.childNodes) {
        return null;
    }
    // check if this table contain the valid building datas:
    // with code, title and address
    // response to: "we cannot assume that any given
    // html only has one table elements, but when we find
    // a valid one we assume it is the only one"
    if (isValidTBody(root, fun, fields)) {
        return root;
    }

    for (let node of root.childNodes) {
        let result = findTBody(node, fun, fields);
        if (result) {
            return result;
        }
    }
    return null;
}

function isValidTBody(parent: any, fun: (str: string) => (node: any) => boolean, fields: string[]) {
    if (!parent) {
        return false;
    }
        // check if some nodes at contain the requried (look into tbody)
    let rows = parent.childNodes;
    if (rows) {
        return rows.some((row: any) => hasValidChildren(row, fun, fields));
    }
    return false;

}

// look for valid tr with given class
function hasValidChildren(parent: any, fun: (str: string) => (node: any) =>  boolean, fields: string[]) {
    let nodes: any[] = parent.childNodes; // trs..
    if (nodes) {
        return fields.every((str) => nodes.some(fun(str)));
    }
    return false;
}

export const requiredRules = (str: string) => (node: any) => {
    return (node.tagName === "td") && hasClassName(node, str);
};
