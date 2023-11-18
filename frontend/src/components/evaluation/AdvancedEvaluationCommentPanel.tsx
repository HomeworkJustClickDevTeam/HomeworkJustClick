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

  return <div ref={commentPanelRef} id={"commentPanel"} className='float-right h-full'>
      <p className='text-center underline underline-offset-4 mb-4'>KOMENTARZE</p>
        Dodaj nowy komentarz:<br/>
      <div className='inline-flex w-full pb-4'>
          <input className='border border-black rounded-sm mr-2 w-full pl-1.5' onChange={(event) => {setNewCommentDescription(event.target.value) }}/>
      <button type={"button"} className='w-24 ml-auto mr-2 bg-main_blue text-white px-2 py-1 rounded-md' onClick={(event) => {handleNewCommentCreation(event);
      }}>Dodaj +</button></div><br/><hr/>
      <div>
          <p className='pt-2'>Wybierz komentarz: </p>
            {rightPanelComments.map((comment) => {
        return(<AdvancedEvaluationCommentPanelListElement
        handleCommentRemoval={handleCommentRemoval}
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
        })}
    </div>
    </div>
}