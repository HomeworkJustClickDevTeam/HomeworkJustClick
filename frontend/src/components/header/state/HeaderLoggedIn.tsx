import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link, useNavigate } from "react-router-dom"
import LogOut from "../../user/logging/LogOut"

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  const navigate = useNavigate()
  return (
    <>
      {!userState.homePageIn && (
        <Link to="#" onClick={() => navigate(-1)}>
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
