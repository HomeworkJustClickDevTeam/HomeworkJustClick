import { useEffect, useState } from "react"
import { useAppDispatch } from "../../types/HooksRedux"
import { SolutionInterface } from "../../types/SolutionInterface"
import {
  getCheckedSolutionByUserAssignmentGroupPostgresService,
  getUncheckedSolutionByUserAssignmentGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetSolutionByUserAssignmentGroup = (userId: number|undefined, assignmentId: number|undefined, groupId:number|undefined, type:"unchecked"|"checked") => {
  const [solution, setSolution] = useState<SolutionInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(userId !== undefined && assignmentId !== undefined && groupId !== undefined){
        dispatch(setIsLoading(true))
        try {
          if(type==="unchecked"){
            response =  await getUncheckedSolutionByUserAssignmentGroupPostgresService(userId.toString(), assignmentId.toString(), groupId.toString())
          }
          else if(type==="checked"){
            response = await getCheckedSolutionByUserAssignmentGroupPostgresService(userId.toString(), assignmentId.toString(), groupId.toString())
          }
          if(response !== null && response!=undefined){
            if(mounted){setSolution(response.data)}
          }
        }
        catch (e) {console.log(e)}
      }
    }
    let mounted = true
    fetchData()
    dispatch(setIsLoading(false))
    return () => {mounted = false}
  }, [userId,assignmentId,groupId,type])

  return solution
}
