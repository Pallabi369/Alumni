import React, {Suspense} from "react";
import {useTranslation} from "react-i18next";
import {Transition} from '@headlessui/react'
import {PageHeader} from "../components/PageHeader";
import {PersonalDataView} from "../personaldata/PersonalDataView";
import {linkTo} from '../routes';


export function PersonalDataPage() {

  const keyPrefix = 'service.personalData';
  const {t} = useTranslation("", {keyPrefix});

  return (
    <div className="py-6 lg:py-12 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
      <PageHeader title={t('title')} subtitle={t('subtitle')} backHref={() => linkTo('/')}/>
      <Transition
        appear={true}
        show={true}
        className=""
        enter="transition-opacity duration-500"
        enterFrom="opacity-0"
        enterTo="opacity-100"
        leave="transition-opacity duration-500"
        leaveFrom="opacity-100"
        leaveTo="opacity-0"
      >
        <Suspense fallback={<PersonalDataSuspense/>}>
          <PersonalDataView keyPrefix={keyPrefix}/>
        </Suspense>
      </Transition>
    </div>
  )
}

function PersonalDataSuspense() {

  function TableOfContentMock() {
    return (
      <ul className="flex justify-start space-x-4 h-12 animate-pulse">
        <li className="w-40 bg-gray-200  rounded-lg"></li>
        <li className="w-40 bg-gray-200  rounded-lg"></li>
        <li className="w-40 bg-gray-200  rounded-lg"></li>
        <li className="w-40 bg-gray-200  rounded-lg"></li>
      </ul>
    )
  }

  function FieldMock() {
    return (
      <div className="sm:col-span-1">
        <dt className="bg-blue-700 h-4 w-48 rounded-md opacity-50"></dt>
        <dd className="bg-gray-800 h-4 w-36 mt-2 rounded-md opacity-50"></dd>
      </div>
    )
  }

  function SectionMock() {
    return (
      <ul className="mt-20 animate-pulse">
        <li className="">
          <h3 className="w-1/3 h-8 bg-blue-800 mb-16 opacity-50 rounded-md">&nbsp;</h3>
          <hr className="mb-8"/>
          <div className="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-3">
            {[...Array(5)].map((e, i) => (<FieldMock key={i} />))}
          </div>
          <hr className="my-16"/>
          <div className="grid grid-cols-1 gap-x-4 gap-y-6 sm:grid-cols-3">
            {[...Array(4)].map((e, i) => (<FieldMock key={i} />))}
          </div>
        </li>
      </ul>
    )
  }

  return (
    <Transition
      appear={true}
      show={true}
      enter="transition-opacity duration-75"
      enterFrom="opacity-0"
      enterTo="opacity-100"
      leave="transition-opacity duration-150"
      leaveFrom="opacity-100"
      leaveTo="opacity-0"
    >
      <div className="pt-8 opacity-20">

        <TableOfContentMock/>
        <SectionMock/>
        <SectionMock/>

      </div>
    </Transition>
  )
}
