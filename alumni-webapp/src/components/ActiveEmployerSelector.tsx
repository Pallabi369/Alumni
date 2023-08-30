import {Fragment, useEffect} from 'react'
import {Menu, Transition} from '@headlessui/react'
import {SelectorIcon} from '@heroicons/react/outline'
import {useQuery} from "react-query";
import {getEmploymentHistory} from "../services/employees";
import {useAppStore} from "../store/appStore";
import {DateTime} from "luxon";
import {InfiniteIcon} from "./InfiniteIcon";
import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {Employment} from "../api";

export default function ActiveEmployerSelector({suspense = true}) {
  const {t: root_t} = useTranslation();

  const {data: employmentsHistory} = useQuery('employmentHistory', getEmploymentHistory(),
    {retry: 1, staleTime: Infinity, suspense});
  const {data: {employments = []} = {}} = employmentsHistory || {};
  const {employment, setEmployment, corporateId} = useAppStore();

  useEffect(() => {
    if (!employment) {
      setEmployment(employments[0]);
    }
  }, [employment, setEmployment, employments]);

  const isSelectable = (that: Employment) => {
    return that.zalarisId !== (employment && employment.zalarisId);
  }

  if (!employment || employments.length === 0) {
    return (<></>)
  }

  return (
    employments.length > 1 ? (
      <>
        <Menu as="div" className="relative w-full">
          <Menu.Button
            className="group w-full bg-white border border-gray-200 rounded-md px-3.5 py-2 text-sm text-left font-medium text-gray-700 hover:bg-gray-100 focus:outline-none"
            role="button">
          <div className="flex w-full justify-between items-center">
            <EmployerCard employment={employment}/>
            <SelectorIcon
              className="flex-shrink-0 h-5 w-5 text-gray-400 group-hover:text-gray-500"
              aria-hidden="true"
            />
          </div>
          </Menu.Button>
          <Transition
            as={Fragment}
            enter="transition ease-out duration-100"
            enterFrom="transform opacity-0 scale-95"
            enterTo="transform opacity-100 scale-100"
            leave="transition ease-in duration-75"
            leaveFrom="transform opacity-100 scale-100"
            leaveTo="transform opacity-0 scale-95"
          >
            <Menu.Items
              className="z-10 mx-3 origin-top absolute right-0 left-0 mt-1 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 divide-y divide-gray-200 focus:outline-none"
              role="menu">
              <div className="py-1">
                {employments.map((employer, index) => (
                  <Menu.Item key={index} disabled={!employer.companyName}>
                    {({active}) => (
                      <span
                        className={[
                          active ? 'bg-gray-100 text-gray-900' : 'text-gray-800',
                          'block px-4 py-2 text-sm'].join(' ')
                        }
                      >
                        {employer.companyName ? (
                          <Link onClick={() => isSelectable(employer) && setEmployment(employer)}
                                to={"/" + corporateId}
                                className={(!isSelectable(employer) && 'pointer-events-none') || 'block'}
                          >
                            {employer.companyName}
                          </Link>
                        ) : (
                          <span className="text-gray-400 cursor-not-allowed italic">
                              {employer.zalarisId} - {root_t('common.inprogress')}
                            </span>
                        )}
                      </span>
                    )}
                  </Menu.Item>
                ))}

              </div>
            </Menu.Items>
          </Transition>
        </Menu>
      </>
    ) : (
      <div
        className="w-full bg-white border border-gray-200 rounded-md px-3.5 py-2 text-sm text-left font-medium text-gray-700"
        role="presentation"
      >
        <EmployerCard employment={employment}/>
      </div>
    )
  )
}


const DateSpan = (props: { value: string | undefined }) => {
  if (!props.value) {
    return (<></>)
  }
  const sapInfiniteDate = '9999-12-31';
  const date: DateTime = DateTime.fromISO(props.value);
  return (<>{props.value === sapInfiniteDate ?
    <InfiniteIcon className="w-4 h-4 inline-flex"/> : date.toLocaleString()}</>)
}


const EmployerCard = (props: {employment: Employment}) => {
  return (
      <div className="flex min-w-0 items-center justify-between space-x-3 w-full">
        <div className="flex-1 flex flex-col min-w-0 text-center">
          <span className="text-blue-800 text-base font-semibold truncate">{props.employment.companyName}</span>
          <div className="text-blue-800 text-xs truncate flex justify-center">
            <DateSpan value={props.employment.startDate}/>
            <span className="mx-2">&#8212;</span>
            <DateSpan value={props.employment.endDate}/>
          </div>
        </div>
      </div>
  )
}
