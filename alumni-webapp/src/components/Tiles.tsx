import {Link} from "react-router-dom";
import {getClaimZalarisId} from "../services/claims";
import {useAppStore} from "../store/appStore";

export enum RootServiceDef {
    personal = "personal",
    payroll = "payroll",
    helpdesk = "helpdesk",
    time = "time",
    travel = "travel"
}

export enum ServiceAudience {
    ESS = 'ess',
    MSS = 'mss'
}

type TileProps = {
    service: RootServiceDef
    alt?: string
    href: string
}

type ServiceStyleColors = {
    bg?: string
    border?: string
    text?: string
    shadow?: string
}
const styleConfig = {

    [RootServiceDef.personal]: {border: 'border-blue-700', bg: 'bg-blue-700'} as ServiceStyleColors,
    [RootServiceDef.payroll]: {border: 'border-orange-500', bg: 'bg-orange-500'} as ServiceStyleColors,
    [RootServiceDef.helpdesk]: {border: 'border-blue-400', bg: 'bg-blue-400'} as ServiceStyleColors,
    [RootServiceDef.time]: {border: 'border-indigo-500', bg: 'bg-indigo-500'} as ServiceStyleColors,
    [RootServiceDef.travel]: {border: 'border-indigo-500', bg: 'bg-indigo-500'} as ServiceStyleColors

}


/**
 * Single label tile
 */
export type MiniTileProps = TileProps & { name: string }
export function MiniTile(props: MiniTileProps) {

    const colors = styleConfig[props.service];
    return (
        <Link to={props.href} className={`${colors.bg} rounded-lg h-12 sm:h-24 md:h-48 flex justify-center items-center text-white text-lg sm:text-2xl md:text-3xl font-semibold block`}>
            {props.name}
        </Link>
    )
}

/**
 * Basic tile with title and description
 */
export type BasicTileProps = TileProps & {
    title: string
    description: string
    audience: ServiceAudience
}
export function BasicTile(props: BasicTileProps) {

    const colors = styleConfig[props.service];
    const tagVisible = props.audience === ServiceAudience.ESS ? 'hidden': '';

    return (
        <Link to={props.href} className={`tile border border-gray-200 md:h-56 md:w-56 rounded-md bg-white  pt-6 pb-16 px-8 text-gray-800 font-medium block relative `}>
            <h3 className="pt-2 pb-6">{props.title}</h3>
            <p className="text-sm text-gray-400 font-normal">{props.description}</p>
            <span className={`${tagVisible} absolute ${colors.bg} text-white text-xs p-1 left-0 bottom-0 rounded-tr-md rounded-bl-md`}>{props.audience}</span>
        </Link>
    )
}

/**
 * Custom tile with Call To Action
 */
export function HelpdeskTile(props: MiniTileProps) {
    const { corporateId, employment } = useAppStore();
    const helpdeskContext = corporateId || `alumni?zalarisId=${employment?.zalarisId}`;
    const helpdeskUrl = [window.__RUNTIME_CONFIG__.HELPDESK_PATH, helpdeskContext].join('/');
    const colors = styleConfig[props.service];

    return (<a href={helpdeskUrl} className={`${colors.bg} rounded-lg h-12 sm:h-24 md:h-48 flex justify-center items-center text-white text-lg sm:text-2xl md:text-3xl font-semibold block`}>
        {props.name}
    </a>)

}


