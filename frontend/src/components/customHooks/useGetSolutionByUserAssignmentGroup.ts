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
        }catch (error:any) {
          if(error !== null && error!== undefined && error.response.status === 404){
            if(mounted){setSolution(undefined)}
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
  }, [userId,assignmentId,groupId,filter])

  return solution
}
