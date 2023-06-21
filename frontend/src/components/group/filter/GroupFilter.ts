import postgresqlDatabase from "../../../services/postgresDatabase"
import { GroupType, PropsForFiltering } from "../../../types/types"

export const groupFilter = ({ setGroups, setIsLoading }: PropsForFiltering) => {
  const teacherUserGroups = (): void => {
    setIsLoading(true)
    postgresqlDatabase
      .get("/groups/byTeacher/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: GroupType[] = response.data
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
        const groups: GroupType[] = response.data
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
        const groups: GroupType[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
    setIsLoading(false)
  }
  return {
    teacherUserGroups,
    studentsUserGroups,
    allUserGroups,
  }
}
