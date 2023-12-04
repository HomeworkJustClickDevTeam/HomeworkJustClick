import { AdvancedEvaluationTextCommentCreateInterface } from "./AdvancedEvaluationTextCommentCreateInterface"
import {CommentInterface} from "./CommentInterface";
import {FileInterface} from "./FileInterface";

export class AdvancedEvaluationTextCommentModel {
  constructor(
    public id: number,
    public highlightStart: number,
    public highlightEnd: number,
    public color:string,
    public comment: CommentInterface,
    public file: FileInterface
  ) {
  }
}