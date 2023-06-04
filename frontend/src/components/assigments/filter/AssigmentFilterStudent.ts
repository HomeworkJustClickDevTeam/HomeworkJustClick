import { AssigmentFilterProp } from "../../../types/types"
import common_request from "../../../services/default-request-database"

export const assigmentFilterStudent = ({
  setAssignments,
  id,
  userId,
}: AssigmentFilterProp) => {
  const doneAssignments = (): void => {
    common_request
      .get(`/assignments/doneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e) => console.log(e))
  }

  const expiredAssignments = (): void => {
    common_request
      .get(`/assignments/expiredUndoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e) => console.log(e))
  }

  const noneExpiredAssignments = (): void => {
    common_request
      .get(`/assignments/undoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
  }
  return {
    doneAssignments,
    expiredAssignments,
    noneExpiredAssignments,
  }
}
