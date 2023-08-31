import {getUser} from "../../services/otherServices";
import React, {useContext} from "react";

import {Navigate, Outlet, Route, Routes, RoutesProps} from "react-router-dom";
import HeaderLoggedInState from "../header/HeaderLoggedInState";
import HeaderLoggedOutState from "../header/HeaderLoggedOutState";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";


export const LoggedOutUserRoute = () => {
  const {applicationState} = useContext(ApplicationStateContext)
  if (applicationState?.userState !== undefined) {
    return (
      <Navigate to={"/"} replace/>
    )
  } else {
    return <div><HeaderLoggedOutState/><Outlet></Outlet></div>
  }
}

