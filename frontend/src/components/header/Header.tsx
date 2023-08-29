import {useContext} from "react"
import UserContext from "../../contexts/UserContext"
import HeaderLoggedOutState from "./HeaderLoggedOutState"
import HeaderLoggedInState from "./HeaderLoggedInState"

function Header() {
  const userState = useContext(UserContext)
  return (
    <header>
      {userState.loggedIn ? <HeaderLoggedInState /> : <HeaderLoggedOutState />}
    </header>
  )
}
export default Header
