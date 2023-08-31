import {getUser} from "../../services/otherServices";
import React from "react";

import {Navigate, Outlet, Route, Routes, RoutesProps} from "react-router-dom";
import HeaderLoggedInState from "../header/HeaderLoggedInState";
import HeaderLoggedOutState from "../header/HeaderLoggedOutState";


export const LoggedOutUserRoute = () => {
  const userState = getUser()
  if (userState !== undefined) {
    return (
      <Navigate to={"/"} replace/>
    )
  } else {
    return <div><HeaderLoggedOutState/><Outlet></Outlet></div>
  }
}

