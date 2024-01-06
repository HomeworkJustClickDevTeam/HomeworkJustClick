export class AssignmentModel {
  constructor(
    public id: number,
    public title: string,
    public visible: boolean,
    public userId: number,
    public groupId: number,
    public taskDescription: string,
    public creationDatetime: Date,
    public lastModifiedDatetime: Date,
    public completionDatetime: Date,
    public maxPoints: number,
    public autoPenalty: number,
    public advancedEvaluation: boolean
  ) {
  }
}