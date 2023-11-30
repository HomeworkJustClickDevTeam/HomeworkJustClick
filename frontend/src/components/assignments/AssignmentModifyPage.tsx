import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignment,
  changeAssignmentPostgresService,
  deleteAssignmentPostgresService,
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

interface AssignmentModifyPropsInterface extends AssignmentPropsInterface {
  setAssignment: (assignment: (prevState: any) => any) => void
}

function AssignmentModifyPage({
  assignment,
  setAssignment,
}: AssignmentModifyPropsInterface) {
  const navigate = useNavigate()
  const [toSend, setToSend] = useState<boolean>(false)
  const group = useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const {evaluationTable} = useGetEvaluationTable(userState?.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] = useState<number>(-1)

  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }
  const handleNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleCheckboxChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = event.target
    setAssignment((prevState) => ({
      ...prevState,
      [name]: checked,
    }))
  }

  const handleDateChange = (date: Date) => {
    setAssignment((prevState) => ({
      ...prevState,
      completionDatetime: date,
    }))
  }

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    changeAssignmentPostgresService(assignment)
      .then(() => {
        setToSend(true)
        if(chosenEvaluationTable !== undefined)
          return addEvaluationPanelToAssignment({assignmentId: assignment.id, evaluationId: chosenEvaluationTable})
      })
      .catch((error) => console.log(error))
  }

  function handleDelete(event: React.FormEvent) {
    deleteAssignmentPostgresService(assignment.id.toString())
      .then(() => navigate(`/group/${group?.id}/assignments/`))
      .catch((error) => console.log(error))
  }

  return (
    <AssignmentSettingsPage handleSubmit={handleSubmit}
                            handleTextChange={handleTextChange}
                            handleNumberChange={handleNumberChange}
                            assignment={assignment}
                            chosenEvaluationTable={chosenEvaluationTable}
                            handleDateChange={handleDateChange}
                            handleCheckboxChange={handleCheckboxChange}
                            toSend={toSend}
                            handleDelete={handleDelete}
                            setAssignment={setAssignment}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            evaluationTable={evaluationTable}
                            group={group}/>
  )
}

export default AssignmentModifyPage
