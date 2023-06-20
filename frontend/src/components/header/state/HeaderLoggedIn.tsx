import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link } from "react-router-dom"
import LogOut from "../../user/logging/LogOut"

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  return (
    <>
      {!userState.homePageIn && (
        <Link to="#" onClick={() => window.history.back()}>
          Powrót
        </Link>
      )}
      <Link to="/">Moje Grupy</Link>
      <Link to={`/${userState.userState.userId}/assignments`}>
        Moje zadanie domowe
      </Link>
      <Link to={`/settings`}>Ustawienia</Link>
      <LogOut />
    </>
  )
}
export default HeaderLoggedIn
