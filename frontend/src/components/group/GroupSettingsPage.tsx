import GroupAppearanceSettings from "./GroupAppearanceSettings"
import GroupUsersSettings from "./GroupUsersSettings"
import GroupGeneralSettings from "./GroupGeneralSettings"
import React from "react"

export default function GroupSettingsPage() {

  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md  h-fit py-2 px-6 overflow-y-hidden'>
      <p className='font-bold'>Ustawienia</p>
      <dl className='flex flex-col box-content overflow-y-auto'>
        <dt className='font-semibold'>Ogólne</dt>
        <dd><GroupGeneralSettings/></dd>
          <dt className='font-semibold mt-2 mb-2'>Zmień kolor grupy:</dt>
          <dd><GroupAppearanceSettings/></dd>
        <dt className='mb-1 mt-2 font-semibold'>Użytkownicy:</dt>
        <dd><GroupUsersSettings/></dd>

      </dl>
    </div>
  )
}