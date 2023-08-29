import {useNavigate} from "react-router-dom"
import {useContext} from "react"

import HomePageContext from "../../contexts/HomePageContext"
import {logout} from "../../services/otherServices";
import HomeGuestPage from "../home/HomeGuestPage";

function LogOut() {
  const navigate = useNavigate()
  const handleLogout = () => {
    logout()
    navigate("/")
    window.location.reload();
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}
export default LogOut
