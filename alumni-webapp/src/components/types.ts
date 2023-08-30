import {ReactNode} from "react";

export type FieldType = {
  label: string
  value: string
}

export type GroupType<T> = {
  keyPrefix: string
  data: T,
  renderer?: { [key: string]: ReactNode }
}

export type Dictionary<T> = {
  [index: string]: T[];
}
