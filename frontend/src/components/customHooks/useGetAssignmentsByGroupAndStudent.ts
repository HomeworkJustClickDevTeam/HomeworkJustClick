import { useEffect, useState } from "react"
import { AssignmentModel } from "../../types/Assignment.model"
import {
  getAssignmentsDoneByGroupAndStudentPostgresService,
  getAssignmentsExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsNonExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsUndoneByGroupAndStudentPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { AssignmentsType } from "../../types/AssignmentsType"

export const useGetAssignmentsByGroupAndStudent = (groupId: number|undefined|null, userId: number|undefined|null, filter:AssignmentsType) => {
  const dispatch = useAppDispatch()
  const [assignments, setAssignments] = useState<AssignmentModel[]>([])

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(groupId !==undefined && userId !== undefined && groupId !==null && userId !== null){
        try{
          switch (filter){
            case "done":
              response = await getAssignmentsDoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
              break
            case "expiredUndone":
              response = await getAssignmentsExpiredUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
              break
            case "undone":
              response = await getAssignmentsUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
              break
            case "nonExpiredUndone":
              response = await getAssignmentsNonExpiredUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
              break
          }
          if(response !== undefined && response!==null){
            if(mounted){
              setAssignments(response.data)
            }
          }
        }
        catch (error:any) {
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

  }, [userId,groupId,filter])

  return assignments
}