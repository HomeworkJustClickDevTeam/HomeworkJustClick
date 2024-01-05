import { useAppDispatch } from "../../types/HooksRedux"
import { AppDispatch } from "../../redux/store"
import { logOut } from "../../redux/userStateSlice"

function LogOut() {
  const dispatch: AppDispatch = useAppDispatch()

  const handleLogout = () => {
    dispatch(logOut())
  }

  return <button onClick={handleLogout} className="flex items-center h-full hover:bg-hover_blue px-2 xl:px-4">Wyloguj się</button>
}

export default LogOut
