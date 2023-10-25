import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const AdvancedEvaluationImageArea = ({image, chosenComment, setChosenComment}:{
  image: HTMLImageElement,
  chosenComment: CommentInterface|undefined,
  setChosenComment: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([])
  return <CanvasLogic editable={true} setChosenComment={setChosenComment} chosenComment={chosenComment} setCommentsList={setComments} image={image} commentsList={comments}></CanvasLogic>
}