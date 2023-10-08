import React from "react"

import { Navigate, Outlet } from "react-router-dom"
import HeaderLoggedOutState from "../header/HeaderLoggedOutState"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"


export const LoggedOutUserRoute = () => {
  const userState = useAppSelector(selectUserState)
  if (userState !== null) {
    return (
      <Navigate to={"/"} replace/>
    )
  } else {
    return <div><HeaderLoggedOutState/><Outlet></Outlet></div>
  }
}

