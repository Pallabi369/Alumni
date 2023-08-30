import Field from "./Field";
import {FieldType, GroupType} from "./types";
import {t} from "i18next";
import {ReactNode} from "react";
import {GroupHeader} from "./GroupHeader";

function Group<T>(props: GroupType<T>) {
  const fields = extractFields(props.data, props.keyPrefix + ".field", props.renderer)

  return (
    <>
      {fields.length > 0 && <GroupHeader keyPrefix={props.keyPrefix}/> }
      {fields.map((field:any, index:number) => {
        return (
          <field.component key={index} {...field} />
        )}
      )}
    </>
  );
}

function extractFields<T>(data: T, keyPrefix: string, renderer?: { [key: string]: ReactNode }) {
  const fields: FieldType[] = [];
  if (data) {
    let k: keyof T;
    for (k in data) {
      const value = data[k];
      const component: ReactNode = renderer?.[k] || Field
      if (value) {
        // @ts-ignore
        fields.push({label: t([keyPrefix, k].join(".")), value, component})
      }
    }
  }
  return fields;
}

export default Group;
