import { useEffect, useState } from "react"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getEvaluationBySolutionPostgresService,
  getEvaluationsByGroupPostgresService
} from "../../services/postgresDatabaseServices"
import {EvaluationModel} from "../../types/EvaluationExtendedModel";

export const useGetEvaluationsByGroup = (
  groupId: number | undefined | null
) => {
  const [evaluations, setEvaluations] = useState<EvaluationModel[]>(
    []
  )
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if (groupId !== undefined && groupId !== null) {
      dispatch(setIsLoading(true))
      getEvaluationsByGroupPostgresService(groupId.toString())
        .then((response) => {
          if (response !== null && response !== undefined) {
            if (mounted) {
              setEvaluations(response.data)
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
              setEvaluations([])
            }
          } else {
            console.log("Error fetching evaluations:", error)
          }
        })
      dispatch(setIsLoading(false))
    }
    return () => {
      mounted = false
    }
  }, [groupId])

  return evaluations
}
