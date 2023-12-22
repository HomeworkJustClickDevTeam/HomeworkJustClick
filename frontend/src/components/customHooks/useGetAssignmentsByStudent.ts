import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getAssignmentsByStudentPostgresService,
  getAssignmentsDoneByStudentPostgresService,
  getAssignmentsExpiredUndoneByStudentPostgresService,
  getAssignmentsUndoneByStudentPostgresService
} from "../../services/postgresDatabaseServices"
import {AssignmentsType} from "../../types/AssignmentsType";

export const useGetAssignmentsByStudent = (studentId:number|undefined|null, filter?: AssignmentsType) => {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(studentId !== undefined && studentId !== null){
        try {
          switch (filter){
            case undefined:
              response = await getAssignmentsByStudentPostgresService(studentId.toString())
              break
            case "done":
              response = await getAssignmentsDoneByStudentPostgresService(studentId.toString())
              break
            case "undone":
              response = await getAssignmentsUndoneByStudentPostgresService(studentId.toString())
              break
            case "expiredUndone":
              response = await getAssignmentsExpiredUndoneByStudentPostgresService(studentId.toString())
              break
          }
          if(response?.status === 200){
            if(mounted){setAssignments(response!.data)}
          }
        }catch (error:any) {
          if(error !== null && error!== undefined && error.response.status === 404){
            if(mounted){setAssignments([])}
          }
          else{
            console.log(error)
          }
        }
      }
    }

    let mounted = true
    dispatch(setIsLoading(true))
    fetchData()
    dispatch(setIsLoading(false))
    return () => {mounted = false}
  }, [studentId, filter])

  return assignments
}