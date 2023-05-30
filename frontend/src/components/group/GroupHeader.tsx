import { Link } from "react-router-dom"
import { useContext } from "react"
import GroupContext from "../../GroupContext"

function GroupHeader() {
  const { role } = useContext(GroupContext)
  return (
    <>
      <header>
        <Link to="assignments"> Zadania</Link>
        <Link to="users"> Osoby </Link>
        {role === "Student" && <Link to="#">Oceny</Link>}
        {role === "Student" ? (
          <Link to="assignments">Do wykonania</Link>
        ) : (
          <Link to="assignments">Do sprawdzenia</Link>
        )}
        <Link to="assignments">Zrobione</Link>
        <Link to="assignments">Zalagle</Link>
      </header>
    </>
  )
}
export default GroupHeader
