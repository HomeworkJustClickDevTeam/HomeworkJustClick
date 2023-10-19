import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const AdvancedEvaluationImageArea = ({image, chosenComment}:{image: HTMLImageElement, chosenComment: CommentInterface|undefined}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([
    {color:'red', id:0, description: "tutaj komentarz", leftTopXY:[0/1920, 208/514], rightTopXY:[160/1920, 208/514], leftBottomXY: [0/1920, 370/514], rightBottomXY: [160/1920, 370/514]}
  ])
  return <CanvasLogic image={image} commentsList={comments}></CanvasLogic>
}