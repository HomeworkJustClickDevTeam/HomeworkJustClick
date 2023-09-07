import { useNavigate } from "react-router-dom"
import { logout } from "../../services/otherServices"
import { useContext } from "react"
import { useDispatch } from "react-redux"
import { useAppDispatch } from "../../types/HooksRedux"
import { AppDispatch } from "../../redux/store"

function LogOut() {
  const navigate = useNavigate()
  const dispatch:AppDispatch = useAppDispatch()

  const handleLogout = () => {
    logout(dispatch)
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}

export default LogOut
