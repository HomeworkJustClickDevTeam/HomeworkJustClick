import { CommentInterface } from "./CommentInterface"

export interface AdvancedEvaluationCommentInterface extends CommentInterface{
  highlightStart:number
  highlightEnd: number
}