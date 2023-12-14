import {AssignmentPropsInterface} from "./AssignmentPropsInterface";
import {AssignmentInterface} from "./AssignmentInterface";

export interface AssignmentListElementPropsInterface extends AssignmentPropsInterface {
  idGroup?: string
  optionalUserId?: string
  createReportButton?: boolean
  handleGenerateReportButtonClick?: () => void
  unfoldedPieChartAssignment?: AssignmentInterface
  handleAssignmentClick?: (clickedAssignment: AssignmentInterface) => void
}