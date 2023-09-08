import { configureStore } from "@reduxjs/toolkit"
import userStateReducer from "./userStateSlice"
import roleReducer from "./roleSlice"
import groupReducer from "./groupSlice"
import isLoadingReducer from "./isLoadingSlice"
import homePageInReducer from "./homePageInSlice"
import { getUser } from "../services/otherServices"


export const store = configureStore({
  reducer: {
    userState: userStateReducer,
    role: roleReducer,
    group: groupReducer,
    isLoading: isLoadingReducer,
    homePageIn: homePageInReducer
  },
  preloadedState: {userState: getUser()}
})


export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch