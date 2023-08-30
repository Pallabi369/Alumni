import React from "react";
import {ChevronLeftIcon} from "@heroicons/react/outline";
import {Link} from "react-router-dom";

type PageHeaderProps = {
  title: string,
  subtitle?: string,
  backHref?: () => string
}

export function PageHeader(props: PageHeaderProps) {
  return (
    <div className="flex items-top py-8">
      {props.backHref && <ChevronLeft href={props.backHref}/>}
      <div className="">
        <h2 className="font-bold text-3xl text-blue-800 leading-6">{props.title}</h2>
        {props.subtitle && <h3 className="mt-2 max-w-4xl text-sm text-gray-500">{props.subtitle}</h3>}
      </div>
    </div>
  )
}

const ChevronLeft = (props: { href: () => string }) => {
  const linkTo = props.href();
  return (
    <div className="w-6 text-gray-500 -ml-0 xl:-ml-10 mr-4">
      <Link to={linkTo}>
        <ChevronLeftIcon/>
      </Link>
    </div>
  )
}
