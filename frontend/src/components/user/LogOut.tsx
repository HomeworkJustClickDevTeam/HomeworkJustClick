import {useNavigate} from "react-router-dom"
import {useContext} from "react"

import DispatchContext from "../../contexts/DispatchContext"
import {ActionType} from "../../types/ActionType";

function LogOut() {
  const globalDispatch = useContext(DispatchContext)
  const navigate = useNavigate()
  const handleLogout = () => {
    const logout: ActionType = {
      type: "logout",
    }
    globalDispatch?.(logout)
    navigate("/")
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}
export default LogOut
