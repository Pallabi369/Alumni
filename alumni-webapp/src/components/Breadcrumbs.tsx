import {Link} from 'react-router-dom';

type BreadcrumbsLink = {
  name: string
  to: string
}

type BreadcrumbsProps = {
  title: string
  link?: BreadcrumbsLink
}

function Breadcrumbs(props: BreadcrumbsProps) {
  return (
    <div className="px-4 sm:px-6 md:px-8">
      {props.link && (
        <Link to={props.link.to}
            className={'text-blue-400 text-sm font-medium ' + (props.link.to ? 'underline' : '')}>
          {props.link.name}
        </Link>
      )}
      <div className="pt-4 leading-2 text-blue-800 font-semibold text-3xl">{props.title}</div>
    </div>
  )
}

export default Breadcrumbs;
