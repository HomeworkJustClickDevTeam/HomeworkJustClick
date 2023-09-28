import { useEffect, useState } from "react"
import { useAppDispatch } from "../../types/HooksRedux"
import { SolutionInterface } from "../../types/SolutionInterface"
import {
  getCheckedSolutionByUserAssignmentGroupPostgresService,
  getUncheckedSolutionByUserAssignmentGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetSolutionByUserAssignmentGroup = (userId: number|undefined|null, assignmentId: number|undefined|null, groupId:number|undefined|null, filter:"unchecked"|"checked") => {
  const [solution, setSolution] = useState<SolutionInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(userId !== undefined && assignmentId !== undefined && groupId !== undefined && userId !== null && assignmentId !== null && groupId !== null){
        dispatch(setIsLoading(true))
        try {
          if(filter==="unchecked"){
            response =  await getUncheckedSolutionByUserAssignmentGroupPostgresService(userId.toString(), assignmentId.toString(), groupId.toString())
          }
          else if(filter==="checked"){
            response = await getCheckedSolutionByUserAssignmentGroupPostgresService(userId.toString(), assignmentId.toString(), groupId.toString())
          }
          if(response !== null && response!==undefined){
            if(mounted){setSolution(response.data)}
          }
        }
        catch (e) {console.log(e)}
        dispatch(setIsLoading(false))
      }
    }
    let mounted = true
    fetchData()
    return () => {mounted = false}
  }, [userId,assignmentId,groupId,filter])

  return solution
}
