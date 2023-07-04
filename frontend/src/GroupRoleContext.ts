import { createContext } from "react"

interface GroupRoleInterface {
  role: string
}
const GroupRoleContext = createContext<GroupRoleInterface>(<GroupRoleInterface>{
  role: "",
})

export default GroupRoleContext
