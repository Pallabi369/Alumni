import React, {useState} from "react";

import {useTranslation} from "react-i18next";

export default function HideableValue (props: {value: string|number, hideByDefault?: boolean})  {
    const [showValue, setShowValue] = useState<boolean>(!props.hideByDefault);
    const { t } = useTranslation("", { keyPrefix: "common" });

    return showValue ? (<>{props.value}</>) : (
        (
            <span
                className="cursor-pointer"
                onClick={() => setShowValue(true)}
                title={t('clickToShowValue')}
            >*****</span>
        )
    );
}
