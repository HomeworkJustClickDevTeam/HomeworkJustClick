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
        dispatch(setIsLoading(true))
        try {
          const response = await getAssignmentsByStudentPostgresService(studentId.toString())
          if(response !== null && response !== undefined){
            setAssignments(response.data)
          }
        }catch (error:any) {
          if(error !== null && error!== undefined && error.response.status === 404){
            setAssignments([])
          }
          else{
            console.log(error)
          }
        }
        dispatch(setIsLoading(false))
      }
    }

    let mounted = true
    fetchData()
    return () => {mounted = false}
  }, [studentId])

  return assignments
}