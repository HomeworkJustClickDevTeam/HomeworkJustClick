import {UserInterface} from "./UserInterface";
import {SolutionInterface} from "./SolutionInterface";
import {GroupInterface} from "./GroupInterface";

export class EvaluationExtendedModel {
  constructor(
    public id: number,
    public result: number,
    public user: UserInterface,
    public solution: SolutionInterface,
    public group:GroupInterface,
    public creationDateTime: Date,
    public lastModifiedDateTime: Date,
    public grade:number,
    public reported: boolean,
    public comment: string
  ) {}
}
export class EvaluationModel {
  constructor(
    public id: number,
    public result: number,
    public userId: number,
    public solutionId: number,
    public groupId:number,
    public creationDateTime: Date,
    public lastModifiedDateTime: Date,
    public grade:number,
    public comment: string
  ) {}
}