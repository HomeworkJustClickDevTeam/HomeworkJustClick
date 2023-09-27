import { useEffect, useState } from "react"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { getAssignmentPostgresService } from "../../services/postgresDatabaseServices"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { parseISO } from "date-fns"

export const useGetAssignment = (assignmentId:number|undefined) => {
  const [assignment, setAssignment] = useState<AssignmentInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(assignmentId!== undefined) {
      dispatch(setIsLoading(true))
      getAssignmentPostgresService(assignmentId.toString())
        .then((response) => {
          if(response !== null && response !== undefined) {
            if(mounted){
              const responseData = response.data
              const parsedDate = parseISO(responseData.completionDatetime)
              setAssignment({
                ...responseData,
                completionDatetime: parsedDate,
              })
            }
          }
        })
        .catch((error) => {
          console.log("Error fetching assignment:", error)
        })
      dispatch(setIsLoading(false))

    }
    return () => {mounted = false}

  }, [assignmentId])
  return { assignment, setAssignment}
}