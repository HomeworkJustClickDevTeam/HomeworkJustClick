import {useNavigate} from "react-router-dom";

type Props = {
    setLoggedIn : (loggedIn: boolean) => void
}
function LogOut ({setLoggedIn}: Props){

    const navigate = useNavigate()
    const handleLogout = () => {
        setLoggedIn(false)
        localStorage.removeItem("token")
        localStorage.removeItem("id")
        navigate("/")
    }

    return(
        <button onClick={handleLogout}>
            Wyloguj się
        </button>
    )
}
export default LogOut