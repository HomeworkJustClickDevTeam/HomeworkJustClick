import { useNavigate } from "react-router-dom"
import { useContext } from "react"
import { useDispatch } from "react-redux"
import { useAppDispatch } from "../../types/HooksRedux"
import { AppDispatch } from "../../redux/store"
import { logOut } from "../../redux/userStateSlice"

function LogOut() {
  const dispatch:AppDispatch = useAppDispatch()

  const handleLogout = () => {
    dispatch(logOut())
  }

  return <button onClick={handleLogout}>Wyloguj się</button>
}

export default LogOut
