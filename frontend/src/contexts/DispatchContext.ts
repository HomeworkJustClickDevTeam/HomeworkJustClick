import {createContext, Dispatch} from "react"
import {ActionType} from "../types/ActionType";

const DispatchContext = createContext<Dispatch<ActionType> | undefined>(undefined)

export default DispatchContext
