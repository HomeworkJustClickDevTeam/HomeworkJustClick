import { useEffect, useState } from "react"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getExtendedSolutionsCheckedByGroupPostgresService,
  getExtendedSolutionsLateByGroupPostgresService,
  getExtendedSolutionsUncheckedByGroupPostgresService,
  getSolutionsCheckedByGroupPostgresService, getSolutionsLateByGroupPostgresService,
  getSolutionsUncheckedByGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { SolutionExtendedInterface } from "../../types/SolutionExtendedInterface"
import { ExtendedSolutionType } from "../../types/ExtendedSolutionType"
import {SolutionInterface} from "../../types/SolutionInterface";

export const useGetSolutionsByGroup = (groupId: number|undefined|null, filter: ExtendedSolutionType, extended: boolean) => {
  const [solutions, setSolutions] = useState<SolutionExtendedInterface[]|SolutionInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () =>{
      let response = null
      if(groupId !== undefined && groupId !== null){

        try {
          switch(filter){
            case "checked":
              response = extended ? await getExtendedSolutionsCheckedByGroupPostgresService(groupId.toString())
                : await getSolutionsCheckedByGroupPostgresService(groupId.toString())
              break
            case "unchecked":
              response = extended ? await getExtendedSolutionsUncheckedByGroupPostgresService(groupId.toString())
                : await getSolutionsUncheckedByGroupPostgresService(groupId.toString())
              break
            case "late":
              response = extended ? await getExtendedSolutionsLateByGroupPostgresService(groupId.toString())
                : await getSolutionsLateByGroupPostgresService(groupId.toString())
              break
          }
          if(response !== null && response!==undefined) {
            if(mounted){setSolutions(response.data)}
          }
        }catch (error:any) {
          if(error !== null && error!== undefined && error.response.status === 404){
            if(mounted){setSolutions([])}
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
  }, [groupId, filter])

  return extended ? solutions as SolutionExtendedInterface[] : solutions as SolutionInterface[]
}