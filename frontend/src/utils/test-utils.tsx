import {configureStore, PreloadedState} from "@reduxjs/toolkit";
import userStateReducer from "../redux/userStateSlice";
import roleReducer from "../redux/roleSlice";
import groupReducer from "../redux/groupSlice";
import isLoadingReducer from "../redux/isLoadingSlice";
import homePageInReducer from "../redux/homePageInSlice";
import {Provider} from "react-redux";
import {render, RenderOptions} from "@testing-library/react";
import {RootState, setupStore} from "../redux/store";
import {ToolkitStore} from "@reduxjs/toolkit/dist/configureStore";
import {PropsWithChildren} from "react";

interface ExtendedRenderOptions extends Omit<RenderOptions, 'queries'> {
  preloadedState?: PreloadedState<RootState>
  store?: ToolkitStore
}
export const renderWithProviders = (
    ui: React.ReactElement,
    {
      preloadedState = {},
      store = setupStore(preloadedState),
      ...renderOptions
    }: ExtendedRenderOptions={})=> {

  const Wrapper = ({children}:PropsWithChildren<{}>):JSX.Element => {
    return <Provider store={store}>{children}</Provider>
  }

  return {store, ...render(ui, {wrapper: Wrapper, ...renderOptions})}
}