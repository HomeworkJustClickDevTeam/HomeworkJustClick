import React, {SetStateAction, useState} from "react";
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";
import {useAppDispatch, useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {AdvancedEvaluationCommentPanelListElement} from "../evaluation/AdvancedEvaluationCommentPanelListElement";
import {CommentInterface} from "../../types/CommentInterface";
import {
  changeCommentPostgresService,
  createCommentWithUserPostgresService,
  deleteCommentPostgresService
} from "../../services/postgresDatabaseServices";
import {CommentCreateInterface} from "../../types/CommentCreateInterface";
import {parseISO} from "date-fns";
import {AssignmentCommentPanelForm} from "./AssignmentCommentPanelForm";


interface AssignmentModifyCommentsPanelPropsInterface{
  comments: CommentInterface[]
  assignmentId: number
  setComments: React.Dispatch<SetStateAction<CommentInterface[]>>
}
export const AssignmentModifyCommentsPanel = ({assignmentId, setComments, comments}:AssignmentModifyCommentsPanelPropsInterface) => {
  const [newCommentDescription, setNewCommentDescription] = useState("")
  const userState = useAppSelector(selectUserState)
  const handleCommentRemoval = async (deletedCommentId:number) => {
    try {
      const response = await deleteCommentPostgresService(deletedCommentId.toString())
      if (response.data !== null && response.data !== undefined && response.status === 200) {
        const userNewComments = comments.filter(oldComment => oldComment.id !== deletedCommentId)
        setComments(userNewComments)
      }
    } catch (error) {
      console.log(error)
    }
  }
  const updateCommentsList = async (changedComment:CommentInterface) => {
    try {
      const response = await changeCommentPostgresService(changedComment)
      if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
        const newComments = comments.map(comment => {
          if (comment.id === changedComment?.id) return changedComment
          else return comment
        })
        setComments(newComments)

        //await zeby aktualizowalo narysowane komentarze
      }
    }catch (error){
      console.log(error)
    }
  }
  const handleNewCommentCreation = async (event:React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription.length > 0)
    {
      if(userState?.id !== null && userState?.id !== undefined){
        const newComment:CommentCreateInterface = {
          description: newCommentDescription,
          title:"",
          color: "#" + Math.floor(Math.random()*16777215).toString(16), //random color
          userId:userState.id,
          assignmentId:assignmentId
        }
        createCommentWithUserPostgresService(newComment)
          .then((response) => {
            if(response.data !== undefined && response.data !== null && response.status === (201 || 200)){
              const tempComments = comments
              setComments([{
                ...newComment,
                id:response.data.id,
                counter:response.data.counter,
                lastUsedDate: parseISO(response.data.lastUsedDate)},
                ...tempComments])
              setNewCommentDescription("")
            }
          })
          .catch((error) => console.log(error))
      }
    }
  }

  return(<>
      <AssignmentCommentPanelForm handleNewCommentCreation={handleNewCommentCreation} newCommentDescription={newCommentDescription} setNewCommentDescription={setNewCommentDescription}/>
      {comments.map((comment)=>{
        return <AdvancedEvaluationCommentPanelListElement key={comment.id} commentId={comment.id} updateCommentsList={updateCommentsList} comment={comment} handleCommentRemoval={handleCommentRemoval}></AdvancedEvaluationCommentPanelListElement>
      })}
    </>
  )
}