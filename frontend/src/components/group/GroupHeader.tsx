import { Link } from "react-router-dom"
import { useContext } from "react"
import GroupContext from "../../GroupRoleContext"
import { GroupType } from "../../types/types"

function GroupHeader(props: { group: GroupType }) {
  const { role } = useContext(GroupContext)

  return (
    <>
      <header>
        <a>{props.group.name}</a>
        <Link to="assignments"> Zadania</Link>
        <Link to="users"> Osoby </Link>

        {role === "Student" && <Link to="#">Oceny</Link>}
        {role === "Student" ? (
          <Link to="assignments/todo">Do wykonania</Link>
        ) : (
          <Link to="solutions/uncheck">Do sprawdzenia</Link>
        )}
        {role === "Student" ? (
          <Link to="assignments/done">Zrobione</Link>
        ) : (
          <Link to="solutions/check">Sprawdzone</Link>
        )}
        {role === "Student" ? (
          <Link to="assignments/expired">Zalagle</Link>
        ) : (
          <Link to="solutions/late">Spóznione</Link>
        )}
        {role === "Teacher" && <Link to="settings">Ustawienia</Link>}
      </header>
    </>
  )
}
export default GroupHeader
