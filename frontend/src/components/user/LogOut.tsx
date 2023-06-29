import { useNavigate } from "react-router-dom"
import { useContext } from "react"

import DispatchContext from "../../DispatchContext"
import { Action } from "../../types/types"

function LogOut() {
  const globalDispatch = useContext(DispatchContext)
  const navigate = useNavigate()
  const handleLogout = () => {
    const logout: Action = {
      type: "logout",
    }
    globalDispatch?.(logout)
    navigate("/")
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}
export default LogOut
