import { CommentInterface } from "./CommentInterface"
import { AdvancedEvaluationImageCommentCreateInterface } from "./AdvancedEvaluationImageCommentCreateInterface"
import {FileInterface} from "./FileInterface";

export class AdvancedEvaluationImageCommentModel {
  constructor(
    public id: number,
    public leftTopX: number,
    public leftTopY: number,
    public width: number,
    public height: number,
    public imgHeight: number,
    public imgWidth:number,
    public color:string,
    public comment:CommentInterface,
    public file:FileInterface,
  ) {
  }
}