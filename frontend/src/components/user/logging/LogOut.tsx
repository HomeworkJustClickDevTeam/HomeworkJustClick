import {useNavigate} from "react-router-dom";
import {useContext} from "react";
import userContext from "../../../UserContext";


function LogOut (){
    const {setLoggedIn} = useContext(userContext)
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