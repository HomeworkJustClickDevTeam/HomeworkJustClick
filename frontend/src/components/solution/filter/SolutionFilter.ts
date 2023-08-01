import {
  getExtendedSolutionsCheckedByGroupPostgresService,
  getExtendedSolutionsLateByGroupPostgresService,
  getExtendedSolutionsUncheckedByGroupPostgresService
} from "../../../services/postgresDatabaseServices"
import {SolutionExtendedInterface} from "../../../types/SolutionExtendedInterface";

interface SolutionTypesPropInterface {
  idGroup: string
  setSolutionsExtended: (solutionExtended: SolutionExtendedInterface[]) => void
}
export const solutionFilter = ({
  idGroup,
  setSolutionsExtended,
}: SolutionTypesPropInterface) => {
  const lateSolutions = (): void => {
    getExtendedSolutionsLateByGroupPostgresService(idGroup)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }

  const uncheckedSolutions = (): void => {
    getExtendedSolutionsUncheckedByGroupPostgresService(idGroup)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }

  const checkSolutions = (): void => {
    getExtendedSolutionsCheckedByGroupPostgresService(idGroup)
      .then((r) => setSolutionsExtended(r.data))
      .catch(() => setSolutionsExtended([]))
  }
  return {
    checkSolutions,
    uncheckedSolutions,
    lateSolutions,
  }
}
