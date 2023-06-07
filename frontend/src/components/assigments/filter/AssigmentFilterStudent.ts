import { AssigmentFilterProp } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"

export const assigmentFilterStudent = ({
  setAssignments,
  id,
  userId,
}: AssigmentFilterProp) => {
  const doneAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/doneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e) => console.log(e))
  }

  const expiredAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/expiredUndoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e) => console.log(e))
  }

  const noneExpiredAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/undoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
  }
  return {
    doneAssignments,
    expiredAssignments,
    noneExpiredAssignments,
  }
}
