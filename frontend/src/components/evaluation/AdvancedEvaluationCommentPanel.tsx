import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"

export const AdvancedEvaluationCommentPanel = (
  {setChosenComment, chosenComment, commentPanelRef, setChosenCommentFrameWidth}:{
    setChosenComment:(comment:CommentInterface|undefined) => void,
    chosenComment:CommentInterface|undefined,
    commentPanelRef: MutableRefObject<HTMLDivElement|null>,
    setChosenCommentFrameWidth:(commentWidth:number|undefined) => void}) => {

  const [newCommentDescription, setNewCommentDescription] = useState<string|undefined>(undefined)
  const [rightPanelComments, setRightPanelComments] = useState<CommentInterface[]>([
    {color:'#ff0000', id:0, description: "tutaj komentarz"},
    {color:'#0068ff', id:1, description: "tutaj inny komentraz"},
    {color:'#fffb00', id:2, description: "ostatni komentarz"}])

  const handleNewCommentCreation = (event:React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription !== undefined)
    {
      const newId = Math.max(...rightPanelComments.map(comment => comment.id)) + 1
      const newComment:CommentInterface = {
        description: newCommentDescription,
        id: newId,
        color: '#fffb00'
      }
      setRightPanelComments((prevState)=>([newComment, ...prevState]))
    }

  }
  const handleCommentRemoval = (commentId:number) => {
    const newComments = rightPanelComments.filter(comment => comment.id !== commentId)
    setRightPanelComments(newComments)
  }
  const handleCommentClick = (clickedComment:CommentInterface, clickedCommentWidth:number) => {
      setChosenComment(clickedComment)
      setChosenCommentFrameWidth(clickedCommentWidth)
      const newComments = rightPanelComments.map(comment => {
        if(comment.id === clickedComment?.id) return clickedComment
        else return comment
      })
      setRightPanelComments(newComments)
  }

  return <div ref={commentPanelRef} id={"commentPanel"} style={{float:"right", height:"100vh"}}>
    Dodaj nowy komentarz:<br/>
      <input onChange={(event) => setNewCommentDescription(event.target.value)}/>
      <button type={"button"} onClick={(event) => handleNewCommentCreation(event)}>Dodaj</button><br/>
    Panel komentarzy: <br/>
    {rightPanelComments.map((comment) => {
      return(<AdvancedEvaluationCommentPanelListElement
        handleCommentRemoval={handleCommentRemoval}
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
    })}
    </div>
}