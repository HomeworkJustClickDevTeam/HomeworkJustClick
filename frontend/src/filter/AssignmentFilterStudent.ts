import {
  getAssignmentsDoneByGroupAndStudentPostgresService,
  getAssignmentsExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsUndoneByGroupAndStudentPostgresService
} from "../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { AssignmentInterface } from "../types/AssignmentInterface"

interface AssignmentFilterStudentProps {
  setAssignments: (assignments: AssignmentInterface[]) => void
  userId: string
  idGroup: string
}

export const assignmentFilterStudent = ({
                                         setAssignments,
                                         idGroup,
                                         userId,
                                       }: AssignmentFilterStudentProps) => {
  const doneAssignments = (): void => {
    getAssignmentsDoneByGroupAndStudentPostgresService(idGroup, userId)
      .then((r) => setAssignments(r.data as AssignmentInterface[]))
      .catch((e: AxiosError) => (e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }

  const expiredAssignments = (): void => {
    getAssignmentsExpiredUndoneByGroupAndStudentPostgresService(idGroup, userId)
      .then((r) => setAssignments(r.data))
      .catch((e: AxiosError) => (e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }

  const noneExpiredAssignments = (): void => {
    getAssignmentsUndoneByGroupAndStudentPostgresService(idGroup, userId)
      .then((r) => {
        const assignments: AssignmentInterface[] = []
        r.data.map((assignment: AssignmentInterface) => {
          if (assignment.visible) {
            assignments.push(assignment)
          }
        })
        setAssignments(assignments)
      })
      .catch((e: AxiosError) => (e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }
  return {
    doneAssignments,
    expiredAssignments,
    noneExpiredAssignments,
  }
}
