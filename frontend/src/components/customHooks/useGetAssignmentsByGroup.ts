import { useEffect, useState } from "react"
import { AssignmentModel } from "../../types/Assignment.model"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { selectRole } from "../../redux/roleSlice"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getAssignmentsByGroupPostgresService } from "../../services/postgresDatabaseServices"

export const useGetAssignmentsByGroup = (groupId: number|undefined|null) => {
  const [assignments, setAssignments] = useState<AssignmentModel[]>([])
  const dispatch = useAppDispatch()
  const role = useAppSelector(selectRole)

  useEffect(() => {
    let mounted = true
    if(groupId !== undefined && groupId !== null){
      dispatch(setIsLoading(true))
      getAssignmentsByGroupPostgresService(groupId.toString())
        .then((response)=> {
          const assignmentsFromServer = response.data as AssignmentModel[]
          if(response !== null && response !== undefined){
            if(role === "Teacher"){
              if(mounted){setAssignments(assignmentsFromServer)}
            }
            else {
              if(mounted){
                setAssignments(
                  assignmentsFromServer.filter((assignment)=>assignment.visible)
                )
              }
            }
          }
        })
        .catch((error)=> {
          if(error !== null && error !== undefined && error?.response.status === 404) {
            if(mounted){setAssignments([])}
          }
          else {console.log(error)}
        })
      dispatch(setIsLoading(false))
    }
    return () => {mounted = false}

  }, [role, groupId])
  return assignments
}