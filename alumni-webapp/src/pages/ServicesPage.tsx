import {SearchIcon} from "@heroicons/react/outline";
import Breadcrumbs from "../components/Breadcrumbs";
import {BasicTile, RootServiceDef, ServiceAudience} from "../components/Tiles";

import {Transition} from "@headlessui/react";
import _ from "lodash";
import {useSearchParams} from "react-router-dom";
import {Component, useState} from "react";


const data = [
    {
        name: 'Personal data',
        service: 'personal',
        tiles: [
            { title: 'Organizational assignment', href: '/services/org-assignment', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Personal information', href: '/services/personal-information', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Addresses', href: '/services/addresses', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Communications', href: '/services/communications', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Basic pay', href: '/services/basic-pay', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Bank details', href: '/services/bank-details', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.ESS},
            { title: 'Request employee data update', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.personal, audience: ServiceAudience.MSS}
        ]
    },
    {
        name: 'Payroll data',
        service: 'payroll',
        tiles: [
            { title: 'Payroll summary', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.payroll, audience: ServiceAudience.ESS},
            { title: 'Payslips', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.payroll, audience: ServiceAudience.ESS}
        ]
    },
    {
        name: 'Time data',
        service: 'time',
        tiles: [
            { title: 'Time accounts', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.time, audience: ServiceAudience.ESS},
            { title: 'Record working time', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.time, audience: ServiceAudience.ESS},
            { title: 'Record absence', href: '#', description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor', service: RootServiceDef.time, audience: ServiceAudience.ESS}
        ]
    },
]

type SearchCriteria = {
    query?: string,
    group?: RootServiceDef,
    audience?:ServiceAudience
}

export default function ServicesPage(props: {service?: string} ) {
    const [searchParams, setSearchParams] = useSearchParams();
    const group = props.service || searchParams.get('group');

    const [criteria, setCriteria] = useState<SearchCriteria>(
        {
            group: group ? RootServiceDef[group as keyof typeof RootServiceDef] : undefined,
        }
    );

    const services = _.filter(data, (s) => {
        return !criteria.group || s.service === criteria.group;
    })


    function clearGroupCriteria():void {
        setCriteria({...criteria, group:undefined})
        setSearchParams({})
    }


    return (
        <div className="py-6 lg:py-12 max-w-7xl mx-auto">
            <div className=" flex flex-col md:flex-row">
                <div className="grow">
                    <Breadcrumbs title={'Services'} />
                </div>
                <div className="px-4 sm:px-6 md:px-8 pt-4 flex justify-between md:justify-end">
                    <div className="space-x-1">
                        <SearchByGroupTag group={ criteria.group||undefined } label={'personal'} onClickedClose={clearGroupCriteria}></SearchByGroupTag>
                    </div>
                    <div>
                        <SearchServiceButton />
                    </div>
                </div>
            </div>

            <div className="px-4 sm:px-6 md:px-8">
                <div className="content">

                    <div className="grid gap-x-8 md:gap-x-16 gap-y-8 grid-cols-1 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-4 items-start">
                        {services.map((group, index) => (
                            <Group name={group.name} tiles={group.tiles} key={index}></Group>
                        ))}
                    </div>

                </div>
            </div>
        </div>
    )
}


function Group(props:any) {

    return (
        <>
            <div className="pt-10 pb-2 border-b border-gray-200 col-span-full">
                <h3 className="leading-6 text-blue-800 font-semibold">{props.name}</h3>
            </div>

            {props.tiles.map((tile:any, index:number) => (
                <Transition
                    key={index}
                    className="sm:col-span-1"
                    appear={true}
                    show={true}
                    enter={`transform ease-in-out duration-300 transition`}
                    enterFrom="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
                    enterTo="translate-y-0 opacity-100 sm:translate-x-0"
                    leave="transition ease-in duration-100"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                >
                    <BasicTile {... tile} />
                </Transition>
            ))}
        </>
    );
}

class SearchServiceButton extends Component<any, any> {
    render() {
        return (
            <button className="px-4 sm:px-6 md:px-8 text-gray-400">
                <SearchIcon className="h-6 w-6"/>
            </button>
        )
    }
}

function SearchByGroupTag(props: {group?:RootServiceDef, label?:string, onClickedClose: ()=>void}) {

    if (!props.group) {
        return (<></>);
    }

    const styleConfig: {[keyof: string]: any} = {
        [RootServiceDef.personal]: {text: 'text-orange-700', bg: 'bg-orange-100', btnText: 'text-orange-400', hoverText: 'hover:text-orange-500', hoverBg: 'hover:bg-orange-200'},
        [RootServiceDef.payroll]: {text: 'text-blue-700', bg: 'bg-blue-100', btnText: 'text-blue-400', hoverText: 'hover:text-blue-500', hoverBg: 'hover:bg-blue-200'},
        [RootServiceDef.time]: {text: 'text-teal-700', bg: 'bg-teal-100', btnText: 'text-teal-400', hoverText: 'hover:text-teal-500', hoverBg: 'hover:bg-teal-200'},
        [RootServiceDef.travel]: {text: 'text-indigo-700', bg: 'bg-indigo-100', btnText: 'text-indigo-400', hoverText: 'hover:text-indigo-500', hoverBg: 'hover:bg-indigo-200'}
    }

    const cfg = styleConfig[props.group];

    return (
        <span className={`inline-flex rounded-full items-center py-0.5 pl-2.5 pr-1 text-sm font-medium ${cfg.bg} ${cfg.text}`}>
            {props.label}
            <button
                type="button"
                className={`flex-shrink-0 ml-0.5 h-4 w-4 rounded-full inline-flex items-center justify-center ${cfg.btnText} ${cfg.hoverBg} ${cfg.hoverText} focus:outline-none focus:text-white`}
                onClick={props.onClickedClose}
            >
              <span className="sr-only">Remove {props.label} option</span>
              <svg className="h-2 w-2" stroke="currentColor" fill="none" viewBox="0 0 8 8">
                <path strokeLinecap="round" strokeWidth="1.5" d="M1 1l6 6m0-6L1 7" />
              </svg>
            </button>
          </span>
    )
}
