import {UserInterface} from "./UserInterface";
import {SolutionInterface} from "./SolutionInterface";
import {GroupInterface} from "./GroupInterface";

export class EvaluationModel {
  constructor(
    public id: number,
    public result: number,
    public user: UserInterface,
    public solution: SolutionInterface,
    public group:GroupInterface,
    public creationDateTime: Date,
    public lastModifiedDateTime: Date,
    public grade:number,
    public reported: boolean
  ) {}
}