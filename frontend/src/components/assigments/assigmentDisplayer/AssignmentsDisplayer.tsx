import { Link, useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { Assigment } from "../../../types/types"
import common_request from "../../../services/default-request-database"
import AssigmentItem from "./assigmentItem/AssigmentItem"

function AssignmentsDisplayer() {
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const [assignmentsVisible, setVisibleAssigments] = useState<Assigment[]>([])
  const { id = "" } = useParams<string>()

  useEffect(() => {
    common_request.get("/assignments/byGroupId/" + id).then((response) => {
      const assigmentFromServer = response.data as Assigment[]
      console.log(response.data)
      setAssignments(assigmentFromServer)
      setVisibleAssigments(
        assigmentFromServer.filter((assigment) => assigment.visible)
      )
    })
  }, [])

  return (
    <>
      <Link to={`/group/${id}/assignments/add`}>
        <button>Dodaj zadanie domowe</button>
      </Link>
      <ul>
        {assignmentsVisible.map((assigment) => (
          <li key={assigment.id}>
            <AssigmentItem assignment={assigment} idGroup={id} />{" "}
          </li>
        ))}
      </ul>
    </>
  )
}
export default AssignmentsDisplayer
