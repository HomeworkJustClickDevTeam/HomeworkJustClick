import { useEffect, useState } from "react"
import { AssignmentModel } from "../../types/Assignment.model"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getAssignmentsByStudentPostgresService,
  getAssignmentsDoneByStudentPostgresService,
  getAssignmentsExpiredUndoneByStudentPostgresService, getAssignmentsNonExpiredUndoneByStudentPostgresService,
  getAssignmentsUndoneByStudentPostgresService, getAssignmentsWithEvaluationByStudentPostgresService
} from "../../services/postgresDatabaseServices"
import {AssignmentsType} from "../../types/AssignmentsType";
import {AssignmentWithEvaluationModel} from "../../types/AssignmentWithEvaluation.model";

export const useGetAssignmentsWithEvaluationByStudent = (studentId:number|undefined|null) => {
  const [assignments, setAssignments] = useState<AssignmentWithEvaluationModel[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(studentId !== undefined && studentId !== null){
        try {
          response = await getAssignmentsWithEvaluationByStudentPostgresService(studentId.toString())
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
  }, [studentId])

  return assignments
}