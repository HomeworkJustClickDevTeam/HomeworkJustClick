import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useWindowSize } from "../customHooks/useWindowSize"

export const AdvancedEvaluationImageArea = ({image, chosenComment, setChosenComment}:{
  image: HTMLImageElement,
  chosenComment: CommentInterface|undefined,
  setChosenComment: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([])
  const windowSize = useWindowSize()


  return <CanvasLogic windowSize={windowSize} editable={true} setChosenComment={setChosenComment} chosenComment={chosenComment} setCommentsList={setComments} image={image} commentsList={comments}></CanvasLogic>
}