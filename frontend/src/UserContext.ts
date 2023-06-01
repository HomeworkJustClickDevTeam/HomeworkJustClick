import { createContext } from "react"
import { ApplicationState } from "./types/types"
const UserContext = createContext<ApplicationState>(<ApplicationState>{
  loggedIn: false,
  userState: {
    token: "",
    userId: "",
  },
})
export default UserContext
