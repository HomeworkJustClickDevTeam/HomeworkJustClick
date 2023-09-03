import {useNavigate} from "react-router-dom"
import {logout} from "../../services/otherServices";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";
import {useContext} from "react";

function LogOut() {
  const navigate = useNavigate()
  const {setApplicationState} = useContext(ApplicationStateContext)

  const handleLogout = () => {
    logout(setApplicationState)
    navigate("/home")
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}

export default LogOut
