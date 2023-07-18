import {createContext} from "react"
import {ApplicationStateInterface} from "../types/ApplicationStateInterface";

const UserContext = createContext<ApplicationStateInterface>(<ApplicationStateInterface>{
  loggedIn: false,
  userState: {
    token: "",
    userId: "",
  },
})
export default UserContext
