import {createContext, Dispatch} from "react"
import {ActionType} from "../types/ActionType";
import {ApplicationStateInterface} from "../types/ApplicationStateInterface";

const DispatchContext = createContext<{dispatch: Dispatch<ActionType>, state: ApplicationStateInterface} | undefined>(undefined)

export default DispatchContext
