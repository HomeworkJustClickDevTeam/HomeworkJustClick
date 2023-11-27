import { useState } from "react"
import { Table } from "../../types/Table.model"

export const useGetEvaluationTable = (userId: number | undefined | null) => {
  const [evaluationTable, SetEvaluationTable] = useState<Table[]>()

  return [evaluationTable, SetEvaluationTable]
}
