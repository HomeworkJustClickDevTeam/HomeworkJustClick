import {UserInterface} from "./UserInterface";
import {SolutionInterface} from "./SolutionInterface";
import {GroupInterface} from "./GroupInterface";

export class EvaluationCreateModel {
  constructor(
    public result: number,
    public userId: number,
    public solutionId: number,
    public groupId:number,
    public grade:number,
    public reported: boolean
  ) {}
}