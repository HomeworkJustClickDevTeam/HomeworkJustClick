import { useEffect, useState } from "react"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getEvaluationBySolutionPostgresService } from "../../services/postgresDatabaseServices"
import { EvaluationExtendedModel } from "../../types/EvaluationExtendedModel"

export const useGetEvaluationBySolution = (
  solutionId: number | undefined | null
) => {
  const [evaluation, setEvaluation] = useState<EvaluationExtendedModel | undefined>(
    undefined
  )
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if (solutionId !== undefined && solutionId !== null) {
      dispatch(setIsLoading(true))
      getEvaluationBySolutionPostgresService(solutionId.toString())
        .then((response) => {
          if (response !== null && response !== undefined) {
            if (mounted) {
              setEvaluation(response.data)
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
              setEvaluation(undefined)
            }
          } else {
            console.log("Error fetching evaluation:", error)
          }
        })
      dispatch(setIsLoading(false))
    }
    return () => {
      mounted = false
    }
  }, [solutionId])

  return evaluation
}
