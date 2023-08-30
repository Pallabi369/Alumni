import Group from "./Group";
import {ReactNode} from "react";

export type Info<T> = {
  keyPrefix: string
  groups: T,
  renderer?: { [key: string]: ReactNode }
}

export function GroupSet<T>(info: Info<T>) {
  // @ts-ignore
  const groupNames: [keyof T] = Object.keys(info.groups);
  return (
        <dl className="grid grid-cols-1 gap-x-4 gap-y-4 sm:grid-cols-3">
          {groupNames.map(groupName => (
            <Group key={groupName.toString()}
                   keyPrefix={info.keyPrefix + '.group.' + groupName}
                   data={info.groups[groupName]}
                   renderer={info.renderer} />
          ))}
        </dl>
  );
}
