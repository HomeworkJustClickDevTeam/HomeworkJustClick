import React from "react"
import UserGeneralSettingsPage from "./UserGeneralSettingsPage"
import UserSecuritySettingsPage from "./UserSecuritySettingsPage"

import UserMarkingTablesSettingsPage from "./UserMarkingTablesSettingsPage"

export default function UserSettingsPage(): JSX.Element {

  return (
    <div className='flex bg-lilly-bg h-[calc(100dvh-40px)] xl:h-[calc(100dvh-64px)] justify-center overflow-y-hidden'>
      <div className=' bg-white xl:w-[40%] lg:w-[50%] w-[70%] px-4 py-3 box-content overflow-y-auto'>
        <p className='text-lg font-bold mb-3'>Ustawienia</p>
        <ul className='relative '>
          <li className=''>
            <p>
              <p className='font-semibold underline my-1 underline-offset-2'>Ogólne </p>
              <UserGeneralSettingsPage/>
            </p>
          </li>
          <hr className='w-32 border-solid border-[1px] mb-2 border-main_blue'/>
          <li>
            <p>
              <p className='font-semibold underline my-1 underline-offset-2'>Bezpieczeństwo </p>
              <UserSecuritySettingsPage/>
            </p>
          </li>
          <hr className='w-32 border-solid border-[1px] mb-2 border-main_blue'/>
          <li>
            <p >
              <p className='font-semibold underline my-1 underline-offset-2 mb-4'>Moje tabele ocen </p>
              <UserMarkingTablesSettingsPage />
            </p>
          </li>
        </ul>
      </div>
    </div>
  )
}
