import { Link, useParams } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import { Assigment } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"
import AssigmentItem from "./assigmentItem/AssigmentItem"
import Loading from "../../animations/Loading"
import GroupRoleContext from "../../../GroupRoleContext"

function AssignmentsGroupDisplayed() {
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const { id = "" } = useParams<string>()
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const { role } = useContext(GroupRoleContext)

  useEffect(() => {
    postgresqlDatabase.get("/assignments/byGroupId/" + id).then((response) => {
      const assigmentFromServer = response.data as Assigment[]
      console.log(response.data)
      if (role === "Teacher") {
        setAssignments(assigmentFromServer)
      } else {
        setAssignments(
          assigmentFromServer.filter((assigment) => assigment.visible)
        )
      }
      setIsLoading(false)
    })
  }, [])

  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      <Link to={`/group/${id}/assignments/add`}>
        <button>Dodaj zadanie domowe</button>
      </Link>
      <ul>
        {assignments.map((assigment) => (
          <li key={assigment.id}>
            <AssigmentItem assignment={assigment} idGroup={id} />{" "}
          </li>
        ))}
      </ul>
    </>
  )
}
export default AssignmentsGroupDisplayed
