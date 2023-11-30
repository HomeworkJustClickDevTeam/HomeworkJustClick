import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import { createAssignmentWithUserAndGroupPostgresService } from "../../services/postgresDatabaseServices"
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

function AssignmentAddPage() {
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
      [name]: parseInt(value),
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
    if (userState) {
      createAssignmentWithUserAndGroupPostgresService(
        userState.id.toString(),
        group?.id as unknown as string,
        assignment
      )
        .catch((error) => console.log(error))
        .then((response) => {
          if (response !== undefined) {
            setIdAssignment(response.data.id)
            setToSend(true)
            navigate(-1)
          }
        })
    }
  }

  return (
    <AssignmentSettingsPage handleSubmit={handleSubmit}
                            handleTextChange={handleTextChange}
                            handleNumberChange={handleNumberChange}
                            assignment={assignment}
                            handleDateChange={handleDateChange}
                            handleCheckboxChange={handleCheckboxChange}
                            toSend={toSend}
                            setAssignment={setAssignment} evaluationTable={evaluationTable}
                            chosenEvaluationTable={chosenEvaluationTable}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            group={group}
                            newAssignmentId={idAssignment}/>
  )
}

export default AssignmentAddPage
