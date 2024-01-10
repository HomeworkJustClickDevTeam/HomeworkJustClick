import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignmentPostgresService,
  createAssignmentWithUserAndGroupPostgresService,
  createFileWithAssignmentPostgresService,
  createListOfCommentsPostgresService
} from "../../services/postgresDatabaseServices"
import ReactDatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"
import { AssignmentCreateModel } from "../../types/AssignmentCreate.model"
import { selectGroup } from "../../redux/groupSlice"
import { useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import {AssignmentSettingsPage} from "./AssignmentSettingsPage";
import {useGetEvaluationTable} from "../customHooks/useGetEvaluationTable";
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";
import {CommentInterface} from "../../types/CommentInterface";
import {CommentCreateInterface} from "../../types/CommentCreateInterface";
import {toast} from "react-toastify";
import {postFileMongoService} from "../../services/mongoDatabaseServices";


function AssignmentAddSettingsPageWrapper() {
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)

  const [assignment, setAssignment] = useState<AssignmentCreateModel>(new AssignmentCreateModel(
    "",
    true,
    "",
    new Date(),
    1,
    50,
    false
  ))
  const [idAssignment, setIdAssignment] = useState<number>()
  const group = useSelector(selectGroup)
  const {evaluationTable} = useGetEvaluationTable(userState!.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] = useState<number>(-1)
  const [newFile, setNewFile] = useState<File|undefined>(undefined)
  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    try {
      let response = null
      let newAssignmentResponse = await createAssignmentWithUserAndGroupPostgresService(
        userState!.id.toString(),
        group!.id.toString(),
        assignment
      )
      if(newAssignmentResponse?.status !== 200){
        toast.error("Błąd, podczas tworzenia zadania.")
        return
      }
      setIdAssignment(newAssignmentResponse.data.id)
      if(chosenEvaluationTable !== -1){
        response = await addEvaluationPanelToAssignmentPostgresService({assignmentId: newAssignmentResponse.data.id, evaluationPanelId: chosenEvaluationTable})
        if(response?.status !== 201){
          toast.error("Nie przypisano tabelę oceniania do zadania.")
          return
        }
      }
      if(newFile === undefined) {
        toast.success("Pomyślnie dodano zadanie.")
        navigate(`/group/${group!.id}/assignments`)
        return
      }
      const formData = new FormData()
      formData.append('file', newFile)
      let responseMongo = await postFileMongoService(formData)
      if(responseMongo?.status === 413){
        toast.error("Twój plik jest za duży")
        return
      }
      if(responseMongo?.status !== 200) return
      response = await createFileWithAssignmentPostgresService(
        `${responseMongo.data.id}`,
        responseMongo.data.format,
        responseMongo.data.name,
        newAssignmentResponse.data.id.toString())
      if(response?.status === 200)
        toast.success("Pomyślnie dodano zadanie.")
      else toast.error("Błąd podczas zmiany pliku.")
      navigate(`/group/${group!.id}/assignments`)
    }
    catch (e){
      toast.error("Błąd podczas tworzenia zadania.")
      console.error(e)}
  }

  return (
    <AssignmentSettingsPage handleSubmit={handleSubmit}
                            assignment={assignment}
                            setNewFile={setNewFile}
                            setAssignment={setAssignment}
                            evaluationTable={evaluationTable}
                            chosenEvaluationTable={chosenEvaluationTable}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            newAssignmentId={idAssignment}/>
  )
}

export default AssignmentAddSettingsPageWrapper