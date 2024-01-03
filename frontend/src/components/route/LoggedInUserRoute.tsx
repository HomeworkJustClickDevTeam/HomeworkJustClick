import React from "react"

import {Navigate, Outlet} from "react-router-dom"
import HeaderLoggedInState from "../header/HeaderLoggedInState"
import Loading from "../animations/Loading"
import {selectIsLoading} from "../../redux/isLoadingSlice"
import {selectUserState} from "../../redux/userStateSlice"
import {useAppSelector} from "../../types/HooksRedux"


export const LoggedInUserRoute = () => {
    const isLoading = useAppSelector(selectIsLoading)
    const userState = useAppSelector(selectUserState)
    if (userState === null) {
        return (
            <Navigate to={"/home"} replace/>
        )
    } else {
        return (
            <div>
                <HeaderLoggedInState/>
                {isLoading ? <Loading/> : <Outlet/>}
            </div>
        )
    }
}

