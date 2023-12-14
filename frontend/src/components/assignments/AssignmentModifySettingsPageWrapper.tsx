import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignmentPostgresService,
  changeAssignmentPostgresService,
  changeCommentPostgresService,
  changeEvaluationPanelAssignmentPostgresService,
  createCommentWithUserPostgresService,
  deleteAssignmentPostgresService,
  deleteCommentPostgresService,
  deleteEvaluationPanelAssignmentPostgresService,
} from "../../services/postgresDatabaseServices"
import ReactDatePicker from "react-datepicker"

import { AssignmentModifyFile } from "./AssignmentModifyFile"
import { AssignmentPropsInterface } from "../../types/AssignmentPropsInterface"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import {useGetEvaluationTable} from "../customHooks/useGetEvaluationTable";
import {selectUserState} from "../../redux/userStateSlice";
import {Table} from "../../types/Table.model";
import {ta} from "date-fns/locale";
import {AssignmentSettingsPage} from "./AssignmentSettingsPage";
import {useGetEvaluationPanelAssignment} from "../customHooks/useGetEvaluationPanelAssignment";
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";
import {CommentInterface} from "../../types/CommentInterface";
import {CommentCreateInterface} from "../../types/CommentCreateInterface";
import {parseISO} from "date-fns";

interface AssignmentModifyPropsInterface extends AssignmentPropsInterface {
  setAssignment: (assignment: (prevState: any) => any) => void
}

function AssignmentModifySettingsPageWrapper({
  assignment,
  setAssignment,
}: AssignmentModifyPropsInterface) {
  const navigate = useNavigate()
  const [toSend, setToSend] = useState<boolean>(false)
  const group = useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const {evaluationTable} = useGetEvaluationTable(userState?.id)
  const {evaluationPanelAssignment} = useGetEvaluationPanelAssignment(assignment.id, userState!.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] =
    useState<number>(evaluationPanelAssignment === undefined ? -1 : evaluationPanelAssignment.evaluationPanelId)

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    changeAssignmentPostgresService(assignment)
      .then(() => {
        setToSend(true)
        if(chosenEvaluationTable !== -1 && evaluationPanelAssignment === undefined)
          return addEvaluationPanelToAssignmentPostgresService({assignmentId: assignment.id, evaluationPanelId: chosenEvaluationTable})
        else if(chosenEvaluationTable !== -1 && evaluationPanelAssignment !== undefined && chosenEvaluationTable !== evaluationPanelAssignment.evaluationPanelId)
          return changeEvaluationPanelAssignmentPostgresService({evaluationPanelId:evaluationPanelAssignment.id, assignmentId:assignment.id, id:evaluationPanelAssignment.id})
        else if(chosenEvaluationTable === -1 && evaluationPanelAssignment !== undefined)
          return deleteEvaluationPanelAssignmentPostgresService(evaluationPanelAssignment.id.toString())
      })
      .then(()=> navigate(-1))
      .catch((error) => console.log(error))
  }

  function handleDelete(event: React.FormEvent) {
    deleteAssignmentPostgresService(assignment.id.toString())
      .then(() => navigate(`/group/${group?.id}/assignments/`))
      .catch((error) => console.log(error))
  }

  useEffect(() => {
    setChosenEvaluationTable(evaluationPanelAssignment === undefined ? -1 : evaluationPanelAssignment.evaluationPanelId)
  }, [evaluationPanelAssignment]);

  return (
    <AssignmentSettingsPage handleSubmit={handleSubmit}
                            assignment={assignment}
                            chosenEvaluationTable={chosenEvaluationTable}
                            toSend={toSend}
                            handleDelete={handleDelete}
                            setAssignment={setAssignment}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            evaluationTable={evaluationTable}
                            group={group}/>
  )
}

export default AssignmentModifySettingsPageWrapper
