import React, {useState} from "react";
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


interface AssignmentAddCommentsPanelPropsInterface{
  comments: CommentInterface[] | CommentCreateInterface[]
  setComments: React.Dispatch<React.SetStateAction<CommentInterface[]|CommentCreateInterface[]>>
}
export const AssignmentAddCommentsPanel = ({setComments, comments}:AssignmentAddCommentsPanelPropsInterface) => {
  const [newCommentDescription, setNewCommentDescription] = useState("")
  const userState = useAppSelector(selectUserState)
  const handleCommentRemoval = async (deletedComment:CommentInterface) => {
    try {
      const response = await deleteCommentPostgresService(deletedComment.id.toString())
      if (response.data !== null && response.data !== undefined && response.status === 200) {
        const userNewComments = comments.filter(oldComment => oldComment.id !== deletedComment.id)
        setComments(userNewComments)
      }
    } catch (error) {
      console.log(error)
    }
  }
  const updateCommentsList = (changedComment: CommentInterface)=> {
    const newComments = comments.map(comment => {
      if (comment.id === changedComment?.id) return changedComment
      else return comment
    })
    setComments(newComments)
  }
  const handleNewCommentCreation = async (event:React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (newCommentDescription.length > 0) {
      if (userState?.id !== null && userState?.id !== undefined) {
        const newComment: CommentCreateInterface = {
          description: newCommentDescription,
          title: "",
          color: "#" + Math.floor(Math.random() * 16777215).toString(16), //random color
          userId: userState.id,
          assignmentId: -1
        }
      }
    }
  }
  return(<>
      <form onSubmit={}>

      </form>
      {comments.map((comment)=>{
        return <AdvancedEvaluationCommentPanelListElement key={comment.id} updateCommentsList={updateCommentsList} comment={comment} handleCommentRemoval={handleCommentRemoval}></AdvancedEvaluationCommentPanelListElement>
      })}
    </>
  )
}