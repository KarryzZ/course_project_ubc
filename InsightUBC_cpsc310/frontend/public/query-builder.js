const LogicOpeator = {
    all: "AND",
    any: "OR",
    none: "NOT",
};

let mKeys = ["avg", "pass", "fail", "audit", "year", "lat", "lon", "seats"];

/**
 * Builds a query object using the current document object model (DOM).
 * Must use the browser's global document object {@link https://developer.mozilla.org/en-US/docs/Web/API/Document}
 * to read DOM information.
 *
 * @returns query object adhering to the query EBNF
 */
CampusExplorer.buildQuery = () => {
    let query = {};
    let kind = document.querySelector(".nav a[class='nav-item tab active']").getAttribute("data-type");
    const currForm = getCurrFrom(kind);

    // build where
    query.WHERE = buildWhere(currForm,kind);

    // build Columns:

    let order = buildOrder(currForm, kind);

    if (order !== null && order !== undefined && order.length !== 0) {
        query.OPTIONS = {
            COLUMNS: buildColumns(currForm, kind),
            ORDER : order,
        }
    } else {
        query.OPTIONS = {
            COLUMNS: buildColumns(currForm, kind),
        }
    }



    // build group
    let groups = buildGroup(currForm ,kind);
    if (groups.length !== 0) {
        query.TRANSFORMATIONS = {
            GROUP: groups,
            APPLY: buildApply(currForm, kind),
        };
    }

    return query;
};

/**
 * Build the array of checked columns
 *
 * @returns query object adhering to the query EBNF
 * @param {Element} form
 * @param {string} kind
 */

const buildColumns = (form, kind) => {
    const columnContainer = form.querySelector(".columns");
    const checkedCols = getCheckedValues(columnContainer,kind,"field");

    const transformCols = getCheckedValues(columnContainer,kind,"transformation");

    return checkedCols.concat(transformCols);
}

const buildOrder = (form, kind) => {
    let selected = form.querySelectorAll(".order select option[selected = 'selected']");
    if (!selected.length) {
        return [];
    }
    let selectedKeys = [];
    Array.from(selected).forEach((key) => {
        if (key.getAttribute("class") === "transformation") {
            selectedKeys.push(key.value);
        } else {
            selectedKeys.push(kind + "_" + key.value);
        }
    });
    let val = form.querySelector(".descending input").getAttribute("checked");
    let isDescending = !!(val);


    return {
        dir: isDescending? "DOWN" : "UP",
        keys: selectedKeys,
    }
}

/**
 * Build the array of checked group
 *
 * @returns query object adhering to the query EBNF
 * @param {Element} form
 * @param {string} kind
 */

const buildGroup = (form, kind) => {
    const container = form.querySelector(".groups");

    // const groupFields = container.querySelectorAll(".field input[checked='checked']");
    // return Array.from(groupFields)
    //     .map((group) => (kind + group.value));
    return getCheckedValues(container,kind,"field");
};

/**
 * Build the apply
 *
 * @returns query object adhering to the query EBNF
 * @param {Element} form
 * @param {string} kind
 */

const buildApply = (form, kind) => {
    const container = form.querySelector(".transformations");

    const applys = container.querySelectorAll(".transformation");
    let results = [];
    Array.from(applys)
        .forEach((apply) => {
            const name = apply.querySelector(".term input").value;
            const operation = apply.querySelector(".operators select option[selected='selected']").value;
            const field = apply.querySelector(".fields select option[selected='selected']").value;
            let toadd = {
                [name]: {
                    [operation]: (kind + "_" + field),
                },
            };
            results.push(toadd);
        });
    return results;
};



/**
 * Build the where part
 *
 * @returns query object adhering to the query EBNF
 * @param {Element} form
 * @param {string} kind
 */

const buildWhere = (form, kind) => {
    let container = form.querySelector(".conditions");

    // select the outer and innter control groups
    let outerCtrGroup = container.querySelector(".control-group.condition-type");
    let innerCtrGroup = container.querySelector(".conditions-container");


    // process those groups
    let logicOpt = getLogicOperator(outerCtrGroup);
    let conditions = getConditions(innerCtrGroup, kind);

    // empty where
    if (conditions.length === 0) {
        return {};
    }

    return addOuterOpr(logicOpt, conditions);
};

/** wrap up the conditions with the outer
 *
 * @param {"AND" | "OR" | "NOT"} operator
 * @param {any[]} conditions
 */
const addOuterOpr = (operator, conditions) => {
    // one element and or would be dropped
    if (operator === "NOT") {
        if (conditions.length === 1) {
            return {
                NOT: conditions[0]
            };
        } else {
            return {
                NOT: {
                    OR: conditions,
                }
            }
        }
    } else { // AND, OR cases
        if (conditions.length === 1) {
            return conditions[0];
        } else {
            return {[operator]: conditions };
        }
    }
};

/**
 * return all the parsed conditions inside the group given the
 * inner condition group
 *
 * @param {Element} ctrGroup
 * @param {string} kind
 * */

const getConditions = (ctrGroup, kind) => {
    let conditions = ctrGroup.querySelectorAll(".control-group");
    let res = [];

    // pasrse the given condition
    Array.from(conditions).forEach((condition) => {
        let isNegated = !!(condition.querySelector(".control.not input").checked);

        let field = condition.querySelector(".fields select option[selected='selected']").value;
        let mComparator = condition.querySelector(".operators select option[selected='selected']").value;

        let value = condition.querySelector(".term input").value;

        // get the type of the corresponding field
        if (mKeys.includes(field)) {
            value = parseFloat(value);
        }
        let key = kind + "_" + field;
        const filter = {
            [mComparator]: {
                [key] : value,
            },
        };
        res.push(isNegated? ({NOT: filter}) : filter);
        });
    return res;
}

/**
 * Helper method to get all the checked box values
 *
 * @param {Element | null} container
 * @param {string} kind
 * @param {string} selector
 */

const getCheckedValues = (container, kind,selector) => {
    let list = container.querySelectorAll(`.${selector} input[checked='checked']`);
    switch (selector) {
        case "field":
            return Array.from(list).map((ele) => (kind + "_" + ele.value));
        case "transformation":
            return Array.from(list).map((ele) => (ele.value));
    }
}



/**
 * return the corresponding logic operator that
 * bounds to the one user selected in the control group
 *
 * @return{"AND"|"OR"|"NOT"}
 * @param {Element} ctrGroup
 * */

const getLogicOperator = (ctrGroup) => {
    let inputs = ctrGroup.querySelectorAll("input[type='radio']");
    let checkedinput = Array.from(inputs).find((input) => !!input.checked).value;
    return LogicOpeator[checkedinput];
}

/**
 * assign the passed in currid be the selected (Courses or rooms)
 * and return the form container of the corresponding kind.
 *
 * @param{string} currId
 * @return{Element}
 * */

const getCurrFrom = (currId) => {
    return document.querySelector(`form[data-type=${currId}]`);
}
