import {useNavigate} from "react-router-dom"
import {useContext} from "react"

import DispatchContext from "../../contexts/DispatchContext"
import {ActionType} from "../../types/ActionType";
import {logout} from "../../services/otherServices";

function LogOut() {
  const globalDispatch = useContext(DispatchContext)
  const navigate = useNavigate()
  const handleLogout = () => {
    const action: ActionType = {
      type: "logout"
    }
    globalDispatch?.dispatch(action)
    logout()
    navigate("/")
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}
export default LogOut
