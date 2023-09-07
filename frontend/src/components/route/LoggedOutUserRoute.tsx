import React, { useContext } from "react"

import { Navigate, Outlet } from "react-router-dom"
import HeaderLoggedOutState from "../header/HeaderLoggedOutState"
import { selectUserState } from "../../redux/userStateSlice"
import { useSelector } from "react-redux"


export const LoggedOutUserRoute = () => {
  const userState = useSelector(selectUserState)
  if (userState !== null) {
    return (
      <Navigate to={"/"} replace/>
    )
  } else {
    return <div><HeaderLoggedOutState/><Outlet></Outlet></div>
  }
}

