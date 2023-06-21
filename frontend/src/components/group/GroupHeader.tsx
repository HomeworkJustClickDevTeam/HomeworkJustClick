import { Link } from "react-router-dom"
import { useContext } from "react"
import GroupContext from "../../GroupRoleContext"
import {PropsForGroupItem} from "../../types/types";



function GroupHeader({ group }: PropsForGroupItem) {
  const { role } = useContext(GroupContext)
  return (
    <div>
        <p>{group.name}</p>
        <p>{group.description}</p>
      <header>
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
    </div>
  )
}
export default GroupHeader
