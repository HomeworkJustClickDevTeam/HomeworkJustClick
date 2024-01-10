import {UserInterface} from "./UserInterface"
import {AssignmentModel} from "./Assignment.model"

export interface SolutionExtendedInterface {
    id: number
    user: UserInterface
    assignment: AssignmentModel
    comment: string
    creationDateTime: Date
}
