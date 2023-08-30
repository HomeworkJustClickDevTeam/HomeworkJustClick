import {createContext, Dispatch, SetStateAction} from "react"

interface GroupSetRoleInterface {
  setRole: Dispatch<SetStateAction<string>>
}

const GroupSetRoleContext = createContext<GroupSetRoleInterface>({
  setRole: () => {
  },
})
export default GroupSetRoleContext
