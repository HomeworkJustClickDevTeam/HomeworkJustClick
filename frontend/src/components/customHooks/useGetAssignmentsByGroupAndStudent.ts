import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import {
  getAssignmentsDoneByGroupAndStudentPostgresService,
  getAssignmentsExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsUndoneByGroupAndStudentPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { AssignmentsType } from "../../types/AssignmentsType"

export const useGetAssignmentsByGroupAndStudent = (groupId: number|undefined|null, userId: number|undefined|null, filter:AssignmentsType) => {
  const dispatch = useAppDispatch()
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])

  useEffect(() => {
    const fetchData =async () => {
      let response = null
      if(groupId !==undefined && userId !== undefined && groupId !==null && userId !== null){
        dispatch(setIsLoading(true))
        try{
          if(filter==='done'){
            response = await getAssignmentsDoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          else if(filter==="expiredUndone"){
            response = await getAssignmentsExpiredUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          else if(filter==="undone"){
            response = await getAssignmentsUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          if(response !== undefined && response!==null){
            if(mounted){
              setAssignments(response.data)
            }
          }
        }
        catch (e) {console.log(e)}
        dispatch(setIsLoading(false))
      }
    }
    let mounted = true
    fetchData()

    return () => {mounted = false}

  }, [userId,groupId,filter])

  return assignments
}