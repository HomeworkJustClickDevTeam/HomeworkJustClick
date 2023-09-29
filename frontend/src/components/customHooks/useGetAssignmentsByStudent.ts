import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getAssignmentsByStudentPostgresService } from "../../services/postgresDatabaseServices"

export const useGetAssignmentsByStudent = (studentId:number|undefined|null) => {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      if(studentId !== undefined && studentId !== null){
        try {
          const response = await getAssignmentsByStudentPostgresService(studentId.toString())
          if(response !== null && response !== undefined){
            if(mounted){setAssignments(response.data)}
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