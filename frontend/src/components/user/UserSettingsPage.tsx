import { useNavigate } from "react-router-dom"
import React, { useContext } from "react"
import UserGeneralSettingsPage from "./UserGeneralSettingsPage"
import UserSecuritySettingsPage from "./UserSecuritySettingsPage"
import UserAppearanceSettingsPage from "./UserAppearanceSettingsPage"
import UserMarkingTablesSettingsPage from "./UserMarkingTablesSettingsPage"

export default function UserSettingsPage(): JSX.Element {

  return (
    <div className='flex bg-lilly-bg h-[100vh] justify-center'>
      <div className=' bg-white w-[40%] px-4 py-3'>
        <p className='text-lg font-semibold'>Ustawienia</p>
        <ul>
          <li>
            <p>
              Ogólne <br/>
              <UserGeneralSettingsPage/>
            </p>
          </li>
          <li>
            <p>
              Wygląd <br/>
              <UserAppearanceSettingsPage/>
            </p>
          </li>
          <li>
            <p>
              Bezpieczeństwo <br/>
              <UserSecuritySettingsPage/>
            </p>
          </li>
          <li>
            <p>
              Moje tabele ocen <br/>
              <UserMarkingTablesSettingsPage/>
            </p>
          </li>
        </ul>
      </div>
    </div>
  )
}
