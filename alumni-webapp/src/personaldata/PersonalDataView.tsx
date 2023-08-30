import React, {FunctionComponent, useState} from "react";
import {useTranslation} from "react-i18next";
import {useQuery} from "react-query";
import {cacheableQueryKey} from "../services/services";
import {getPersonalData} from "../services/employees";
import OrgAssignmentView from "./OrgAssignmentView";
import {Link as ScrollLink} from "react-scroll/modules";
import {SlideOver} from "../components/SlideOver";
import {useSlideOver} from "../Layout";
import PersonalInformationView from "./PersonalInformationView";
import AddressesView from "./AddressesView";
import CommunicationsView from "./CommunicationsView";
import BankDetailsView from "./BankDetailsView";
import BasicPayView from "./BasicPayView";


type SectionType = {id: string, component: any, label: string }

export function PersonalDataView(props: { keyPrefix:string }) {

    const { t } = useTranslation("", { keyPrefix: props.keyPrefix });
    const {setSlideOverOpen} = useSlideOver();
    const [, setShowToC] = useState<boolean>(true);

    const { data } =
        useQuery(cacheableQueryKey('personalData'),
            getPersonalData(), { retry: 1, staleTime: Infinity, suspense: true});

    const personalData = data && data.data;

    if (!personalData) {
        return (<div>No personal data data</div>)
    }

    const sections: Array<SectionType> = [
        {
            id: 'organizationalAssignment',
            component: <OrgAssignmentView keyPrefix={`${props.keyPrefix}.organizationalAssignment`} data={personalData.orgAssignment} />,
            label: t(`organizationalAssignment.title`)
        },
        {
            id: 'personalInformation',
            component: <PersonalInformationView keyPrefix={`${props.keyPrefix}.personalInformation`} data={personalData.personalInformation} />,
            label: t(`personalInformation.title`)
        },
        {
            id: 'addresses',
            component:  <AddressesView keyPrefix={`${props.keyPrefix}.addresses`} data={personalData.addresses} />,
            label: t(`addresses.title`)
        },
        {
            id: 'communications',
            component:  <CommunicationsView keyPrefix={`${props.keyPrefix}.communications`} data={personalData.communications} />,
            label: t(`communications.title`) },
        {
            id: 'basicPay',
            component:  <BasicPayView keyPrefix={`${props.keyPrefix}.basicPay`}
                                      data={personalData.basicPay}
                                      recurringPaymentDeductions={personalData.recurringPaymentDeductions}
                                      additionalPaymentDeductions={personalData.additionalPaymentDeductions} />,
            label: t(`basicPay.title`) },
        {
            id: 'bankDetails',
            component:  <BankDetailsView keyPrefix={`${props.keyPrefix}.bankDetails`} data={personalData.bankDetails} />,
            label: t(`bankDetails.title`)
        }
    ]

    return (
        <>
            <TableOfContent
                sections={sections}
                onClick={()=>{setShowToC(false)}}
                groupClass={`hidden lg:flex space-x-4 py-8 sticky top-0 bg-white/90 backdrop-blur-sm`}
                itemClass='px-4 py-3 text-sm rounded-md' />


            {sections.map((item, index) => {
                return (
                    <Section {...item} key={index}>
                        <SectionHeader header={t(`${item.id}.title`)} description={t(`${item.id}.subtitle`)}/>
                        {item.component}
                    </Section>
                )
            })}

            <div className="h-screen -mt-96"></div>

            <SlideOver title={t('title')}>
                <TableOfContent
                    sections={sections}
                    onClick={() => {setSlideOverOpen(false)}}
                    groupClass='grid grid-cols-1 gap-4'
                    itemClass='h-20 text-xl rounded-lg'
                />
            </SlideOver>
        </>
    )
}


const SectionHeader = (props: {header: string, description: string}) => {
    return (
        <div className='pb-8'>
            <h3 className="text-3xl leading-6 font-semibold text-blue-800">{props.header}</h3>
            {props.description && <p className="mt-4 max-w-4xl text-sm text-blue-800">
                {props.description}
            </p>}
        </div>
    );
}

const Section: FunctionComponent = (props: any) => {
    return (
        <section id={props.id} className='first:pt-0 pt-12 pb-12'>
            { props.children }
        </section>
    )
}

const TableOfContent = (props: {sections: Array<SectionType>, onClick: any, groupClass?: string, itemClass?: string}) => {

    return (
        <ul className={props.groupClass}>
            {props.sections.map((item, index) => {
                return (
                    <li key={index}>
                        <ScrollLink
                            to={item.id}
                            spy={true}
                            smooth={true}
                            offset={-80}
                            onClick={() => {props.onClick()}}
                            activeClass={'!bg-blue-800 !text-white'}
                            className={`cursor-pointer bg-gray-200 text-blue-800 flex justify-center items-center font-semibold block ${props.itemClass}`}>
                            {item.label}
                        </ScrollLink>
                    </li>
                )
            })
            }
        </ul>
    )
}
