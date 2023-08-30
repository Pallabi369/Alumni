import {useTranslation} from "react-i18next";

export function GroupHeader(props: {keyPrefix: string}) {
    const { t } = useTranslation("", { keyPrefix: props.keyPrefix});

    return (
        <div
            className="pt-10 first:pt-0 pb-2 border-b border-gray-200 sm:col-span-3 ">
            <h3 className="leading-6 text-blue-800 font-semibold">{t("title")}</h3>
            <p className="mt-1 max-w-2xl text-xs text-gray-500">{t("subtitle")}</p>
        </div>
    );
}
