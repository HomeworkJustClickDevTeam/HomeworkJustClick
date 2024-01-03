import {UserInterface} from "./UserInterface";
import {GroupInterface} from "./GroupInterface";
import {EvaluationModel} from "./EvaluationExtendedModel";

export class AssignmentWithEvaluationModel{
  constructor(
    public id: number,
    public title: string,
    public visible: boolean,
    public user: UserInterface,
    public group: GroupInterface,
    public taskDescription: string,
    public creationDatetime: Date,
    public lastModifiedDatetime: Date,
    public completionDatetime: Date,
    public maxPoints: number,
    public autoPenalty: number,
    public evaluation: EvaluationModel|null
  ) {
  }

}