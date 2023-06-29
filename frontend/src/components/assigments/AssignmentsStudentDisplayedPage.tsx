import { useContext, useEffect, useState } from "react"
import { Assigment } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import UserContext from "../../UserContext"
import AssigmentListElement from "./AssigmentListElement"

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const { userState } = useContext(UserContext)
  useEffect(() => {
    postgresqlDatabase
      .get("/assignments/byStudent/" + userState.userId)
      .then((response) => {
        const assigmentFromServer = response.data as Assigment[]
        console.log(response.data)
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
