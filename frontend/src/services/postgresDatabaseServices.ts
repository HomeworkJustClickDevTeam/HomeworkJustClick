import axios from "axios"
import {AssignmentInterface} from "../types/AssignmentInterface";
import {AssignmentToSendInterface} from "../types/AssignmentToSendInterface";
import {EvaluationInterface} from "../types/EvaluationInterface";
import {GroupCreateInterface} from "../types/GroupCreateInterface";
import {SolutionToSendInterface} from "../types/SolutionToSendInterface";
import {LoginUserInterface} from "../types/LoginUserInterface";
import {UserRegisterInterface} from "../types/UserRegisterInterface";
import {CredentialsInterface} from "../types/CredentialsInterface";
import {getUser} from "./otherServices";

const postgresqlDatabase = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 8000,
  headers: {
    "Access-Control-Allow-Origin": "http://localhost:3000"
  },
})

postgresqlDatabase.interceptors.request.use(async (config) => {
    const token = getUser()?.token
    config.headers["Content-Type"] = "application/json"
    if (token !== undefined) {
      config.headers["Authorization"] = "Bearer " + token
    } else {
      config.headers["Authorization"] = ""
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  })


export const createAssignmentWithUserAndGroupPostgresService = async (
  userId: string,
  groupId: string,
  assignment: AssignmentToSendInterface) => {
  return await postgresqlDatabase
    .post(
      `/assignment/withUserAndGroup/${userId}/${groupId}`,
      assignment)
}

export const loginPostgresService = async (user: LoginUserInterface) => {
  return await postgresqlDatabase.post("/auth/authenticate", user)
}

export const registerPostgresService = async (user: UserRegisterInterface) => {
  return await postgresqlDatabase.post("/auth/register", user)
}

export const createFileWithAssignmentPostgresService = async (
  mongoId: string,
  format: any,
  name: string,
  assignmentId: string
) => {
  return await postgresqlDatabase
    .post(`file/withAssignment/${assignmentId}`, {
      mongo_id: mongoId,
      format: format,
      name: name,
    })
}

export const createFileWithSolutionPostgresService = async (
  mongoId: string,
  format: any,
  name: string,
  solutionId: string | number
) => {
  return await postgresqlDatabase
    .post(`/file/withSolution/${solutionId}`, {
      mongo_id: mongoId,
      format: format,
      name: name,
    })
}

export const createEvaluationWithUserAndSolution = async (userId: string, solutionId: string, evaluation: EvaluationInterface) => {
  return await postgresqlDatabase.post(`/evaluation/withUserAndSolution/${userId}/${solutionId}`, evaluation)
}

export const addTeacherToGroupPostgresService = async (userId: string, groupId: string) => {
  return await postgresqlDatabase
    .post(`/group/addTeacher/${userId}/${groupId}`)
}
export const createGroupWithTeacherPostgresService = async (userId: string, group: GroupCreateInterface) => {
  return await postgresqlDatabase.post("/group/withTeacher/" + userId, group)
}

export const changePasswordPostgresService = async (newCredentials: CredentialsInterface) => {
  return await postgresqlDatabase
    .post("/changePassword", newCredentials)
}

export const addStudentToGroupPostgresService = async (userId: string, groupId: string) => {
  return await postgresqlDatabase.post(`/group/addStudent/${userId}/${groupId}`)
}

export const createSolutionWithUserAndAssignmentPostgresService = async (userId: string, assignmentId: string, solution: SolutionToSendInterface) => {
  return await postgresqlDatabase
    .post(
      `/solution/withUserAndAssignment/${userId}/${assignmentId}`,
      solution
    )
}

export const  getFilesByAssignmentPostgresService = async (assignmentId: string | number) => {
  return await postgresqlDatabase
    .get(`/files/byAssignment/${assignmentId}`)
}

export const getFilesBySolutionPostgresService = async (solutionId: string | number) => {
  return await postgresqlDatabase.get(
    `/files/bySolution/${solutionId}`
  )
}

export const getUserPostgresService = async (userId: string) => {
  return await postgresqlDatabase.get(`/user/` + userId)
}

export const getGroupsByTeacherPostgresService = async (userId: string) => {
  return await postgresqlDatabase
    .get("/groups/byTeacher/" + userId)
}


export const getGroupsByStudentPostgresService = async (userId: string) => {
  return await postgresqlDatabase
    .get("/groups/byStudent/" + userId)
}

export const getGroupsByUserPostgresService = async (userId: string) => {
  return await postgresqlDatabase
    .get("/groups/byUser/" + userId)
}

export const getUncheckedSolutionByUserAssignmentGroupPostgresService = async (userId: string, assignmentId: string, groupId: string) => {
  return await postgresqlDatabase
    .get(`/solution/getUncheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getCheckedSolutionByUserAssignmentGroupPostgresService = async (userId: string, assignmentId: string, groupId: string) => {
  return await postgresqlDatabase
    .get(`/solution/getCheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`)
}

export const getAssignmentPostgresService = async (assignmentId: string) => {
  return await postgresqlDatabase
    .get(`/assignment/${assignmentId}`)

}

export const getEvaluationBySolutionPostgresService = async (solutionId: string | number) => {
  return await postgresqlDatabase
    .get(`/evaluation/bySolution/${solutionId}`)
}

export const getExtendedSolutionsLateByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase
    .get(`/extended/solutions/lateByGroup/${groupId}`)
}

export const getExtendedSolutionsUncheckedByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase
    .get(`/extended/solutions/uncheckedByGroup/${groupId}`)
}

export const getExtendedSolutionsCheckedByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase
    .get(`/extended/solutions/checkedByGroup/${groupId}`)
}

export const getAssignmentsByStudentPostgresService = async (userId: string) => {
  return await postgresqlDatabase.get("/assignments/byStudent/" + userId)
}

export const getAssignmentsDoneByGroupAndStudentPostgresService = async (groupId: string, userId: string) => {
  return await postgresqlDatabase.get(`/assignments/doneByGroupAndStudent/${groupId}/${userId}`)
}

export const getAssignmentsExpiredUndoneByGroupAndStudentPostgresService = async (groupId: string, userId: string) => {
  return await postgresqlDatabase.get(`/assignments/expiredUndoneByGroupAndStudent/${groupId}/${userId}`)
}

export const getAssignmentsUndoneByGroupAndStudentPostgresService = async (groupId: string, userId: string) => {
  return await postgresqlDatabase.get(`/assignments/undoneByGroupAndStudent/${groupId}/${userId}`)
}

export const getStudentsByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase
    .get("/user/getStudentsByGroup/" + groupId)
}

export const getTeachersByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase
    .get("/user/getTeachersByGroup/" + groupId)
}

export const getGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase.get("/group/" + groupId)
}

export const getAssignmentsByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase.get("/assignments/byGroupId/" + groupId)
}

export const getUserRoleInGroupPostgresService = async (userId: string, groupId: string) => {
  return await postgresqlDatabase
    .get(`/group/userCheckWithRole/${userId}/${groupId}`)
}


export const changeAssignmentPostgresService = async (assignment: AssignmentInterface) => {
  return await postgresqlDatabase
    .put(`/assignment/${assignment.id}`, assignment)
}

export const unarchiveGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase.put("/group/unarchive/" + groupId)
}

export const archiveGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase.put("/group/archive/" + groupId)
}

export const changeGroupColorPostgresService = (groupId: string, color: number) => {
  return postgresqlDatabase.put("/group/color/" + groupId, color)
}

export const changeUserColorPostgresService = async (userId: string, color: number) => {
  return await postgresqlDatabase
    .put(`/user/color/${userId}`, color)
}

export const changeUserIndexPostgresService = async (userId: string, index: number) => {
  return await postgresqlDatabase
    .put(`/user/index/${userId}`, index)
}

export const changeGroupNamePostgresService = async (groupId: string, name: string) => {
  postgresqlDatabase.interceptors.request.use(async config => {
    config.headers["Content-Type"] = "text/plain"
    return config
  })
  return await postgresqlDatabase.put(`/group/name/${groupId}`, name)
}

export const changeGroupDescriptionPostgresService = async (groupId: string, description: string) => {
  postgresqlDatabase.interceptors.request.use(async config => {
    config.headers["Content-Type"] = "text/plain"
    return config
  })
  return await postgresqlDatabase.put(`/group/description/${groupId}`, description)
}

export const deleteAssignmentPostgresService = (assignmentId: string) => {
  return postgresqlDatabase
    .delete(`/assignment/${assignmentId}`)
}

export const deleteFilePostgresService = async (fileId: string) => {
  return await postgresqlDatabase.delete(`file/${fileId}`)
}

export const deleteGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabase.delete("/group/" + groupId)
}

export const deleteStudentInGroupPostgresService = async (userId: string, groupId: string) => {
  return await postgresqlDatabase
    .delete(`/group/deleteStudent/${userId}/${groupId}`)
}

export const deleteTeacherInGroupPostgresService = async (userId: string, groupId: string) => {
  return await postgresqlDatabase
    .delete(`/group/deleteTeacher/${userId}/${groupId}`)
}


