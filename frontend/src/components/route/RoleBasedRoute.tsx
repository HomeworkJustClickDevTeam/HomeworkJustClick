import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {Navigate, Outlet, useLocation, useNavigate, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {selectRole} from "../../redux/roleSlice";
import {useGetGroupAndRole} from "../customHooks/useGetGroupAndRole";
import Loading from "../animation/Loading";
import {useUpdateEffect} from "usehooks-ts";

interface RoleBasedRoutePropsInterface{
  renderForTeacher: JSX.Element,
  renderForStudent: JSX.Element
}
export const RoleBasedRoute = ({renderForTeacher, renderForStudent}:RoleBasedRoutePropsInterface) => {
  const role = useAppSelector(selectRole)
  const {idGroup} = useParams()
  const navigate = useNavigate()
  const user = useAppSelector(selectUserState)
  useGetGroupAndRole(idGroup as unknown as number, user!.id)

  useEffect(()=>{
    if(idGroup === undefined)
      navigate(`/`)
  }, [role])

  if("Teacher" === role)
    return renderForTeacher
  else
    return renderForStudent

}