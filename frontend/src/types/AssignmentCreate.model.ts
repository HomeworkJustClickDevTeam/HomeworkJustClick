export class AssignmentCreateModel {
  constructor(
    public title: string,
    public visible: boolean,
    public taskDescription: string,
    public completionDatetime: Date,
    public maxPoints: number,
    public autoPenalty: number
  ) {
  }
}