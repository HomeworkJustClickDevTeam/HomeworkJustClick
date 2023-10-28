import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"

export const AdvancedEvaluationCommentPanel = (
  {setChosenComment, chosenComment, commentPanelRef, setChosenCommentFrameWidth}:{
    setChosenComment:(comment:CommentInterface|undefined) => void,
    chosenComment:CommentInterface|undefined,
    commentPanelRef: MutableRefObject<HTMLDivElement|null>,
    setChosenCommentFrameWidth:(commentWidth:number|undefined) => void}) => {

  const handleCommentClick = (clickedComment:CommentInterface, clickedCommentWidth:number) => {
    if(chosenComment?.id === clickedComment.id){
      setChosenComment(undefined)
      setChosenCommentFrameWidth(undefined)
    }
    else{
      setChosenComment(clickedComment)
      setChosenCommentFrameWidth(clickedCommentWidth)
    }
  }
  const [comments] = useState<CommentInterface[]>([
    {color:'#ff0000', id:0, description: "tutaj komentarz"},
    {color:'#0068ff', id:1, description: "tutaj inny komentraz"},
    {color:'#fffb00', id:2, description: "ostatni komentarz"}])
  return <div ref={commentPanelRef} id={"commentPanel"} style={{float:"right", height:"100vh"}}>
    Panel komentarzy: <br/>
    {comments.map((comment) => {
      return(<AdvancedEvaluationCommentPanelListElement
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
    })}
    </div>
}