import React, { useContext } from "react"

import { Navigate, Outlet } from "react-router-dom"
import HeaderLoggedInState from "../header/HeaderLoggedInState"
import Loading from "../animations/Loading"
import { useSelector } from "react-redux"
import { selectIsLoading } from "../../redux/isLoadingSlice"
import { selectUserState } from "../../redux/userStateSlice"


export const LoggedInUserRoute = () => {
  const isLoading = useSelector(selectIsLoading)
  const userState = useSelector(selectUserState)
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

