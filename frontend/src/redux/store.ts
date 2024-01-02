import {combineReducers, configureStore, PreloadedState} from "@reduxjs/toolkit";
import userStateReducer from "./userStateSlice"
import roleReducer from "./roleSlice"
import groupReducer from "./groupSlice"
import isLoadingReducer from "./isLoadingSlice"
import homePageInReducer from "./homePageInSlice"
import groupIdReducer from "./groupIdSlice"


const rootReducer = combineReducers({
    userState: userStateReducer,
    role: roleReducer,
    group: groupReducer,
    isLoading: isLoadingReducer,
    homePageIn: homePageInReducer,
    groupId: groupIdReducer
})
export const setupStore = (preloadedState?: PreloadedState<RootState>) => {
    return configureStore({
        reducer: rootReducer,
        preloadedState
    })
}


export type RootState = ReturnType<typeof rootReducer>
export type AppStore = ReturnType<typeof setupStore>
export type AppDispatch = AppStore['dispatch']