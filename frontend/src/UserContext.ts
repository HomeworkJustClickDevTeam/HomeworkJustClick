import {createContext} from "react";
import {PropsForLogin} from "./types/types";
const UserContext = createContext<PropsForLogin>({
    loggedIn:false,
    setLoggedIn: () =>{}
})
export default UserContext