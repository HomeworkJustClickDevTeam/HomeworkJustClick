import { useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import UserContext from "../../UserContext"
import AssigmentListElement from "./AssigmentListElement"
import {AssignmentInterface} from "../../types/AssignmentInterface";

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const { userState } = useContext(UserContext)
  useEffect(() => {
    postgresqlDatabase
      .get("/assignments/byStudent/" + userState.userId)
      .then((response) => {
        const assigmentFromServer = response.data as AssignmentInterface[]
        setAssignments(assigmentFromServer)
      })
  }, [])
  return (
    <div>
      <ul>
        {assignments.map((assigment) => (
          <li key={assigment.id}>
            <AssigmentListElement
              assignment={assigment}
              idGroup={`${assigment.groupId}`}
            />
          </li>
        ))}
      </ul>
    </div>
  )
}
