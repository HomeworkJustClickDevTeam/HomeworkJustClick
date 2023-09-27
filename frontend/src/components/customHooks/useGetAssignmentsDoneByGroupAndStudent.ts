import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import {
  getAssignmentsDoneByGroupAndStudentPostgresService,
  getAssignmentsExpiredUndoneByGroupAndStudentPostgresService, getAssignmentsUndoneByGroupAndStudentPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetAssignmentsByGroupAndStudent = (groupId: number|undefined, userId: number|undefined, type:"done"|"expiredUndone"|"undone") => {
  const dispatch = useAppDispatch()
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])

  useEffect(() => {
    const fetchData =async () => {
      let response = null
      if(groupId !==undefined && userId !== undefined){
        dispatch(setIsLoading(true))
        try{
          if(type==='done'){
            response = await getAssignmentsDoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          else if(type==="expiredUndone"){
            response = await getAssignmentsExpiredUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          else if(type==="undone"){
            response = await getAssignmentsUndoneByGroupAndStudentPostgresService(groupId.toString(), userId.toString())
          }
          if(response !== undefined && response!==null){
            if(mounted){
              setAssignments(response.data)
            }
          }
        }
        catch (e) {console.log(e)}
      }
    }
    let mounted = true
    fetchData()
    dispatch(setIsLoading(false))
    return () => {mounted = false}

  }, [userId,groupId,type])

  return assignments
}