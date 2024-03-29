import { useEffect, useState } from "react"
import { AssignmentModel } from "../../types/Assignment.model"
import { useAppDispatch } from "../../types/HooksRedux"
import { getAssignmentPostgresService } from "../../services/postgresDatabaseServices"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { parseISO } from "date-fns"

export const useGetAssignment = (assignmentId: number | undefined | null) => {
  const [assignment, setAssignment] = useState<AssignmentModel | undefined>(
    undefined
  )
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if (assignmentId !== undefined && assignmentId !== null) {
      dispatch(setIsLoading(true))
      getAssignmentPostgresService(assignmentId.toString())
        .then((response) => {
          if (response !== null && response !== undefined) {
            if (mounted) {
              const responseData = response.data
              const parsedDate = responseData.completionDatetime
              setAssignment({
                ...responseData,
                completionDatetime: parsedDate,
              })
            }
          }
        })
        .catch((error) => {
          if (
            error !== null &&
            error !== undefined &&
            error.response.status === 404
          ) {
            if (mounted) {
              setAssignment(undefined)
            }
          } else {
            console.log("Error fetching assignment:", error)
          }
        })
      dispatch(setIsLoading(false))
    }
    return () => {
      mounted = false
    }
  }, [assignmentId])
  return { assignment, setAssignment }
}
