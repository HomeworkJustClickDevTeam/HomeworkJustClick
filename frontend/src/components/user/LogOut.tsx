import {useNavigate} from "react-router-dom"
import {logout} from "../../services/otherServices";

function LogOut() {
  const navigate = useNavigate()
  const handleLogout = () => {
    logout()
    navigate("/home")
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}

export default LogOut
