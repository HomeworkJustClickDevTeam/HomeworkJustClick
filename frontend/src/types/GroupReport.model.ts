import {AssignmentInterface} from "./AssignmentInterface";
import {GroupInterface} from "./GroupInterface";
import {StudentResultModel} from "./StudentResult.model";

export class GroupReportModel{
  constructor(
    public group: GroupInterface,
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

