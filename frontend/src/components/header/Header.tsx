import {useContext} from "react"
import HeaderLoggedOutState from "./HeaderLoggedOutState"
import HeaderLoggedInState from "./HeaderLoggedInState"
import DispatchContext from "../../contexts/DispatchContext";

function Header() {
  const globalDispatch = useContext(DispatchContext)
  return (
    <header>
      {globalDispatch?.state.loggedIn ? <HeaderLoggedInState /> : <HeaderLoggedOutState />}
    </header>
  )
}
export default Header
