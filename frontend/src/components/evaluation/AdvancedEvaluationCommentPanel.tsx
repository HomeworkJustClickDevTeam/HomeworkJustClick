import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { CommentCreateInterface } from "../../types/CommentCreateInterface"
import { createCommentWithUserPostgresService } from "../../services/postgresDatabaseServices"

export const AdvancedEvaluationCommentPanel = (
  {setChosenComment, setChosenCommentFrameWidth, rightPanelUserComments, setRightPanelUserComments, handleCommentImageRemoval, height}:{
    height:number|undefined
    setChosenComment:(comment:CommentInterface|undefined) => void,
    setChosenCommentFrameWidth:(commentWidth:number|undefined) => void,
    rightPanelUserComments: CommentInterface[],
    setRightPanelUserComments: (comments:CommentInterface[]) => void,
    handleCommentImageRemoval:(comment:CommentInterface)=>void}) => {

  const [newCommentDescription, setNewCommentDescription] = useState<string|undefined>(undefined)
  const userState = useAppSelector(selectUserState)

  const handleNewCommentCreation = async (event:React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription !== undefined)
    {
      if(userState?.id !== null && userState?.id !== undefined){
        const newComment:CommentCreateInterface = {
          description: newCommentDescription,
          title:"",
          defaultColor: '#fffb00',
          userId:userState.id
        }
        createCommentWithUserPostgresService(newComment)
          .then((response) => {
            if(response.data !== undefined && response.data !== null){
              const tempComments = rightPanelUserComments
              setRightPanelUserComments([response.data as CommentInterface, ...tempComments])
            }
          })
          .catch((error) => console.log(error))
      }
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
        handleCommentRemoval={handleCommentImageRemoval}
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
    })}
    </div>
}