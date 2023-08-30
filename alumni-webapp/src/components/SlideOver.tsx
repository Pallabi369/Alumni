import {useSlideOver} from "../Layout";
import React, {Fragment, useEffect} from "react";
import {Dialog, Transition} from "@headlessui/react";
import {XIcon} from "@heroicons/react/solid";


export function SlideOver(props: { children: JSX.Element, title: string }) {
    const {slideOverOpen, setSlideOverOpen, setSlideOverAvailable} = useSlideOver();

    useEffect(() => {
        setSlideOverAvailable(true)
        return () => {
            setSlideOverAvailable(false)
        }
    });

    return (
        <Transition.Root show={slideOverOpen} as={Fragment}>
            <Dialog as="div" className="fixed inset-0 overflow-hidden z-50" onClose={() => {setSlideOverOpen(false)}}>
                <div className="absolute inset-0 overflow-hidden">
                    <Dialog.Overlay className="absolute inset-0" />

                    <div className="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10">
                        <Transition.Child
                            as={Fragment}
                            enter="transform transition ease-in-out duration-500 sm:duration-300"
                            enterFrom="translate-x-full"
                            enterTo="translate-x-0"
                            leave="transform transition ease-in-out duration-500 sm:duration-300"
                            leaveFrom="translate-x-0"
                            leaveTo="translate-x-full"
                        >
                            <div className="pointer-events-auto w-screen max-w-md">
                                <div className="flex h-full flex-col overflow-y-scroll bg-white py-6 shadow-xl">
                                    <div className="px-4 sm:px-6 pt-7">
                                        <div className="flex items-start justify-between">
                                            <Dialog.Title className="text-2xl font-medium text-blue-800"> {props.title} </Dialog.Title>
                                            <div className="ml-3 flex h-7 items-center">
                                                <button
                                                    type="button"
                                                    className="rounded-md bg-white text-gray-600 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:ring-offset-2"
                                                    onClick={() => setSlideOverOpen(false)}
                                                >
                                                    <span className="sr-only">Close panel</span>
                                                    <XIcon className="h-6 w-6" aria-hidden="true" />
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="relative mt-6 flex-1 px-4 sm:px-6">
                                        <div className="absolute inset-0 px-4 sm:px-6">
                                            {props.children}
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </Transition.Child>
                    </div>
                </div>
            </Dialog>
        </Transition.Root>
    )
}
