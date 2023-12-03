import React, {SetStateAction, useState} from "react";
import {AssignmentCommentPanelForm} from "./AssignmentCommentPanelForm";
import {AdvancedEvaluationCommentPanelListElement} from "../evaluation/AdvancedEvaluationCommentPanelListElement";
import {CommentInterface} from "../../types/CommentInterface";
import {CommentCreateInterface} from "../../types/CommentCreateInterface";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";


interface AssignmentAddCommentsPanelPropsInterface{
  comments: CommentInterface[]
  setComments: React.Dispatch<SetStateAction<CommentInterface[]>>
}
export const AssignmentAddCommentsPanel = ({comments, setComments}:AssignmentAddCommentsPanelPropsInterface) => {
  const [newCommentDescription, setNewCommentDescription] = useState("")
  const userState = useAppSelector(selectUserState)

  const handleCommentRemoval = (deletedCommentId:number) =>{
    setComments(prevState => (prevState.filter((_, index)=>index !== deletedCommentId)))
  }
  const updateCommentsList = (changedComment: CommentInterface) =>{
    setComments(prevState => (prevState.map((oldComment) => {
      if(oldComment.id === changedComment.id) return changedComment
      else return oldComment
    })))
  }
  const handleNewCommentCreation = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription.length > 0) {
      if (userState?.id !== null && userState?.id !== undefined) {
        const newComment: CommentInterface = {
          description: newCommentDescription,
          title: "",
          color: "#" + Math.floor(Math.random() * 16777215).toString(16), //random color
          userId: userState.id,
          assignmentId: -1,
          lastUsedDate: new Date(),
          counter:0,
          id:0
        }//dummy values (assignmentId, counter, id, lastUsedDate) will not affect the db insertion
        setComments(prevState => ([...prevState, newComment]))
        setNewCommentDescription("")
      }
    }
  }
  return(<div>
    <AssignmentCommentPanelForm handleNewCommentCreation={handleNewCommentCreation} newCommentDescription={newCommentDescription} setNewCommentDescription={setNewCommentDescription}/>
    {comments.map((comment, index)=>{
      return <AdvancedEvaluationCommentPanelListElement key={index} commentAlreadyInDb={false} commentId={index} updateCommentsList={updateCommentsList} comment={comment} handleCommentRemoval={handleCommentRemoval}></AdvancedEvaluationCommentPanelListElement>
    })}
  </div>)
}