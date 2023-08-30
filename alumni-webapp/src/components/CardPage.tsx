import {Transition} from "@headlessui/react";
import React, {ReactNode} from "react";

export function CardPage<T>(props: { breadcrumbs: ReactNode, children: ReactNode | ReactNode[] }) {

    let children: React.ReactNode[] = (props.children instanceof Array) ? props.children : [ props.children ];

    return (
        <div className="py-6 lg:py-12 max-w-7xl mx-auto">

            {props.breadcrumbs}

            <div className="px-4 sm:px-6 md:px-8">
                <div className="py-8">

                    {children.map((child, index) => (
                        <Transition
                            key={index}
                            appear={true}
                            show={true}
                            enter={`transform ease-out duration-300 transition`}
                            enterFrom="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
                            enterTo="translate-y-0 opacity-100 sm:translate-x-0"
                            leave="transition ease-in duration-100"
                            leaveFrom="opacity-100"
                            leaveTo="opacity-0"
                        >
                        <div className={`text-gray-800 font-medium block relative overflow-auto mt-6 mb-8`}>
                            {child}
                        </div>
                        </Transition>
                    ))}

                </div>
            </div>
        </div>
    );
}

