import {GroupInterface} from "./GroupInterface";
import {StudentResultModel} from "./StudentResult.model";
import {AssignmentReportModel} from "./AssignmentReport.model";

export class GroupReportModel{
  constructor(
    public group: GroupInterface,
    public assignments: AssignmentReportModel[]|null
  ) {
  }
}

