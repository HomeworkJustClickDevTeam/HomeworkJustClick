import UsersDisplayer from "./UsersDisplayer";
import {Link} from "react-router-dom";
import {UserInGroupProp} from "../../../types/types";

function Users({id,role}:UserInGroupProp) {
   return( <>
        <UsersDisplayer id={id}/>
        <Link to={`assignments`}>
            <button>
                Zadania domowe
            </button>
        </Link>
        <Link to="/">
            <button type="button">
                Strona główna
            </button>
        </Link>
    </>
   )
}
export default Users