import {Employment} from "../api";
import {useEffect, useState} from "react";

export type AppStore = {
  readonly ssid?: string
  readonly employment?: Employment
  readonly corporateId: string

  setSsid: (ssid: string) => void
  setEmployment: (employment: Employment) => void
  setCorporateId: (cid: string) => void
  clear: () => void
}

export const APP_STORE = "alumniAppStore";

const createEmitter = () => {
  const subscriptions = new Map();
  return {
    emit: (v: any) => subscriptions.forEach(fn => fn(v)),
    subscribe: (fn: any) => {
      const key = Symbol();
      subscriptions.set(key, fn);
      return () => subscriptions.delete(key);
    },
  }
}

export const createAppStore = () => {
  const emitter = createEmitter();

  const setSsid = (ssid: string) => {
    store = {...store, ssid};
    sessionStorage.setItem(APP_STORE, JSON.stringify(store));
    emitter.emit(store);
  }

  const setEmployment = (employment: Employment) => {
    store = {...store, employment};
    sessionStorage.setItem(APP_STORE, JSON.stringify(store));
    emitter.emit(store);
  }

  const setCorporateId = (corporateId: string) => {
    store = {...store, corporateId};
    sessionStorage.setItem(APP_STORE, JSON.stringify(store));
    emitter.emit(store);
  }

  const clear = () => {
    sessionStorage.removeItem(APP_STORE);
    emitter.emit(store);
  }

  const get = () : AppStore => store;

  let store = (() => {
    const appStore = sessionStorage.getItem(APP_STORE);
    return {...(appStore && JSON.parse(appStore)) || {}, setSsid, setEmployment, setCorporateId, clear};
  })();

  const useStore = () => {
    const [localStore, setLocalStore] = useState(get());
    // @ts-ignore
    useEffect(() => emitter.subscribe(setLocalStore), []);
    return localStore;
  };

  return {
    useStore, get
  }
}

export const { get : appStore, useStore : useAppStore } = createAppStore();
