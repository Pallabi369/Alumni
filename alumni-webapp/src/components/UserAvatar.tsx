import {Menu, Transition} from '@headlessui/react'
import {Fragment} from "react";
import {LogoutIcon} from "@heroicons/react/outline";
import {useTranslation} from "react-i18next";
import {useMsal} from "@azure/msal-react";
import {useAppStore} from "../store/appStore";

export function UserAvatar(props: { name: string, bgColor: string }) {
  const {instance} = useMsal();
  const {corporateId} = useAppStore();

  return corporateId ? (
      <Initials name={props.name} bgColor={props.bgColor}/>
    ) : (
    <UserMenu onLogoutClick={() => instance.logoutRedirect()}>
      <Initials name={props.name} bgColor={props.bgColor}/>
    </UserMenu>
  )
}

export const Initials = (props: { name: string, bgColor: string }) => {
  return (
    <div className={`inline-flex items-center justify-center h-12 w-12 rounded-full ${props.bgColor} `}>
      <span className="text-lg font-medium leading-none text-white">{getInitials(props.name, 2)}</span>
    </div>
  )
}

const TriangleIcon = (props: {className: string}) => (
  <svg width="7px" height="7px" viewBox="0 0 7 7" className={props.className}>
    <polygon fill="currentColor" fillRule="nonzero" points="3.5 0 7 7 0 7"/>
  </svg>
)

const UserMenu = (props: { children: JSX.Element, onLogoutClick: Function }) => {
  const {t} = useTranslation("", {keyPrefix: "common"});

  return (
    <Menu as="div" className="relative flex-shrink-0">
      <Menu.Button className="rounded-full flex text-sm text-white">
        {props.children}
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
          className="z-20 absolute -bottom-14 right-0 -ml-3 mt-2 w-48 rounded-md bg-gray-200 ring-1 ring-gray-200 hidden lg:block">
          <TriangleIcon className="absolute -top-2 right-5 text-gray-200"/>
          <div className=" py-1 ">
            <Menu.Item>
              {({active}) => (
                <button
                  className={`${active ? 'bg-gray-100' : ''} transition-all text-blue-800 group flex items-center w-full px-3 py-1 text-md`}
                  onClick={() => props.onLogoutClick()}
                >
                  <LogoutIcon className={`${active ? 'text-orange-500' : 'text-gray-700'} transition-all w-5 h-5 mr-2`}
                              aria-hidden="true"/>
                  {t('logout')}
                </button>
              )}
            </Menu.Item>
          </div>
        </Menu.Items>
      </Transition>
    </Menu>
  )
}

const getInitials = (name: string, maxInitials: number) => {
  return name.split(/\s/)
    .map(part => part.substring(0, 1).toUpperCase())
    .filter(v => !!v)
    .slice(0, maxInitials)
    .join('')
    .toUpperCase();
}
