import {
  getGroupsByStudentPostgresService,
  getGroupsByTeacherPostgresService,
  getGroupsByUserPostgresService
} from "../../../services/postgresDatabaseServices"
import {GroupInterface} from "../../../types/GroupInterface";

interface GroupFilterProps {
  setGroups: (groups: GroupInterface[] | undefined) => void
  setIsLoading: (loading: boolean) => void
  userId: string
}
export const groupFilter = ({ setGroups, setIsLoading, userId }: GroupFilterProps) => {

  const teacherUserGroups = (): void => {
    setIsLoading(true)
    getGroupsByTeacherPostgresService(userId)
      .then((response) => {
        const groups: GroupInterface[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
    setIsLoading(false)
  }

  const studentsUserGroups = (): void => {
    setIsLoading(true)
    getGroupsByStudentPostgresService(userId)
      .then((response) => {
        const groups: GroupInterface[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
    setIsLoading(false)
  }
  const allUserGroups = (): void => {
    setIsLoading(true)
    getGroupsByUserPostgresService(userId)
      .then((response) => {
        const groups: GroupInterface[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups(undefined))
    setIsLoading(false)
  }
  return {
    teacherUserGroups,
    studentsUserGroups,
    allUserGroups,
  }
}
