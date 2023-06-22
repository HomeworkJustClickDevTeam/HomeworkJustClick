import { Link } from "react-router-dom"
import { useContext, useEffect } from "react"
import { Action } from "../../types/types"
import DispatchContext from "../../DispatchContext"

function HomeGuest() {
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: Action = {
      type: "homePageIn",
    }
    globalDispatch?.(action)
  }, [])
  return (
    <>
      <Link to="/login">
        <button type="button">Zaloguj się</button>
      </Link>
      <Link to="/register">
        <button type="button">Zarejestruj się</button>
      </Link>
    </>
  )
}
export default HomeGuest
