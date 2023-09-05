import {getUser} from "../../services/otherServices";
import React, {useContext} from "react";

import {Navigate, Outlet, Route, Routes, RoutesProps} from "react-router-dom";
import HeaderLoggedInState from "../header/HeaderLoggedInState";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";
import {LoadingContext} from "../../contexts/LoadingContext";
import Loading from "../animations/Loading";


export const LoggedInUserRoute = () => {
  const {applicationState} = useContext(ApplicationStateContext)
  const {isLoading} = useContext(LoadingContext)
  if (applicationState?.userState === undefined) {
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

