import {AssignmentPropsInterface} from "./AssignmentPropsInterface";
import {AssignmentModel} from "./Assignment.model";

export interface AssignmentListElementPropsInterface extends AssignmentPropsInterface {
  idGroup?: string
  optionalUserId?: string
  createReportButton?: boolean
  handleGenerateReportButtonClick?: () => void
  unfoldedPieChartAssignment?: AssignmentModel
  handleAssignmentClick?: (clickedAssignment: AssignmentModel) => void
  resultPoints?: number
}