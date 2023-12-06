import {AssignmentInterface} from "./AssignmentInterface";
import {StudentModel} from "./Student.model";
import {StudentResultModel} from "./StudentResult.model";

export class AssignmentReportModel {
  constructor(
    public assignment: AssignmentInterface,
    public maxResult: boolean,
    public maxResultPercent: number,
    public minResult: number,
    public minResultPercent: number,
    public avgResult: boolean,
    public avgResultPercent: number,
    public late: boolean,
    public hist: number[],
    public studentsHist: number[],
    public students: StudentResultModel[],
    public description: string
  ) {
  }
}
