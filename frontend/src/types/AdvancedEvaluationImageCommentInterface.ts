import { CommentInterface } from "./CommentInterface"

export interface AdvancedEvaluationImageCommentInterface extends CommentInterface {
  leftTopXY: number[]
  width: number
  height: number
  lineWidth: number
}