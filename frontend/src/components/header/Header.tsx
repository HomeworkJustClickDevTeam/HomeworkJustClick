import { useContext } from "react"
import UserContext from "../../UserContext"
import HeaderLogout from "./state/HeaderLogout"
import HeaderLoggedIn from "./state/HeaderLoggedIn"

function Header() {
  const userState = useContext(UserContext)
  return (
    <header>
      {userState.loggedIn ? <HeaderLoggedIn /> : <HeaderLogout />}
    </header>
  )
}
export default Header
