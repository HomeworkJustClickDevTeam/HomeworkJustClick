import {getUser} from "../../services/otherServices";
import React, {useContext} from "react";

import {Navigate, Outlet, Route, Routes, RoutesProps} from "react-router-dom";
import HeaderLoggedInState from "../header/HeaderLoggedInState";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";


export const LoggedInUserRoute = () => {
  const {applicationState} = useContext(ApplicationStateContext)
  if (applicationState?.userState === undefined) {
    return (
      <Navigate to={"/home"} replace/>
    )
  } else {
    return <div><HeaderLoggedInState/><Outlet></Outlet></div>
  }
}

