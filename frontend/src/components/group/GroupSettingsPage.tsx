import GroupAppearanceSettings from "./GroupAppearanceSettings";
import GroupUsersSettings from "./GroupUsersSettings";
import GroupGeneralSettings from "./GroupGeneralSettings";
import React, {useContext, useState} from "react";
import Loading from "../animations/Loading";
import {getUser} from "../../services/otherServices";
import {useNavigate} from "react-router-dom";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";
import {LoadingContext} from "../../contexts/LoadingContext";

export default function GroupSettingsPage() {
  const {applicationState} = useContext(ApplicationStateContext)
  const navigate = useNavigate()

  if (applicationState?.userState === undefined) {
    navigate("/")
  }
  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md  h-fit py-2 px-6'>
      <p className='font-bold'>Ustawienia</p>
      <dl>
        <dt className='font-semibold'>Ogólne</dt>
        <dd><GroupGeneralSettings/></dd>
        <dt className='mb-1 mt-2 font-semibold'>Użytkownicy:</dt>
        <dd><GroupUsersSettings/></dd>
        <dt className='font-semibold mt-2'>Zmień kolor grupy:</dt>
        <dd><GroupAppearanceSettings/></dd>
      </dl>
    </div>
  )
}