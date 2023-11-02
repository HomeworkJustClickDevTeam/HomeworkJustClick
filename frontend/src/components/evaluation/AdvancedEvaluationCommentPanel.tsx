import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"

export const AdvancedEvaluationCommentPanel = (
  {setChosenComment, setChosenCommentFrameWidth, rightPanelUserComments, setRightPanelUserComments, handleCommentRemoval, height}:{
    height:number|undefined
    setChosenComment:(comment:CommentInterface|undefined) => void,
    setChosenCommentFrameWidth:(commentWidth:number|undefined) => void,
    rightPanelUserComments: CommentInterface[],
    setRightPanelUserComments: (comments:CommentInterface[]) => void,
    handleCommentRemoval:(commentId:number)=>void}) => {

  const [newCommentDescription, setNewCommentDescription] = useState<string|undefined>(undefined)


  const handleNewCommentCreation = (event:React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription !== undefined)
    {
      const newId = Math.max(...rightPanelUserComments.map(comment => comment.id)) + 1
      const newComment:CommentInterface = {
        description: newCommentDescription,
        id: newId,
        color: '#fffb00'
      }
      const tempComments = [newComment, ...rightPanelUserComments]
      setRightPanelUserComments(tempComments)
    }

  }

  const handleCommentClick = (clickedComment:CommentInterface, clickedCommentWidth:number) => {
      setChosenComment(clickedComment)
      setChosenCommentFrameWidth(clickedCommentWidth)
      const newComments = rightPanelUserComments.map(comment => {
        if(comment.id === clickedComment?.id) return clickedComment
        else return comment
      })
    setRightPanelUserComments(newComments)
  }

  return <div id={"commentPanel"} style={{float:"right", height:height !== undefined ? height.toString() : "100%", overflow:"scroll"}}>
    Dodaj nowy komentarz:<br/>
      <input onChange={(event) => setNewCommentDescription(event.target.value)}/>
      <button type={"button"} onClick={(event) => handleNewCommentCreation(event)}>Dodaj</button><br/>
    Panel komentarzy: <br/>
    {rightPanelUserComments.map((comment) => {
      return(<AdvancedEvaluationCommentPanelListElement
        handleCommentRemoval={handleCommentRemoval}
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
    })}
    </div>
}