import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {Navigate, Outlet, useLocation} from "react-router-dom";
import React from "react";
import {selectRole} from "../../redux/roleSlice";

interface RoleBasedRoutePropsInterface{
  renderForTeacher: JSX.Element,
  renderForStudent: JSX.Element
}
export const RoleBasedRoute = ({renderForTeacher, renderForStudent}:RoleBasedRoutePropsInterface) => {
  const role = useAppSelector(selectRole)
  if("Teacher" === role)
    return renderForTeacher
  else
    return renderForStudent
}