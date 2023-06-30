import { Dispatch, SetStateAction } from "react"
import {GroupInterface} from "./GroupInterface";
import {UserInterface} from "./UserInterface";

interface Assigment {
  title: string
  visible: boolean
  taskDescription: string
  completionDatetime: Date
  id: number
  max_points: number
  groupId: number
}
interface AssigmentToSend {
  title: string
  visible: boolean
  taskDescription: string
  completionDatetime: Date
  max_points: number
}
interface AssigmentProps {
  assignment: Assigment
}
interface AssigmentModifyProps extends AssigmentProps {
  setAssigment: (assignment: (prevState) => any) => void
}
interface AssigmentItemProps extends AssigmentProps {
  idGroup: string
  optionalUserId?: string
}


type FileRespondMongo = {
  id: string
  name: string
  format: string
}
type SolutionToSend = {
  creationDatetime: string
  lastModifiedDatetime: string
  comment: string
}
interface userState {
  token: string
  userId: string
}
interface ApplicationState {
  loggedIn: boolean
  homePageIn: boolean
  userState: userState
}
type Action =
  | { type: "login"; data: User }
  | { type: "logout" }
  | { type: "homePageOut" }
  | { type: "homePageIn" }

interface GroupRole {
  role: string
}
interface GroupSetRole {
  setRole: Dispatch<SetStateAction<string>>
}
interface PropsForType {
  type: string
}
interface Solution {
  id: number
  userId: number
  assignmentId: number
  groupId: number
  creationDateTime: string
  lastModifiedDateTime: string
  comment: string
}
interface SolutionTypesProp {
  id: string
  setSolutionsExtended: (solutionExtended: SolutionExtended[]) => void
}
interface SolutionExtended {
  id: number
  user: UserInterface
  assignment: Assigment
}
interface FileFromPost {
  mongo_id: string
  id: number
  format: string
  name: string
}

interface RatingProps {
  maxPoints: number
  points: number | undefined
  setPoints: (number) => void
  solutionId: number
  groupId: number
}
