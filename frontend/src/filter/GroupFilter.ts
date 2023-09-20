import {
  getGroupsByStudentPostgresService,
  getGroupsByTeacherPostgresService,
  getGroupsByUserPostgresService
} from "../services/postgresDatabaseServices"
import { GroupInterface } from "../types/GroupInterface"
import { AppDispatch } from "../redux/store"

interface GroupFilterProps {
  setGroups: (groups: GroupInterface[] | undefined) => void
  setIsLoading: (isLoading:boolean) => void
  userId: string
}

export const groupFilter = ({setGroups, setIsLoading, userId}: GroupFilterProps) => {

  const studentsUserGroups = (): void => {

  }
  const allUserGroups = (): void => {
    setIsLoading(true)

    setIsLoading(false)
  }
  return {
    studentsUserGroups,
    allUserGroups,
  }
}
