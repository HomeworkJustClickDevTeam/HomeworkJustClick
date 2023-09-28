import { useEffect, useState } from "react"
import { SolutionInterface } from "../../types/SolutionInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getExtendedSolutionsCheckedByGroupPostgresService, getExtendedSolutionsLateByGroupPostgresService,
  getExtendedSolutionsUncheckedByGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { SolutionExtendedInterface } from "../../types/SolutionExtendedInterface"
import { ExtendedSolutionType } from "../../types/ExtendedSolutionType"

export const useGetExtendedSolutionsByGroup = (groupId: number|undefined, filter: ExtendedSolutionType) => {
  const [solutions, setSolutions] = useState<SolutionExtendedInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () =>{
      let response = null
      if(groupId !== undefined){
        dispatch(setIsLoading(true))
        try {
          if(filter==="checked"){
            response = await getExtendedSolutionsCheckedByGroupPostgresService(groupId.toString())
          }
          else if(filter==="unchecked"){
            response = await getExtendedSolutionsUncheckedByGroupPostgresService(groupId.toString())
          }
          else if(filter==="late"){
            response = await getExtendedSolutionsLateByGroupPostgresService(groupId.toString())
          }
          if(response !== null && response!==undefined) {
            if(mounted){setSolutions(response.data)}
          }
          }catch (e){console.log(e)}
        dispatch(setIsLoading(false))
      }
    }
    let mounted = true
    fetchData()
    return () => {mounted = false}
  }, [groupId, filter])
  return solutions
}