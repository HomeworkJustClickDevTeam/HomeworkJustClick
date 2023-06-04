import { createContext, Dispatch } from "react"
import { Action } from "./types/types"

const DispatchContext = createContext<Dispatch<Action> | undefined>(undefined)

export default DispatchContext
