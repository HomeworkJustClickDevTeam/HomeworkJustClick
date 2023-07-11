import axios, {AxiosInstance, AxiosResponse} from "axios"
import {AssignmentInterface} from "../types/AssignmentInterface";
import {SetStateAction} from "react";
import {AssignmentToSendInterface} from "../types/AssignmentToSendInterface";
import {AssignmentAddFilePropsInterface} from "../types/AssignmentAddFilePropsInterface";
import {FileFromPostInterface} from "../types/FileFromPostInterface";
import {EvaluationInterface} from "../types/EvaluationInterface";
import GroupCreatePage from "../components/group/GroupCreatePage";
import {GroupCreateInterface} from "../types/GroupCreateInterface";

const postgresqlDatabase = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 8000,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "http://localhost:3000",
    ...(localStorage.getItem("token") && {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    }),
  },
})

export const postAssignmentWithUserAndGroupPostgresService = (
  userId: string,
  groupId: string | undefined,
  assignment: AssignmentToSendInterface) =>{
  return postgresqlDatabase
    .post(
      `/assignment/withUserAndGroup/${userId}/${groupId}`,
      assignment)
}

export const postFileWithAssignmentPostgresService = (
  mongoId: string,
  format: any,
  name: string,
  assignmentId: string
) => {
  return postgresqlDatabase
    .post(`file/withAssignment/${assignmentId}`, {
      mongo_id: mongoId,
      format: format,
      name: name,
    })
}

export const postEvaluationWithUserAndSolution = (userId: string, solutionId: string, evaluation: EvaluationInterface) => {
  return postgresqlDatabase.post(`/evaluation/withUserAndSolution/${userId}/${solutionId}`, evaluation)
}

export const postGroupWithTeacherPostgresService = (userId: string, group: GroupCreateInterface) => {
  return postgresqlDatabase.post("/group/withTeacher/" + userId, group)
}

export const getFilesByAssignmentPostgresService = (assigmentId: number) => {
  return postgresqlDatabase
    .get(`/files/byAssignment/${assigmentId}`    )
}

export const getUncheckedSolutionByUserAssignmentGroupPostgresService = (userId: string, assignmentId: string | undefined, groupId: undefined | string) => {
  return postgresqlDatabase
    .get(`/solution/getUncheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getCheckedSolutionByUserAssignmentGroupPostgresService = (userId: string, assignmentId: string | undefined, groupId: undefined | string) => {
  return postgresqlDatabase
    .get(`/solution/getCheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getAssignmentPostgresService = (assignmentId: string | undefined) => {
  return postgresqlDatabase
    .get(`/assignment/${assignmentId}`)

}

export const getAssignmentsByStudentPostgresService = (userId: string | undefined) => {
  return postgresqlDatabase.get("/assignments/byStudent/" + userId)
}

export const getAssignmentsDoneByGroupAndStudentPostgresService = (groupId:string, userId: string) =>{
  return postgresqlDatabase.get(`/assignments/doneByGroupAndStudent/${groupId}/${userId}`)
}

export const getAssignmentsExpiredUndoneByGroupAndStudentPostgresService = (groupId:string, userId: string) =>{
  return postgresqlDatabase.get(`/assignments/expiredUndoneByGroupAndStudent/${groupId}/${userId}`)
}

export const getAssignmentsUndoneByGroupAndStudentPostgresService = (groupId:string, userId: string) =>{
  return postgresqlDatabase.get(`/assignments/undoneByGroupAndStudent/${groupId}/${userId}`)
}

export const getGroupPostgresService = (groupId: string | undefined) => {
  return postgresqlDatabase.get("/group/" + groupId)
}

export const getAssignmentsByGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase.get("/assignments/byGroupId/" + groupId)
}

export const putAssignmentPostgresService = (assignment: AssignmentInterface) =>{
  return postgresqlDatabase
    .put(`/assignment/${assignment.id}`, assignment)
}

export const putGroupUnarchivePostgresService = (groupId: string | undefined) => {
  return postgresqlDatabase.put("/group/unarchive/" + groupId)
}

export const putGroupArchivePostgresService = (groupId: string | undefined) => {
  return postgresqlDatabase.put("/group/archive/" + groupId)
}

export const putGroupColorPostgresService = (groupId: string | undefined, color: number) => {
  return postgresqlDatabase.put("/group/color/" + groupId, color)
}

export const putGroupNamePostgresService = (groupId:string, name: string) => {
  return postgresqlDatabase({headers:{"Content-Type":"text/plain"}})
}

export const deleteAssignmentPostgresService = (assignmentId: string) => {
  return postgresqlDatabase
    .delete(`/assignment/${assignmentId}`)
}

export const deleteFilePostgresService = (fileId: string) => {
  return postgresqlDatabase.delete(`file/${fileId}`)
}

export const deleteGroupPostgresService = (groupId: string | undefined) => {
  return postgresqlDatabase.delete("/group/" + groupId)
}


export default postgresqlDatabase
