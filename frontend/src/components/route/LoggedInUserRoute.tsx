import {getUser} from "../../services/otherServices";
import React from "react";

import {Navigate, Outlet, Route, Routes, RoutesProps} from "react-router-dom";
import HeaderLoggedInState from "../header/HeaderLoggedInState";


export const LoggedInUserRoute = () => {
  const userState = getUser()
  if (userState === undefined) {
    return (
      <Navigate to={"/home"} replace/>
    )
  } else {
    return <div><HeaderLoggedInState/><Outlet></Outlet></div>
  }
}

