import { createContext } from "react"
import { groupContext } from "./types/types"

const GroupContext = createContext<groupContext>({
  role: "",
  setRole: () => {},
})
export default GroupContext
