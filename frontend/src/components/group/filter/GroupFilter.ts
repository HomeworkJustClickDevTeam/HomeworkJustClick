import common_request from "../../../services/default-request-database"
import { Group, PropsForFiltering } from "../../../types/types"

export const groupFilter = ({ setGroups }: PropsForFiltering) => {
  const teacherUserGroups = (): void => {
    common_request
      .get("/groups/byTeacher/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch((e) => console.log(e))
  }

  const studentsUserGroups = (): void => {
    common_request
      .get("/groups/byStudent/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch((e) => console.log(e))
  }
  const allUserGroups = (): void => {
    common_request
      .get("/groups/byUser/" + localStorage.getItem("id"))
      .then((response) => {
        const groups: Group[] = response.data
        setGroups(groups)
      })
      .catch((e) => console.log(e))
  }
  return {
    teacherUserGroups,
    studentsUserGroups,
    allUserGroups,
  }
}