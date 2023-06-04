import { createContext } from "react"
import { GroupRole } from "./types/types"

const GroupRoleContext = createContext<GroupRole>(<GroupRole>{
  role: "",
})

export default GroupRoleContext
