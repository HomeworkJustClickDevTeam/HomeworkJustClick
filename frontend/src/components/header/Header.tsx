import { useContext } from "react"
import UserContext from "../../UserContext"
import HeaderLogoutState from "./HeaderLogoutState"
import HeaderLoggedInState from "./HeaderLoggedInState"

function Header() {
  const userState = useContext(UserContext)
  return (
    <header>
      {userState.loggedIn ? <HeaderLoggedInState /> : <HeaderLogoutState />}
    </header>
  )
}
export default Header
