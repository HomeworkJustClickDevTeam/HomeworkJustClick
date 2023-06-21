import { Link } from "react-router-dom"
import React, { useContext, useEffect, useState } from "react"
import userContext from "../../../UserContext"
import Loading from "../../animations/Loading"
import DispatchContext from "../../../DispatchContext"
import { Action } from "../../../types/types"

export default function UserSettings(): JSX.Element {
  const { loggedIn, userState } = useContext(userContext)
  const [loading, setLoading] = useState<boolean>(false)
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])
  if (loading) {
    return <Loading></Loading>
  }
  if (!loggedIn) {
    return <>Not log in</>
  }
  return (
    <>
      Ustawienia
      <ul>
        <li>
          <Link to={"/settings/general"}>Ogólne</Link>
        </li>
        <li>
          <Link to={"/settings/appearance"}>Wygląd</Link>
        </li>
        <li>
          <Link to={"/settings/security"}>Bezpieczeństwo</Link>
        </li>
        <li>
          <Link to={"/settings/markingTables"}>Moje tabele ocen</Link>
        </li>
      </ul>
    </>
  )
}
