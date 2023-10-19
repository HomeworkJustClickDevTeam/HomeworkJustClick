import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const AdvancedEvaluationImageArea = ({image, chosenComment}:{image: HTMLImageElement, chosenComment: CommentInterface|undefined}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([
    {color:'red', id:0, description: "tutaj komentarz", leftTopXY:[25/1321, 230/924], rightTopXY:[160/1321, 230/924], leftBottomXY: [25/1321, 370/924], rightBottomXY: [160/1321, 370/924]}
  ])


  return <CanvasLogic image={image} commentsList={comments}></CanvasLogic>
}