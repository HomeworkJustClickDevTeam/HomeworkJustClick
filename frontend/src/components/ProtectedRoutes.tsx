import {getUser} from "../services/otherServices";
import React from "react";

import {Navigate, Routes, RoutesProps} from "react-router-dom";


export const ProtectedRoutes = ({...rest}: RoutesProps) => {
  const userState = getUser()
  if (userState === undefined) {
    return (
      <Navigate to={"/home"} replace/>
    )
  } else {
    return <Routes {...rest}/>
  }
}

