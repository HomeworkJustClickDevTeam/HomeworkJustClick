import GroupAppearanceSettings from "./GroupAppearanceSettings"
import GroupUsersSettings from "./GroupUsersSettings"
import GroupGeneralSettings from "./GroupGeneralSettings"
import React, { useContext } from "react"
import { useNavigate } from "react-router-dom"

export default function GroupSettingsPage() {

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