import { CommentInterface } from "./CommentInterface"

export interface AdvancedEvaluationImageCommentInterface extends CommentInterface {
  leftTopXY: number[]
  leftBottomXY: number[]
  rightTopXY: number[]
  rightBottomXY: number[]
}