import {UserInterface} from "./UserInterface"
import {AssignmentInterface} from "./AssignmentInterface"

export interface SolutionExtendedInterface {
    id: number
    user: UserInterface
    assignment: AssignmentInterface
    comment: string
    creationDateTime: string | Date
}
