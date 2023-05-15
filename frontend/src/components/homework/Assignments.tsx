import {Link} from "react-router-dom";

function Assignments() {
    return(<>
        <Link to="group/assigment/add">
            <button>
                Dodaj zadanie domowe
            </button>
        </Link>
    </>)
}
export default Assignments
