import postgresqlDatabase from "../../../services/postgresDatabase"
import { Group, PropsForFiltering } from "../../../types/types"

export const groupFilter = ({ setGroups }: PropsForFiltering) => {
  const teacherUserGroups = (): void => {
    postgresqlDatabase
      .get("/groups/byTeacher/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
  }

  const studentsUserGroups = (): void => {
    postgresqlDatabase
      .get("/groups/byStudent/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
  }
  const allUserGroups = (): void => {
    postgresqlDatabase
      .get("/groups/byUser/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch(() => setGroups([]))
  }
  return {
    teacherUserGroups,
    studentsUserGroups,
    allUserGroups,
  }
}
