import { useEffect, useState } from "react"
import { Table } from "../../types/Table.model"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getEvaluationTableByUserIdService } from "../../services/postgresDatabaseServices"

export const useGetEvaluationTable = (userId: number | undefined | null) => {
  const [evaluationTable, setEvaluationTable] = useState<Table[]>()
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if (userId !== undefined && userId !== null) {
      dispatch(setIsLoading(true))
      getEvaluationTableByUserIdService(userId)
        .then((response) => {
          if (response !== null && response !== undefined) {
            if (mounted) {
              setEvaluationTable(response.data.content)
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
              setEvaluationTable([])
            }
          } else {
            console.log("Error fetching user:", error)
          }
        })
      dispatch(setIsLoading(false))
    }
    return () => {
      mounted = false
    }
  }, [userId])

  return { evaluationTable, setEvaluationTable } as const
}
