import {Link} from "react-router-dom";

function HomeGuest(){

    return(
        <>
            <Link to="/login">
                <button type="button">
                    Zaloguj się
                </button>
            </Link>
            <Link to="/register">
                <button type="button">
                    Zarejestruj się
                </button>
            </Link>
    </>
            )
}
export default HomeGuest