import {useState} from 'react'

import {Link, Outlet, useOutletContext} from 'react-router-dom';
import ActiveEmployerSelector from "./components/ActiveEmployerSelector";
import {MenuAlt3Icon,} from "@heroicons/react/solid";
import {UserAvatar} from "./components/UserAvatar";
import {useAppStore} from "./store/appStore";
import {getClaimName} from "./services/claims";

type ContextType = { slideOverOpen: boolean, setSlideOverOpen: Function, setSlideOverAvailable: Function };
export function useSlideOver() {
  return useOutletContext<ContextType>();
}

export default function Layout() {
  const [slideOverOpen, setSlideOverOpen] = useState<boolean>(false);
  const [slideOverAvailable, setSlideOverAvailable] = useState<boolean>(false);
  const { corporateId } = useAppStore();
  const rootHref = '/' + corporateId;

  return (
      <div className="min-h-full">

        {/* MOBILE */}
        <header className="mx-auto px-4 sm:px-6 lg:px-8 pt-4 space-y-4 sticky top-0 z-40 bg-white/80 lg:hidden backdrop-filter backdrop-blur firefox:bg-opacity-90">
          <div className="flex items-center justify-between ">
            <Link to={rootHref} className="">
              <UserAvatar name={getClaimName()} bgColor={'bg-orange-500'}/>
            </Link>
            <Link to={rootHref} className="w-40">
              <img
                  src={process.env.PUBLIC_URL + '/zalaris-site-logo.svg'}
                  alt="Alumni"
              />
            </Link>
            <button className={slideOverAvailable ? 'block': 'invisible'}>
              <MenuAlt3Icon className="h-9 w-9 text-gray-600" onClick={_ => {setSlideOverOpen(true)}} />
            </button>
          </div>

        </header>

        <div className="px-4 sm:px-6 md:px-8 pt-8 lg:hidden">
          <ActiveEmployerSelector/>
        </div>


        {/* DESKTOP */}
        <header className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 hidden lg:block">
            <div className="flex items-center justify-between h-32">
                <Link to={rootHref} className="flex-none w-40">
                    <img
                        src={process.env.PUBLIC_URL + '/zalaris-site-logo.svg'}
                        alt="Alumni"
                    />
                </Link>
                <div className="grow flex items-center justify-end max-w-6xl space-x-4">
                    <div className="lg:w-80 w-64">
                        <ActiveEmployerSelector/>
                    </div>
                    <Link to={rootHref} className="">
                        <UserAvatar name={getClaimName()} bgColor={'bg-orange-500'}/>
                    </Link>

                </div>
            </div>
        </header>

        <main className="">
          <Outlet context={{slideOverOpen, setSlideOverOpen, setSlideOverAvailable}} />
        </main>
      </div>
  )


}


