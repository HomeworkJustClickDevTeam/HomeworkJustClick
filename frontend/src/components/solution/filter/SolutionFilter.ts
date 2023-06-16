import { SolutionTypesProp } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"

export const solutionFilter = ({
  id,
  setSolutionsExtended,
}: SolutionTypesProp) => {
  const lateSolutions = (): void => {
    postgresqlDatabase
      .get(`/extended/solutions/lateByGroup/${id}`)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }

  const uncheckedSolutions = (): void => {
    postgresqlDatabase
      .get(`extended/solutions/uncheckedByGroup/${id}`)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }

  const checkSolutions = (): void => {
    postgresqlDatabase
      .get(`/extended/solutions/checkedByGroup/${id}`)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }
  return {
    checkSolutions,
    uncheckedSolutions,
    lateSolutions,
  }
}
