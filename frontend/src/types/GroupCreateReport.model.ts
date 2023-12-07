export class GroupCreateReportModel{
  constructor(
    public groupId: number,
    public maxResult: boolean,
    public minResult: boolean,
    public avgResult: boolean,
    public late: boolean,
    public hist: number[]
  ) {
  }
}