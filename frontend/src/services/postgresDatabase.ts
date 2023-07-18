import axios from "axios"
import {AssignmentInterface} from "../types/AssignmentInterface";
import {AssignmentToSendInterface} from "../types/AssignmentToSendInterface";
import {EvaluationInterface} from "../types/EvaluationInterface";
import {GroupCreateInterface} from "../types/GroupCreateInterface";
import {SolutionToSendInterface} from "../types/SolutionToSendInterface";
import {LoginUserInterface} from "../types/LoginUserInterface";
import {UserRegisterInterface} from "../types/UserRegisterInterface";
import {CredentialsInterface} from "../types/CredentialsInterface";

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
  groupId: string,
  assignment: AssignmentToSendInterface) =>{
  return postgresqlDatabase
    .post(
      `/assignment/withUserAndGroup/${userId}/${groupId}`,
      assignment)
}

export const postAuthAuthenticatePostgresService = (user: LoginUserInterface) => {
  return postgresqlDatabase.post("/auth/authenticate", user)
}

export const postAuthRegisterPostgresService = (user: UserRegisterInterface) => {
  return postgresqlDatabase.post("/auth/register", user)
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

export const postFileWithSolutionPostgresService = (
  mongoId: string,
  format: any,
  name: string,
  solutionId: string | number
) => {
  return postgresqlDatabase
    .post(`/file/withSolution/${solutionId}`, {
      mongo_id: mongoId,
      format: format,
      name: name,
    })
}

export const postEvaluationWithUserAndSolution = (userId: string, solutionId: string, evaluation: EvaluationInterface) => {
  return postgresqlDatabase.post(`/evaluation/withUserAndSolution/${userId}/${solutionId}`, evaluation)
}

export const postGroupAddTeacherPostgresService = (userId: string, groupId: string) => {
return postgresqlDatabase
  .post(`/group/addTeacher/${userId}/${groupId}`)
}
export const postGroupWithTeacherPostgresService = (userId: string, group: GroupCreateInterface) => {
  return postgresqlDatabase.post("/group/withTeacher/" + userId, group)
}

export const postChangePasswordPostgresService = (newCredentials: CredentialsInterface) => {
return postgresqlDatabase
  .post("/changePassword", newCredentials)
}

export const postGroupAddStudentPostgresService = (userId: string, groupId: string) => {
  return postgresqlDatabase.post(`/group/addStudent/${userId}/${groupId}`)
}

export const postSolutionWithUserAndAssignmentPostgresService = (userId: string, assignmentId:string, solution:SolutionToSendInterface) =>{
  return postgresqlDatabase
    .post(
      `/solution/withUserAndAssignment/${userId}/${assignmentId}`,
      solution
    )
}

export const getFilesByAssignmentPostgresService = (assigmentId: string | number) => {
  return postgresqlDatabase
    .get(`/files/byAssignment/${assigmentId}` )
}

export const getFilesBySolutionPostgresService = (solutionId: string | number) => {
  return postgresqlDatabase.get(
    `/files/bySolution/${solutionId}`
  )
}

export const getUserPostgresService = (userId: string) =>{
  return postgresqlDatabase.get(`/user/` + userId)
}

export const getGroupsByTeacherPostgresService = (userId: string) => {
  return postgresqlDatabase
    .get("/groups/byTeacher/" + userId)
}



export const getGroupsByStudentPostgresService = (userId: string) => {
  return postgresqlDatabase
    .get("/groups/byStudent/" + userId)
}

export const getGroupsByUserPostgresService = (userId: string) => {
  return postgresqlDatabase
    .get("/groups/byUser/" + userId)
}

export const getUncheckedSolutionByUserAssignmentGroupPostgresService = (userId: string, assignmentId: string, groupId: string) => {
  return postgresqlDatabase
    .get(`/solution/getUncheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getCheckedSolutionByUserAssignmentGroupPostgresService = (userId: string, assignmentId: string, groupId: string) => {
  return postgresqlDatabase
    .get(`/solution/getCheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getAssignmentPostgresService = (assignmentId: string) => {
  return postgresqlDatabase
    .get(`/assignment/${assignmentId}`)

}

export const getEvaluationBySolutionPostgresService = (solutionId: string | number) => {
  return postgresqlDatabase
    .get(`/evaluation/bySolution/${solutionId}`)
}

export const getExtendedSolutionsLateByGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase
    .get(`/extended/solutions/lateByGroup/${groupId}`)
}

export const getExtendedSolutionsUncheckedByGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase
    .get(`/extended/solutions/uncheckedByGroup/${groupId}`)
}

export const getExtendedSolutionsCheckedByGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase
    .get(`/extended/solutions/checkedByGroup/${groupId}`)
}

export const getAssignmentsByStudentPostgresService = (userId: string) => {
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

export const getUserGetStudentsByGroupPostgresService = (groupId: string) =>{
  return postgresqlDatabase
    .get("/user/getStudentsByGroup/" + groupId)
}

export const getUserGetTeachersByGroupPostgresService = (groupId: string) =>{
  return postgresqlDatabase
    .get("/user/getTeachersByGroup/" + groupId)
}

export const getGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase.get("/group/" + groupId)
}

export const getAssignmentsByGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase.get("/assignments/byGroupId/" + groupId)
}

export const getGroupUserCheckWithRolePostgresService = (userId: string, groupId: string) =>{
  return postgresqlDatabase
    .get(`/group/userCheckWithRole/${userId}/${groupId}`)
}


export const putAssignmentPostgresService = (assignment: AssignmentInterface) =>{
  return postgresqlDatabase
    .put(`/assignment/${assignment.id}`, assignment)
}

export const putGroupUnarchivePostgresService = (groupId: string) => {
  return postgresqlDatabase.put("/group/unarchive/" + groupId)
}

export const putGroupArchivePostgresService = (groupId: string) => {
  return postgresqlDatabase.put("/group/archive/" + groupId)
}

export const putGroupColorPostgresService = (groupId: string, color: number) => {
  return postgresqlDatabase.put("/group/color/" + groupId, color)
}

export const putUserColorPostgresService = (userId: string, color: number) => {
  return postgresqlDatabase
    .put(`/user/color/${userId}`, color)
}

export const putUserIndexPostgresService = (userId: string, index: number) => {
  return postgresqlDatabase
    .put(`/user/index/${userId}`, index)
}

export const putGroupNamePostgresService = (groupId:string, name: string) => {
  return axios.create({
    baseURL: "http://localhost:8080/api",
    timeout: 8000,
    headers: {
      "Content-Type": "text/plain",
      "Access-Control-Allow-Origin": "http://localhost:3000",
      ...(localStorage.getItem("token") && {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      }),
    },
  })
    .put(`/group/name/${groupId}`, name)
}

export const putGroupDescriptionPostgresService = (groupId:string, description: string) => {
  return axios.create({
    baseURL: "http://localhost:8080/api",
    timeout: 8000,
    headers: {
      "Content-Type": "text/plain",
      "Access-Control-Allow-Origin": "http://localhost:3000",
      ...(localStorage.getItem("token") && {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      }),
    },
  })
    .put(`/group/description/${groupId}`, description)
}

export const deleteAssignmentPostgresService = (assignmentId: string) => {
  return postgresqlDatabase
    .delete(`/assignment/${assignmentId}`)
}

export const deleteFilePostgresService = (fileId: string) => {
  return postgresqlDatabase.delete(`file/${fileId}`)
}

export const deleteGroupPostgresService = (groupId: string) => {
  return postgresqlDatabase.delete("/group/" + groupId)
}

export const deleteGroupDeleteStudentPostgresService = (userId: string, groupId: string) =>{
  return postgresqlDatabase
    .delete(`/group/deleteStudent/${userId}/${groupId}`)
}

export const deleteGroupDeleteTeacherPostgresService = (userId: string, groupId: string) =>{
  return postgresqlDatabase
    .delete(`/group/deleteTeacher/${userId}/${groupId}`)
}


