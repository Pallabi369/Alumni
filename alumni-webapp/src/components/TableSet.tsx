import {GroupHeader} from "./GroupHeader";
import React, {ReactNode} from "react";
import {useTranslation} from "react-i18next";
import _ from "lodash";
import {FieldType} from "./types";


export type TableSetProps<T> = {
    keyPrefix: string
    elements: Array<T>,
    renderer?: { [key: string]: ReactNode },
    columnOrder?: Array<string>
}

export function TableSet<T>(props: TableSetProps<T>) {

    if (!props.elements || props.elements.length === 0 ) {
        return (<></>)
    }

    const columns: Array<string> = sortByArray({source: extractColumns(props.elements), by: props.columnOrder||[]});

    return (
        <>
            <GroupHeader keyPrefix={props.keyPrefix}/>
            <div className="flex flex-col">
                <div className="overflow-x-auto">
                    <div className="align-middle inline-block min-w-full">
                        <div className="overflow-hidden border-b border-gray-200">

                            <Table
                            keyPrefix={props.keyPrefix}
                            elements={props.elements}
                            columns={columns}
                            renderer={props.renderer}
                            />

                        </div>
                    </div>
                </div>
            </div>

        </>
    )

}

function Table<T>(props: {columns: Array<string>, elements: Array<T>, keyPrefix: string, renderer?: { [key: string]: ReactNode }}) {
    const { t } = useTranslation("", { keyPrefix: props.keyPrefix});

    const Cell = (props: { name: string, value: string, renderer: any }) => {
        return (<props.renderer {...props}/>)
    }

    const CellValue = (props: FieldType) => (props.value);

    function getRenderer(col:string): ReactNode {
        if (props.renderer && props.renderer[col]) {
            return props.renderer[col];
        }
        return CellValue;
    }

    return (<table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
        <tr>
            {props.columns.map((col, index)=> (
                <th key={index} scope="col" className={`px-6 py-3 text-left text-xs font-semibold text-blue-700 uppercase tracking-wider w-1/${props.columns.length}`} >
                    {t('field.' + col)}
                </th>
            ))}
        </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
            {props.elements.map((row:any, index) => (
                <tr key={index}>
                    {props.columns.map((col, index)=> (
                        <td key={index} className="px-6 py-4 whitespace-nowrap text-sm text-gray-800">
                            <Cell name={col} value={row[col]} renderer={getRenderer(col)}/>
                        </td>
                    ))}
                </tr>
            ))}
        </tbody>
    </table>)
}

function extractColumns<T>(data: Array<T>): Array<string> {
    const columns: Set<string> = new Set<string>();
    data.forEach(value => {
        Object.keys(value).forEach(column => columns.add(column));
    })
    return Array.from(columns);
}

function sortByArray<T, U>({ source, by, sourceTransformer = _.identity }: { source: T[]; by: U[]; sourceTransformer?: (item: T) => U }) {
    const indexesByElements = new Map(by.map((item, idx) => [item, idx]));
    return _.sortBy(source, (p) => indexesByElements.get(sourceTransformer(p)));
}
