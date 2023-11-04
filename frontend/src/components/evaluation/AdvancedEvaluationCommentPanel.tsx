import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { CommentCreateInterface } from "../../types/CommentCreateInterface"
import { createCommentWithUserPostgresService } from "../../services/postgresDatabaseServices"

export const AdvancedEvaluationCommentPanel = (
  {handleCommentClick, fileType, rightPanelUserComments, setRightPanelUserComments, handleCommentRemoval, height}:{
    handleCommentClick: (comment:CommentInterface, clickedCommentWidth?:number) => void,
    fileType:"txt"|"img",
    height:number|undefined,
    rightPanelUserComments: CommentInterface[],
    setRightPanelUserComments: (comments:CommentInterface[]) => void,
    handleCommentRemoval:(comment:CommentInterface)=>void}) => {

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
          color: "#" + Math.floor(Math.random()*16777215).toString(16), //random color
          userId:userState.id
        }
        createCommentWithUserPostgresService(newComment)
          .then((response) => {
            if(response.data !== undefined && response.data !== null && response.status === 201){
              const tempComments = rightPanelUserComments
              setRightPanelUserComments([{
                ...newComment,
                id:response.data.id,
                counter:response.data.counter,
                lastUsedDate: response.data.lastUsedDate},
                ...tempComments])
            }
          })
          .catch((error) => console.log(error))
      }
    }
  }


  return <div id={"commentPanel"} style={{float:"right", height:height !== undefined ? height.toString() : "100%", overflow:"scroll"}}>
    Dodaj nowy komentarz:<br/>
      <input onChange={(event) => setNewCommentDescription(event.target.value)}/>
      <button type={"button"} onClick={(event) => handleNewCommentCreation(event)}>Dodaj</button><br/>
    Panel komentarzy: <br/>
    {rightPanelUserComments.map((comment) => {
      return(<AdvancedEvaluationCommentPanelListElement
        fileType={fileType}
        handleCommentRemoval={handleCommentRemoval}
        comment={comment}
        handleCommentClick={handleCommentClick}
        key={comment.id}></AdvancedEvaluationCommentPanelListElement>)
    })}
    </div>
}