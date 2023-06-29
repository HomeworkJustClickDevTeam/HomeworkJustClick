import postgresqlDatabase from "../../../services/postgresDatabase"
import {GroupInterface} from "../../../types/GroupInterface";

interface GroupFilterProps {
  setGroups: (groups: GroupInterface[] | undefined) => void
  setIsLoading: (loading: boolean) => void
}
export const groupFilter = ({ setGroups, setIsLoading }: GroupFilterProps) => {
  const teacherUserGroups = (): void => {
    setIsLoading(true)
    postgresqlDatabase
      .get("/groups/byTeacher/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: GroupInterface[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
    setIsLoading(false)
  }

  const studentsUserGroups = (): void => {
    setIsLoading(true)
    postgresqlDatabase
      .get("/groups/byStudent/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: GroupInterface[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
    setIsLoading(false)
  }
  const allUserGroups = (): void => {
    setIsLoading(true)
    postgresqlDatabase
      .get("/groups/byUser/" + localStorage.getItem("id"))
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
