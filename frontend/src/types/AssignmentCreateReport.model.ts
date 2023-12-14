export class AssignmentCreateReportModel {
  constructor(
    public assignmentId: number,
    public maxResult: boolean,
    public minResult: boolean,
    public avgResult: boolean,
    public late: boolean,
    public hist: number[]
  ) {
  }
}