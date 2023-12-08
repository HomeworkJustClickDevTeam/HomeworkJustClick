import {AssignmentInterface} from "./AssignmentInterface";
import {GroupInterface} from "./GroupInterface";
import {StudentResultModel} from "./StudentResult.model";

export class GroupReportModel{
  constructor(
    public group: GroupInterface,
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

