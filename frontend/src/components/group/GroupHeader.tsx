import { Link } from "react-router-dom"
import { useContext } from "react"
import GroupContext from "../../GroupRoleContext"

function GroupHeader() {
  const { role } = useContext(GroupContext)
  return (
    <>
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
      </header>
    </>
  )
}
export default GroupHeader
