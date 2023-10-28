import { CanvasLogic } from "./CanvasLogic"
import { CommentInterface } from "../../types/CommentInterface"
import { MutableRefObject, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useWindowSize } from "../customHooks/useWindowSize"

export const AdvancedEvaluationImageArea = ({image, chosenComment, setChosenComment, commentPanelRef, chosenCommentFrameWidth}:{
  image: HTMLImageElement,
  chosenComment: CommentInterface|undefined,
  setChosenComment: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void,
  commentPanelRef: MutableRefObject<HTMLDivElement|null>,
  chosenCommentFrameWidth:number|undefined}) =>{
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([])

  if(commentPanelRef.current !== null && commentPanelRef.current !== undefined){
    return <CanvasLogic chosenCommentFrameWidth={chosenCommentFrameWidth} commentPanelWidth={commentPanelRef.current.clientWidth} editable={true} setChosenComment={setChosenComment} chosenComment={chosenComment} setCommentsList={setComments} image={image} commentsList={comments}></CanvasLogic>
  }
  else {
    return <></>
  }
}