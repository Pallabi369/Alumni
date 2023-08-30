import {FieldType} from "./types";
import React from "react";
import {UserIcon} from "@heroicons/react/solid";
import _ from "lodash";

function ChildrenNumberField(props: FieldType) {

    const count = + _.toInteger(props.value);

    return (
        <>
            <div className="sm:col-span-1">
                <dt className="text-sm font-semibold text-blue-700">{props.label}</dt>
                <dd className="mt-1 text-sm text-gray-800">
                    <HeadCount count={count}></HeadCount>
                </dd>
            </div>
        </>
    )
}

export default ChildrenNumberField;

const HeadCount = (props: {count:number}) => {

    if (props.count === 0) {
        return (<span>&#8212;</span>)
    }

    return (
        <ol className="flex items-center space-x-1 py-1">
            {[...Array(props.count)].map((e, i) => <li key={i}><UserIcon className="h-4 w-4 text-gray-800" aria-hidden="true" /></li>)}
        </ol>
    )
}
