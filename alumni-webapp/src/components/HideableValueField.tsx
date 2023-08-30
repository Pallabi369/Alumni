import {FieldType} from "./types";
import React from "react";
import {useTranslation} from "react-i18next";
import HideableValue from "./HideableValue";

function HideableValueField(props: FieldType) {

    const { t } = useTranslation("", { keyPrefix: "common" });
    const value = props.value === "0.0" ? t('nonApplicable') : props.value;

    return (
        <>
            <div className="sm:col-span-1">
                <dt className="text-sm font-semibold text-blue-700">{props.label}</dt>
                <dd className="mt-1 text-sm text-gray-800">
                    <HideableValue value={value}/>
                </dd>
            </div>
        </>
    )
}

export default HideableValueField;

