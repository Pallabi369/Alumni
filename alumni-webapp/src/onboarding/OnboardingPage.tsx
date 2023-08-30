import StateRenderer from "./StateRenderer";
import {useMsal} from "@azure/msal-react";
import React, {ReactElement} from "react";
import {loginRequest} from "../authConfig";
import {Signicat} from "../signicat";
import {v4} from "uuid"
import {navigateToBasePath} from "../services/url";
import {getClaimName} from "../services/claims";
import {useTranslation} from "react-i18next";
import PageLoader from "../components/PageLoader";
import {HeartIcon} from "@heroicons/react/outline";

function OnboardingPage() {
  const {instance} = useMsal();

  return (
    <div className="min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full text-center">
        <StateRenderer state='unexpected_error'>
          <UnexpectedError />
        </StateRenderer>
        <StateRenderer state="office_signin">
          <WelcomeOfficeUser />
        </StateRenderer>
        <StateRenderer state="home_signin">
          <GetStartedStep onClick={() => instance.loginRedirect(loginRequest())}/>
        </StateRenderer>
        <StateRenderer state='signed_with_invalid_token'>
          <InvalidToken />
        </StateRenderer>
        <StateRenderer state="verification_required">
          <IdentityVerificationStep />
        </StateRenderer>
        <StateRenderer state="assign_national_id">
          <VerificationResult />
        </StateRenderer>
        <StateRenderer state="national_id_assignment_verification">
          <VerificationResult />
        </StateRenderer>
        <StateRenderer state="loading">
          <PageLoader />
        </StateRenderer>
        <StateRenderer state="assign_national_id_failed">
          <InvalidVerificationCode onClick={() => navigateToBasePath()} />
        </StateRenderer>
        <StateRenderer state="onboarded_nodata">
          <NoDataStep />
        </StateRenderer>
        <StateRenderer state="onboarded_service_unavailable">
          <ServiceUnavailable />
        </StateRenderer>
      </div>
    </div>
  );
}

const UnexpectedError = () => {
  const {t} = useTranslation("", {keyPrefix: 'onboarding.unexpectedError'});

  return (
    <>
      <Logo/>
      <Header title={t('title')}/>
      <Help/>
    </>
  )

}

const GetStartedStep = (props: { onClick: Function }) => {
    const {t} = useTranslation("", {keyPrefix: 'onboarding.getStarted'});

    return (
    <>
      <Logo/>
      <Header title={t('title')}/>
      <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
      <div className="mt-8 md:mt-16">
        <button type="button"
                onClick={() => props.onClick()}
                className="inline-flex items-center px-24 py-4 border border-transparent text-base font-semibold rounded-full shadow-sm text-white bg-orange-500 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
        >
            {t('button')}
        </button>
      </div>
      <Help/>
      <Footer/>
    </>
  )
}

const WelcomeOfficeUser = () => {
  const {t} = useTranslation("", {keyPrefix: 'onboarding.getStarted'});

  return (
    <>
      <Logo/>
      <Header title={t('title')}/>
      <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
      <Help/>
      <Footer/>
    </>
  )
}

const InvalidToken = () => {
  const {t} = useTranslation("", {keyPrefix: 'onboarding.invalidToken'});

  return (
    <>
      <Logo/>
      <Header title={t('title')}/>
      <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
      <Help/>
    </>
  )

}

const IdentityVerificationStep = () => {
  const state = v4();
    const {t} = useTranslation("", {keyPrefix: 'onboarding.identityVerification'});

    return(
    <>
        <Logo/>
        <Header title={t('title')}/>
        <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
        <div className="mt-8 md:mt-16">
          <a href={Signicat.url(window.btoa(state))} onClick={() => Signicat.rememberState(state)}
             className="inline-flex items-center px-24 py-4 border border-transparent text-base font-semibold rounded-full shadow-sm text-white bg-orange-500 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
          >
            {t('button')}
          </a>
        </div>
        <Help/>
    </>
  )
}

const VerificationResult = () => {
    const {t} = useTranslation("", {keyPrefix: 'onboarding.verificationResult'});

    return(
    <>
      <Logo/>
      <Header title={t('title') + ' ' + getClaimName()} />
      <Subtitle subtitle={[t('subtitle.line1'), <br/>, <br/>, t('subtitle.line2')]}/>

    </>
  )
}

const InvalidVerificationCode = (props: { onClick: Function }) => {
    const {t} = useTranslation("", {keyPrefix: 'onboarding.invalidVerificationCode'});

    return(
    <>
        <Logo/>
        <Header title={t('title')}/>
        <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>

        <div className="mt-6 md:mt-12">
            <button type="button"
                onClick={() => props.onClick()}
                className="inline-flex items-center px-24 py-4 border border-transparent text-base font-semibold rounded-full shadow-sm text-white bg-orange-500 hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
            >
                {t('button')}
            </button>
        </div>
      <Help/>
    </>
  )
}

const NoDataStep = () => {
    const {t} = useTranslation("", {keyPrefix: 'onboarding.noDataStep'});

    return(
    <>
        <Logo/>
        <Header title={t('title')}/>
        <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
        <Help/>
    </>
  )
}

const ServiceUnavailable = () => {
  const {t} = useTranslation("", {keyPrefix: 'onboarding.serviceUnavailable'});

  return(
    <>
      <Logo/>
      <Header title={t('title')}/>
      <Subtitle subtitle={[t('subtitle.line1'), <br/>, t('subtitle.line2')]}/>
      <Help/>
    </>
  )
}

const Logo = () => (<img
  className="mx-auto h-16 w-auto"
  src={process.env.PUBLIC_URL + '/zalaris-site-logo.svg'}
  alt="Alumni"
/>);

const Header = (props: { title: string }) => (
  <h4 className="mt-32 text-4xl font-semibold text-blue-800">{props.title}</h4>)
const Subtitle = (props: { subtitle: Array<string|ReactElement> }) => (
  <p className="mt-12 px-6 font-normal text-blue-800">
    {props.subtitle.map((value,ix) =>
      <span key={ix}>{value}</span>
    )}
  </p>)
const Help = () => {
    const {t} = useTranslation("", {keyPrefix: 'onboarding.help'});

    return (
        <div className="mt-4 ">
            <a href={process.env.PUBLIC_URL + '/help_alumni.pdf'}
               className="text-blue-500 text-sm" target="_blank" rel="noreferrer"
            >
                {t('link')}
            </a>
        </div>
    )
}

const Footer = () => {
    return (
        <div className="absolute left-0 bottom-2 w-full flex justify-center text-gray-300 text-xs">
            Made with <HeartIcon className="w-4 h-4 mx-1 text-rose-100"/> by Zalaris
        </div>
    )
}

export default OnboardingPage;
