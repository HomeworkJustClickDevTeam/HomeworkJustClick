import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignmentPostgresService,
  createAssignmentWithUserAndGroupPostgresService, createListOfCommentsPostgresService
} from "../../services/postgresDatabaseServices"
import ReactDatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"
import { AssignmentAddFile } from "./AssignmentAddFile"
import { AssignmentToSendInterface } from "../../types/AssignmentToSendInterface"
import { selectGroup } from "../../redux/groupSlice"
import { useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import {AssignmentSettingsPage} from "./AssignmentSettingsPage";
import {useGetEvaluationTable} from "../customHooks/useGetEvaluationTable";
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";
import {CommentInterface} from "../../types/CommentInterface";
import {CommentCreateInterface} from "../../types/CommentCreateInterface";

function AssignmentAddSettingsPageWrapper() {
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)

  const [assignment, setAssignment] = useState<AssignmentToSendInterface>({
    title: "",
    completionDatetime: new Date(),
    taskDescription: "",
    visible: true,
    maxPoints: 1,
    autoPenalty: 50,
  })
  const [toSend, setToSend] = useState<boolean>(false)
  const [idAssignment, setIdAssignment] = useState<number>()
  const group = useSelector(selectGroup)
  const {evaluationTable} = useGetEvaluationTable(userState!.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] = useState<number>(-1)
  const [comments, setComments] = useState<CommentInterface[]>([])

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (userState) {
      createAssignmentWithUserAndGroupPostgresService(
        userState.id.toString(),
        group?.id as unknown as string,
        assignment
      )
        .then(async (response) => {
          if (response?.status === 200) {
            setIdAssignment(response.data.id)
            setToSend(true)
            if(chosenEvaluationTable !== -1)
              return await addEvaluationPanelToAssignmentPostgresService({assignmentId: response.data.id, evaluationPanelId: chosenEvaluationTable})

            if(comments.length > 0){
              const commentsWithAssignmentId :CommentCreateInterface[]= []
              comments.forEach(comment =>{
                commentsWithAssignmentId.push({...comment, assignmentId: response.data.id})
              })
              return await createListOfCommentsPostgresService(commentsWithAssignmentId)
            }
          }
        })
        .then(()=> navigate(`/group/${group!.id}/assignments`))
        .catch((error) => console.log(error))
    }
  }

  return (
    <AssignmentSettingsPage handleSubmit={handleSubmit}
                            assignment={assignment}
                            toSend={toSend}
                            setAssignment={setAssignment} evaluationTable={evaluationTable}
                            chosenEvaluationTable={chosenEvaluationTable}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            group={group}
                            newAssignmentId={idAssignment}/>
  )
}

export default AssignmentAddSettingsPageWrapper
