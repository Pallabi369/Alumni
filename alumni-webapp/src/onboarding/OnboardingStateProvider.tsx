import React from "react";
import {InterpreterFrom} from "xstate";
import {useInterpret} from "@xstate/react";
import stateMachine from "./stateMachine";

interface OnboardingContextType {
  machineService: InterpreterFrom <typeof stateMachine>;
}

export const OnboardingStateContext = React.createContext({} as OnboardingContextType);

function OnboardingStateProvider(props: {children: React.ReactNode}): React.ReactElement {
  const machineService = useInterpret(stateMachine);
  return (
    <OnboardingStateContext.Provider value={{ machineService }}>
      {props.children}
    </OnboardingStateContext.Provider>
  );
}

export default OnboardingStateProvider;
