import { createContext } from "react"
import { GroupSetRole } from "./types/types"

const GroupSetRoleContext = createContext<GroupSetRole>({
  setRole: () => {},
})
export default GroupSetRoleContext
