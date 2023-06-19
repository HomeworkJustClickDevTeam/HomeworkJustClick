import {Assigment, AssigmentFilterProp} from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"
import {Axios, AxiosError} from "axios";

export const assigmentFilterStudent = ({
  setAssignments,
  id,
  userId,
}: AssigmentFilterProp) => {
  const doneAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/doneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }

  const expiredAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/expiredUndoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => setAssignments(r.data))
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }

  const noneExpiredAssignments = (): void => {
    postgresqlDatabase
      .get(`/assignments/undoneByGroupAndStudent/${id}/${userId}`)
      .then((r) => {
        const assignments:Assigment[] = []
        r.data.map((assignment:Assigment) => {
          if(assignment.visible){
            assignments.push(assignment)
          }
        })
        setAssignments(assignments)
      })
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setAssignments([]) : console.log(e))
  }
  return {
    doneAssignments,
    expiredAssignments,
    noneExpiredAssignments,
  }
}
