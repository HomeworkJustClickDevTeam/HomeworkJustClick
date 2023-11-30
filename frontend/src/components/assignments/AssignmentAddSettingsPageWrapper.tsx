import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignmentPostgresService,
  createAssignmentWithUserAndGroupPostgresService
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

function AssignmentAddSettingsPageWrapper() {
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)

  const [assignment, setAssignment] = useState<AssignmentToSendInterface>({
    title: "",
    completionDatetime: new Date(),
    taskDescription: "",
    visible: true,
    max_points: 1,
    auto_penalty: 50,
  })
  const [toSend, setToSend] = useState<boolean>(false)
  const [idAssignment, setIdAssignment] = useState<number>()
  const group = useSelector(selectGroup)
  const {evaluationTable} = useGetEvaluationTable(userState!.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] = useState<number>(-1)

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    if (userState) {
      createAssignmentWithUserAndGroupPostgresService(
        userState.id.toString(),
        group?.id as unknown as string,
        assignment
      )
        .then((response) => {
          if (response !== undefined) {
            setIdAssignment(response.data.id)
            setToSend(true)
            if(chosenEvaluationTable !== -1)
              return addEvaluationPanelToAssignmentPostgresService({assignmentId: response.data.id, evaluationPanelId: chosenEvaluationTable})
          }
        })
        .then(()=> navigate(-1))
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
