import { useEffect, useState } from "react"
import { UserInterface } from "../../types/UserInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getEvaluationBySolutionPostgresService, getUserPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { EvaluationInterface } from "../../types/EvaluationInterface"

export const useGetEvaluationBySolution = (solutionId: number|undefined)=>{
  const [evaluation, setEvaluation] = useState<EvaluationInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(solutionId!== undefined){
      dispatch(setIsLoading(true))
      getEvaluationBySolutionPostgresService(solutionId.toString())
        .then((response) =>
        {
          if(response !== null && response !== undefined){
            if(mounted){
              setEvaluation(response.data)
            }
          }
        })
        .catch((error: AxiosError) => console.log(error))
      dispatch(setIsLoading(false))
    }
    return () => {mounted = false}

  }, [solutionId])

  return evaluation
}