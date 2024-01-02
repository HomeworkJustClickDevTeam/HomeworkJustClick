import {AssignmentInterface} from "./AssignmentInterface";
import {StudentModel} from "./Student.model";
import {StudentResultModel} from "./StudentResult.model";

export class AssignmentReportModel {
  constructor(
    public assignment: AssignmentInterface,
    public maxResult: number,
    public maxResultPercent: number,
    public minResult: number,
    public minResultPercent: number,
    public avgResult: number,
    public avgResultPercent: number,
    public late: number,
    public hist: number[],
    public studentsHist: number[],
    public students: StudentResultModel[],
    public description: string
  ) {
  }
}
