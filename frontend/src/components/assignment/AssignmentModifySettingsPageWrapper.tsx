import { useNavigate } from "react-router-dom"
import React, { ChangeEvent, useEffect, useState } from "react"
import {
  addEvaluationPanelToAssignmentPostgresService,
  changeAssignmentPostgresService,
  changeCommentPostgresService,
  changeEvaluationPanelAssignmentPostgresService,
  createCommentWithUserPostgresService, createFileWithAssignmentPostgresService,
  deleteAssignmentPostgresService,
  deleteCommentPostgresService,
  deleteEvaluationPanelAssignmentPostgresService, deleteFilePostgresService,
} from "../../services/postgresDatabaseServices"
import ReactDatePicker from "react-datepicker"

import { AssignmentFile } from "./AssignmentAddOrModifyFile"
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
import {useGetFile} from "../customHooks/useGetFile";
import {deleteFileMongoService, postFileMongoService} from "../../services/mongoDatabaseServices";
import {toast} from "react-toastify";

interface AssignmentModifyPropsInterface extends AssignmentPropsInterface {
  setAssignment: (assignment: (prevState: any) => any) => void
}

function AssignmentModifySettingsPageWrapper({
  assignment,
  setAssignment,
}: AssignmentModifyPropsInterface) {
  const navigate = useNavigate()
  const [newFile, setNewFile] = useState<File|undefined>(undefined)
  const group = useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const {evaluationTable} = useGetEvaluationTable(userState?.id)
  const {evaluationPanelAssignment} = useGetEvaluationPanelAssignment(assignment.id, userState!.id)
  const [chosenEvaluationTable, setChosenEvaluationTable] =
    useState<number>(evaluationPanelAssignment === undefined ? -1 : evaluationPanelAssignment.evaluationPanelId)
  const databaseFile = useGetFile(assignment.id, 'assignment')

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()
    try{
      let response = await changeAssignmentPostgresService(assignment)
      if(response?.status !== 200) {
        toast.error("Nie udało sie zmienić zadania.")
        return
      }
      if(chosenEvaluationTable !== -1 && evaluationPanelAssignment === undefined)
      {
        let evaluationTabResponse = await addEvaluationPanelToAssignmentPostgresService({assignmentId: assignment.id, evaluationPanelId: chosenEvaluationTable})
        if(evaluationTabResponse?.status !== 201) {
          toast.error("Nie udało sie przypisać tabeli oceniania do zadania.")
          return
        }
      }
      else if(chosenEvaluationTable !== -1 && evaluationPanelAssignment !== undefined && chosenEvaluationTable !== evaluationPanelAssignment.evaluationPanelId){
        let evaluationTabResponse = await changeEvaluationPanelAssignmentPostgresService({evaluationPanelId:evaluationPanelAssignment.id, assignmentId:assignment.id, id:evaluationPanelAssignment.id})
        if(evaluationTabResponse?.status !== 200) {
          toast.error("Nie udało sie przypisać tabeli oceniania do zadania.")
          return
        }
      }
      else if(chosenEvaluationTable === -1 && evaluationPanelAssignment !== undefined){
        let evaluationTabResponse = await deleteEvaluationPanelAssignmentPostgresService(evaluationPanelAssignment.id.toString())
        if(evaluationTabResponse?.status !== 200) {
          toast.error("Nie udało sie przypisać tabeli oceniania do zadania.")
          return
        }
      }
      if((newFile !== undefined)){
        if(databaseFile!== undefined){
          response = await deleteFilePostgresService(databaseFile!.id.toString())
          if(response?.status !== 200) return
          response = await deleteFileMongoService(databaseFile!.mongoId)
          if(response?.status !== 200) {
            await createFileWithAssignmentPostgresService(
              databaseFile.mongoId,
              databaseFile.format,
              databaseFile.name,
              assignment.id.toString()
            )
            toast.error("Błąd podczas zmiany pliku.")
            return
          }
        }
        const formData = new FormData()
        formData.append('file', newFile)
        response = await postFileMongoService(formData)
        if(response?.status === 413){
          toast.error("Twój plik jest za duży")
          return
        }
        if(response?.status !== 200) return
        response = await createFileWithAssignmentPostgresService(
          `${response.data.id}`,
          response.data.format,
          response.data.name,
          assignment.id.toString())
        if(response?.status !== 200)
          toast.error("Błąd podczas zmiany pliku.")
      }
      navigate(`/group/${group?.id}/assignments/`)
      toast.success("Pomyślnie zaktualizowano zadanie.")
    }
    catch (e){
      toast.error("Błąd podczas zmiany zadania.")
      console.error(e)
    }
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
                            setNewFile={setNewFile}
                            handleDelete={handleDelete}
                            setAssignment={setAssignment}
                            databaseFile={databaseFile}
                            setChosenEvaluationTable={setChosenEvaluationTable}
                            evaluationTable={evaluationTable}/>
  )
}

export default AssignmentModifySettingsPageWrapper
