import {Link} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import Loading from "../animations/Loading"
import DispatchContext from "../../contexts/DispatchContext"
import {ActionType} from "../../types/ActionType";
import {getUser} from "../../services/otherServices";

export default function UserSettingsPage(): JSX.Element {
  const userState = getUser()
  const [loading, setLoading] = useState<boolean>(false)
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: ActionType = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])
  if (loading) {
    return <Loading></Loading>
  }
  if (userState === undefined) {
    return <>Not log in</>
  }
  return (
      <div className='flex bg-lilly-bg h-[100vh] justify-center'>
          <div className=' bg-white w-[40%] px-4 py-3'>
              <p className='text-lg font-semibold'>Ustawienia</p>
              <ul>
                  <li>
                      <Link to={"/settings/general"}>
                          Ogólne
                      </Link>
                  </li>
                  <li><Link to={"/settings/appearance"}>Wygląd</Link></li>
                  <li>
                      <Link to={"/settings/security"}>
                          Bezpieczeństwo
                      </Link>
                  </li>
                  <li><Link to={"/settings/markingTables"}>Moje tabele ocen</Link></li>
              </ul>
          </div>
      </div>
  )
}
