import {useContext} from "react"
import HeaderLoggedOutState from "./HeaderLoggedOutState"
import HeaderLoggedInState from "./HeaderLoggedInState"
import HomePageContext from "../../contexts/HomePageContext";
import {getUser} from "../../services/otherServices";

function Header() {
  return (
    <header>
      {Boolean(getUser()) ? <HeaderLoggedInState /> : <HeaderLoggedOutState />}
    </header>
  )
}
export default Header
