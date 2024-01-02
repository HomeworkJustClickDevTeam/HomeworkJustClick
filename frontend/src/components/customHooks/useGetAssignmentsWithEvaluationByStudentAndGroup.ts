import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import {
  getAssignmentsDoneByGroupAndStudentPostgresService,
  getAssignmentsExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsNonExpiredUndoneByGroupAndStudentPostgresService,
  getAssignmentsUndoneByGroupAndStudentPostgresService, getAssignmentsWithEvaluationByStudentAndGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { AssignmentsType } from "../../types/AssignmentsType"
import {AssignmentWithEvaluationModel} from "../../types/AssignmentWithEvaluation.model";

export const useGetAssignmentsWithEvaluationByGroupAndStudent = (groupId: number|undefined|null, userId: number|undefined|null) => {
  const dispatch = useAppDispatch()
  const [assignments, setAssignments] = useState<AssignmentWithEvaluationModel[]>([])

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(groupId !==undefined && userId !== undefined && groupId !==null && userId !== null){
        try{
          response = await getAssignmentsWithEvaluationByStudentAndGroupPostgresService(userId.toString(),groupId.toString())
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

  }, [userId,groupId])

  return assignments
}