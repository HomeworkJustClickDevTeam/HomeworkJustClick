import { createContext } from "react"
import { applicationState } from "./types/types"
const UserContext = createContext<applicationState>(<applicationState>{
  loggedIn: false,
  userState: {
    token: "",
    userId: "",
  },
})
export default UserContext
