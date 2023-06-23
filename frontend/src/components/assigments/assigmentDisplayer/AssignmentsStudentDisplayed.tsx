import { useContext, useEffect, useState } from "react"
import { Assigment } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"
import UserContext from "../../../UserContext"
import AssigmentItem from "./assigmentItem/AssigmentItem"

export default function AssignmentsStudentDisplayed() {
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
            <AssigmentItem
              assignment={assigment}
              idGroup={`${assigment.groupId}`}
            />
          </li>
        ))}
      </ul>
    </div>
  )
}
