import {AssignmentModel} from "./Assignment.model";
import {StudentModel} from "./Student.model";
import {StudentResultModel} from "./StudentResult.model";

export class AssignmentReportModel {
  constructor(
    public assignment: AssignmentModel,
    public maxResult: number,
    public maxResultPercent: number,
    public minResult: number,
    public minResultPercent: number,
    public avgResult: number,
    public avgResultPercent: number,
    public late: number,
    public hist: number[],
    public studentsHist: number[],
    public students: StudentResultModel[]|null,
    public description: string
  ) {
  }
}
