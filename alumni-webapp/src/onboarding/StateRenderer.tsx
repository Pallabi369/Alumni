import React, {useContext} from "react";
import {OnboardingStateContext} from "./OnboardingStateProvider";
import {State} from "./events";
import {useActor} from "@xstate/react";

interface StateProps {
  state: State;
  children: React.ReactNode
}

const StateRenderer: React.FunctionComponent<StateProps> = (
  { state,children}) => {
  const onboardingServices = useContext(OnboardingStateContext);
  const [ currentState ] = useActor(onboardingServices.machineService);
  return currentState.matches(state) ?
    (<div>{children}</div>) : (<div/>);
};

export default StateRenderer;
