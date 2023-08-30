import {HelpdeskTile, MiniTile, MiniTileProps, RootServiceDef} from "../components/Tiles";
import {getClaimName, getClaimZalarisId} from "../services/claims";
import {useTranslation} from "react-i18next";
import {SlideOver} from "../components/SlideOver";
import React from "react";
import {Initials} from "../components/UserAvatar";
import {useMsal} from "@azure/msal-react";
import {PayrollDashboardTile} from "../components/PayrollDashboardTile";

const tiles: Array<{ tile: any, props: MiniTileProps | any, className: string }> = [

  {
    tile: MiniTile,
    props: {name: 'Personal data', href: 'personal-data', service: RootServiceDef.personal},
    className: "col-span-1"
  },
  {
    tile: MiniTile,
    props: {name: 'Payroll data', href: 'payroll-data', service: RootServiceDef.payroll},
    className: "col-span-1"
  },

  {tile: PayrollDashboardTile, props: {}, className: "col-span-1"},
  {
    tile: HelpdeskTile,
    props: {name: 'Helpdesk', service: RootServiceDef.helpdesk},
    className: "col-span-1"
  },
]

export default function DashboardPage() {

  const { t } = useTranslation("", { keyPrefix: "common" });

  const name = [t('welcome'), getClaimName(), "!"].join(' ')
  return (
    <div className={'py-6 lg:py-12 max-w-4xl mx-auto'}>
      <div className="px-4 sm:px-6 md:px-8 py-8 text-center">
        <h2 className="font-bold text-4xl text-blue-800 leading-16">{name}</h2>
        <h3 className="mt-4 text-md text-blue-800">{t('pitch')}</h3>
      </div>

      <div className="px-4 sm:px-6 md:px-8">
        <div className="py-8">
          <div className="content">
            <div
              className="grid grid-flow-row-dense grid-cols-1 gap-x-0 gap-y-4 md:grid-cols-2 md:gap-x-4 lg:grid-cols-2 lg:gap-x-8 lg:gap-y-8 ">

              {tiles.map((item, index) => {
                return (
                  <div key={index} className={item.className}>
                    <item.tile {...item.props}/>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      </div>

      <SlideOver title={t('profile')}>
        <DashboardSlideMenu/>
      </SlideOver>

    </div>
  )
}


function DashboardSlideMenu(props: any) {
  const {instance} = useMsal();

  return (
      <div className="flex-1 space-y-4">
        <div className="flex items-center">
          <div>
            <Initials name={getClaimName()} bgColor='bg-orange-500'/>
          </div>
          <div className="ml-3">
            <p className="text-sm font-medium text-gray-700 group-hover:text-gray-900">{getClaimName()}</p>
            <p className="text-xs font-medium text-gray-500 group-hover:text-gray-700">{getClaimZalarisId()}</p>
          </div>
        </div>

        <hr/>

        <ul className="grid grid-cols-1 gap-4">
          <li>
            <button onClick={() => instance.logoutRedirect()}
               className="cursor-pointer w-full bg-gray-200 text-blue-800 flex justify-center items-center font-semibold block h-20 text-xl rounded-lg">Sign out</button>
          </li>
        </ul>
      </div>
  )
}
