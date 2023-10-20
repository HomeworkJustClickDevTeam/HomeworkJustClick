import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const AdvancedEvaluationImageArea = ({image, chosenComment}:{image: HTMLImageElement, chosenComment: CommentInterface|undefined}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([
    {color:'red', id:0, description: "tutaj komentarz", leftTopXY:[5,5], width:100, height:150, lineWidth: 3},
    {color:'CornflowerBlue', id:1, description: "tutaj inny komentraz", leftTopXY:[10,20], width:50, height:150, lineWidth: 8}
  ])
  return <CanvasLogic chosenComment={chosenComment} setCommentsList={setComments} image={image} commentsList={comments}></CanvasLogic>
}