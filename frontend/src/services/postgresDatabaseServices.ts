import axios from "axios"
import { AssignmentInterface } from "../types/AssignmentInterface"
import { AssignmentToSendInterface } from "../types/AssignmentToSendInterface"
import { EvaluationInterface } from "../types/EvaluationInterface"
import { GroupCreateInterface } from "../types/GroupCreateInterface"
import { SolutionCreateInterface } from "../types/SolutionCreateInterface"
import { LoginUserInterface } from "../types/LoginUserInterface"
import { UserRegisterInterface } from "../types/UserRegisterInterface"
import { CredentialsInterface } from "../types/CredentialsInterface"
import { getUser } from "./otherServices"
import { CommentCreateInterface } from "../types/CommentCreateInterface"
import { AdvancedEvaluationTextCommentCreateInterface } from "../types/AdvancedEvaluationTextCommentCreateInterface"
import { CommentInterface } from "../types/CommentInterface"
import { AdvancedEvaluationImageCommentInterface } from "../types/AdvancedEvaluationImageCommentInterface"
import { AdvancedEvaluationTextCommentInterface } from "../types/AdvancedEvaluationTextCommentInterface"
import { AdvancedEvaluationImageCommentCreateInterface } from "../types/AdvancedEvaluationImageCommentCreateInterface"
import { Table } from "../types/Table.model"

const postgresqlDatabaseJSON = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 8000,
  headers: {
    "Access-Control-Allow-Origin": "http://localhost:3000",
    "Content-Type": "application/json",
  },
})

const postgresqlDatabaseTextPlain = axios.create({
  baseURL: "http://localhost:8080/api",
  timeout: 8000,
  headers: {
    "Access-Control-Allow-Origin": "http://localhost:3000",
    "Content-Type": "text/plain",
  },
})

postgresqlDatabaseJSON.interceptors.request.use(
  async (config) => {
    const token = getUser()?.token
    if (token !== undefined) {
      config.headers["Authorization"] = "Bearer " + token
    } else {
      config.headers["Authorization"] = ""
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

postgresqlDatabaseJSON.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    if (
      error !== undefined &&
      error.response?.status === 403 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true
      localStorage.removeItem("user")
      window.dispatchEvent(new Event("storage"))
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

postgresqlDatabaseTextPlain.interceptors.request.use(
  async (config) => {
    const token = getUser()?.token
    if (token !== undefined) {
      config.headers["Authorization"] = "Bearer " + token
    } else {
      config.headers["Authorization"] = ""
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

postgresqlDatabaseTextPlain.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config
    if (error.response.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true
      localStorage.removeItem("user")
      window.dispatchEvent(new Event("storage"))
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

export const createAssignmentWithUserAndGroupPostgresService = async (
  userId: string,
  groupId: string,
  assignment: AssignmentToSendInterface
) => {
  return await postgresqlDatabaseJSON.post(
    `/assignment/withUserAndGroup/${userId}/${groupId}`,
    assignment
  )
}

export const refreshToken = async (userId: number) => {
  return await postgresqlDatabaseJSON.post(`/refreshToken/${userId}`)
}
export const loginPostgresService = async (user: LoginUserInterface) => {
  return await postgresqlDatabaseJSON.post("/auth/authenticate", user)
}

export const registerPostgresService = async (user: UserRegisterInterface) => {
  return await postgresqlDatabaseJSON.post("/auth/register", user)
}

export const createFileWithAssignmentPostgresService = async (
  mongoId: string,
  format: any,
  name: string,
  assignmentId: string
) => {
  return await postgresqlDatabaseJSON.post(
    `file/withAssignment/${assignmentId}`,
    {
      mongo_id: mongoId,
      format: format,
      name: name,
    }
  )
}

export const createCommentWithUserPostgresService = async (
  comment: CommentCreateInterface
) => {
  return await postgresqlDatabaseJSON.post("/comment", comment)
}

export const createCommentTextWithFilePostgresService = async (
  comment: AdvancedEvaluationTextCommentCreateInterface
) => {
  return await postgresqlDatabaseJSON.post("/comment_file_text", comment)
}

export const createCommentImageWithFilePostgresService = async (
  comment: AdvancedEvaluationImageCommentCreateInterface
) => {
  return await postgresqlDatabaseJSON.post("/comment_file_img", comment)
}
export const createFileWithSolutionPostgresService = async (
  mongoId: string,
  format: any,
  name: string,
  solutionId: string | number
) => {
  return await postgresqlDatabaseJSON.post(`/file/withSolution/${solutionId}`, {
    mongo_id: mongoId,
    format: format,
    name: name,
  })
}

export const createEvaluationWithUserAndSolution = async (
  userId: string,
  solutionId: string,
  evaluation: EvaluationInterface
) => {
  return await postgresqlDatabaseJSON.post(
    `/evaluation/withUserAndSolution/${userId}/${solutionId}`,
    evaluation
  )
}

export const addTeacherToGroupPostgresService = async (
  userId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.post(
    `/group/addTeacher/${userId}/${groupId}`
  )
}
export const createGroupWithTeacherPostgresService = async (
  userId: string,
  group: GroupCreateInterface
) => {
  return await postgresqlDatabaseJSON.post(
    "/group/withTeacher/" + userId,
    group
  )
}

export const changePasswordPostgresService = async (
  newCredentials: CredentialsInterface
) => {
  return await postgresqlDatabaseJSON.post("/changePassword", newCredentials)
}

export const addStudentToGroupPostgresService = async (
  userId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.post(
    `/group/addStudent/${userId}/${groupId}`
  )
}

export const createSolutionWithUserAndAssignmentPostgresService = async (
  userId: string,
  assignmentId: string,
  solution: SolutionCreateInterface
) => {
  return await postgresqlDatabaseJSON.post(
    `/solution/withUserAndAssignment/${userId}/${assignmentId}`,
    solution
  )
}

export const getFilesByAssignmentPostgresService = async (
  assignmentId: string | number
) => {
  return await postgresqlDatabaseJSON.get(`/files/byAssignment/${assignmentId}`)
}

export const getFilesBySolutionPostgresService = async (
  solutionId: string | number
) => {
  return await postgresqlDatabaseJSON.get(`/files/bySolution/${solutionId}`)
}

export const getUserPostgresService = async (userId: string) => {
  return await postgresqlDatabaseJSON.get(`/user/` + userId)
}

export const getGroupsByTeacherPostgresService = async (userId: string) => {
  return await postgresqlDatabaseJSON.get("/groups/byTeacher/" + userId)
}

export const getGroupsByStudentPostgresService = async (userId: string) => {
  return await postgresqlDatabaseJSON.get("/groups/byStudent/" + userId)
}

export const getGroupsByUserPostgresService = async (userId: string) => {
  return await postgresqlDatabaseJSON.get("/groups/byUser/" + userId)
}

export const getUncheckedSolutionByUserAssignmentGroupPostgresService = async (
  userId: string,
  assignmentId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/solution/getUncheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`
  )
}

export const getCheckedSolutionByUserAssignmentGroupPostgresService = async (
  userId: string,
  assignmentId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/solution/getCheckedSolutionByUserAssignmentGroup/${userId}/${assignmentId}/${groupId}`
  )
}

export const getAssignmentPostgresService = async (assignmentId: string) => {
  return await postgresqlDatabaseJSON.get(`/assignment/${assignmentId}`)
}

export const getEvaluationBySolutionPostgresService = async (
  solutionId: string | number
) => {
  return await postgresqlDatabaseJSON.get(
    `/evaluation/bySolution/${solutionId}`
  )
}

export const getExtendedSolutionsLateByGroupPostgresService = async (
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/extended/solutions/lateByGroup/${groupId}`
  )
}

export const getExtendedSolutionsUncheckedByGroupPostgresService = async (
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/extended/solutions/uncheckedByGroup/${groupId}`
  )
}

export const getExtendedSolutionsCheckedByGroupPostgresService = async (
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/extended/solutions/checkedByGroup/${groupId}`
  )
}

export const getAssignmentsByStudentPostgresService = async (
  userId: string
) => {
  return await postgresqlDatabaseJSON.get("/assignments/byStudent/" + userId)
}

export const getAssignmentsDoneByGroupAndStudentPostgresService = async (
  groupId: string,
  userId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/assignments/doneByGroupAndStudent/${groupId}/${userId}`
  )
}

export const getAssignmentsExpiredUndoneByGroupAndStudentPostgresService =
  async (groupId: string, userId: string) => {
    return await postgresqlDatabaseJSON.get(
      `/assignments/expiredUndoneByGroupAndStudent/${groupId}/${userId}`
    )
  }

export const getAssignmentsUndoneByGroupAndStudentPostgresService = async (
  groupId: string,
  userId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/assignments/undoneByGroupAndStudent/${groupId}/${userId}`
  )
}

export const getStudentsByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.get("/user/getStudentsByGroup/" + groupId)
}

export const getTeachersByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.get("/user/getTeachersByGroup/" + groupId)
}

export const getGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.get("/group/" + groupId)
}

export const getAssignmentsByGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.get("/assignments/byGroupId/" + groupId)
}

export const getCommentsByUserPostgresService = async (
  userId: string,
  params: string
) => {
  return await postgresqlDatabaseJSON.get(`/comment/byUser/${userId}?${params}`)
}

export const getCommentsTextByFilePostgresService = async (
  fileId: string,
  params: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/comment_file_text/byFileId/${fileId}?${params}`
  )
}

export const getCommentsImageByFilePostgresService = async (
  fileId: string,
  params: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/comment_file_img/byFileId/${fileId}?${params}`
  )
}

export const getUserRoleInGroupPostgresService = async (
  userId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.get(
    `/group/userCheckWithRole/${userId}/${groupId}`
  )
}

export const changeAssignmentPostgresService = async (
  assignment: AssignmentInterface
) => {
  return await postgresqlDatabaseJSON.put(
    `/assignment/${assignment.id}`,
    assignment
  )
}

export const unarchiveGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.put("/group/unarchive/" + groupId)
}

export const archiveGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.put("/group/archive/" + groupId)
}

export const changeGroupColorPostgresService = async (
  groupId: string,
  color: number
) => {
  return await postgresqlDatabaseJSON.put("/group/color/" + groupId, color)
}

export const changeUserColorPostgresService = async (
  userId: string,
  color: number
) => {
  return await postgresqlDatabaseJSON.put(`/user/color/${userId}`, color)
}

export const changeUserIndexPostgresService = async (
  userId: string,
  index: number
) => {
  return await postgresqlDatabaseJSON.put(`/user/index/${userId}`, index)
}
export const changeCommentPostgresService = async (
  comment: CommentInterface
) => {
  return await postgresqlDatabaseJSON.put(`/comment/${comment.id}`, comment)
}
export const changeCommentImagePostgresService = async (
  comment: AdvancedEvaluationImageCommentInterface
) => {
  return await postgresqlDatabaseJSON.put(
    `/comment_file_img/${comment.id}`,
    comment
  )
}
export const changeCommentTextPostgresService = async (
  comment: AdvancedEvaluationTextCommentInterface
) => {
  return await postgresqlDatabaseJSON.put(
    `/comment_file_text/${comment.id}`,
    comment
  )
}

export const changeGroupNamePostgresService = async (
  groupId: string,
  name: string
) => {
  return await postgresqlDatabaseTextPlain.put(`/group/name/${groupId}`, name)
}

export const changeGroupDescriptionPostgresService = async (
  groupId: string,
  description: string
) => {
  return await postgresqlDatabaseTextPlain.put(
    `/group/description/${groupId}`,
    description
  )
}

export const deleteAssignmentPostgresService = async (assignmentId: string) => {
  return await postgresqlDatabaseJSON.delete(`/assignment/${assignmentId}`)
}

export const deleteCommentTextByCommentFilePostgresService = async (
  commentId: string,
  fileId: string
) => {
  return await postgresqlDatabaseJSON.delete(
    `/comment_file_text/byCommentFile/${commentId}/${fileId}`
  )
}

export const deleteCommentImageByCommentFilePostgresService = async (
  commentId: string,
  fileId: string
) => {
  return await postgresqlDatabaseJSON.delete(
    `/comment_file_img/byCommentFile/${commentId}/${fileId}`
  )
}

export const deleteFilePostgresService = async (fileId: string) => {
  return await postgresqlDatabaseJSON.delete(`file/${fileId}`)
}

export const deleteGroupPostgresService = async (groupId: string) => {
  return await postgresqlDatabaseJSON.delete("/group/" + groupId)
}
export const deleteCommentImagePostgresService = async (commentId: string) => {
  return await postgresqlDatabaseJSON.delete(`/comment_file_img/${commentId}`)
}

export const deleteCommentTextPostgresService = async (commentId: string) => {
  return await postgresqlDatabaseJSON.delete(`/comment_file_text/${commentId}`)
}
export const deleteCommentPostgresService = async (commentId: string) => {
  return await postgresqlDatabaseJSON.delete(`comment/${commentId}`)
}

export const deleteStudentInGroupPostgresService = async (
  userId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.delete(
    `/group/deleteStudent/${userId}/${groupId}`
  )
}

export const deleteTeacherInGroupPostgresService = async (
  userId: string,
  groupId: string
) => {
  return await postgresqlDatabaseJSON.delete(
    `/group/deleteTeacher/${userId}/${groupId}`
  )
}

export const createEvaluationPanel = async (evaluationPanel: Table) => {
  return await postgresqlDatabaseJSON.post("/evaluation_panel", evaluationPanel)
}
