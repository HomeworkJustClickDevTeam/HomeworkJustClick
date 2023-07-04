import postgresqlDatabase from "../../../services/postgresDatabase"
import {SolutionExtendedInterface} from "../../../types/SolutionExtendedInterface";

interface SolutionTypesPropInterface {
  id: string
  setSolutionsExtended: (solutionExtended: SolutionExtendedInterface[]) => void
}
export const solutionFilter = ({
  id,
  setSolutionsExtended,
}: SolutionTypesPropInterface) => {
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
