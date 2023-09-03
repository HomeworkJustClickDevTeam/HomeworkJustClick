import {createContext, Dispatch} from "react"
import {ApplicationStateInterface} from "../types/ApplicationStateInterface";
import {ActionTypes} from "../types/ActionTypes";

interface ApplicationStateContextInterface {
  applicationState: ApplicationStateInterface | undefined
  setApplicationState: Dispatch<ActionTypes>
}

const ApplicationStateContext = createContext<ApplicationStateContextInterface>({
  applicationState: undefined, setApplicationState: () => {}
})

export default ApplicationStateContext
