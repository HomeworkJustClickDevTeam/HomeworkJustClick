import { Assigment, PropsForType } from "../../types/types"
import { useContext, useEffect, useState } from "react"
import { assigmentFilterStudent } from "./filter/AssigmentFilterStudent"
import { useNavigate, useParams } from "react-router-dom"
import userContext from "../../UserContext"
import AssigmentItem from "./assigmentDisplayer/assigmentItem/AssigmentItem"
import Loading from "../animations/Loading"
import groupRoleContext from "../../GroupRoleContext"

function AssignmentsTypes({ type }: PropsForType) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const { id = "" } = useParams()
  const { userState } = useContext(userContext)
  const { role } = useContext(groupRoleContext)
  const navigate = useNavigate()
  const { doneAssignments, expiredAssignments, noneExpiredAssignments } =
    assigmentFilterStudent({
      id: id,
      userId: userState.userId,
      setAssignments: setAssignments,
    })

  useEffect(() => {
    function typeOfAssigment() {
      switch (type) {
        case "expired":
          expiredAssignments()
          setIsLoading(false)
          break
        case "done":
          doneAssignments()
          setIsLoading(false)
          break
        case "todo":
          noneExpiredAssignments()
          setIsLoading(false)
          break
        default:
          break
      }
    }
    if (role === "Student") {
      typeOfAssigment()
    } else {
      navigate(`-/group/${id}`)
    }
  }, [type])
  if (isLoading) {
    return (
      <>
        <Loading />
      </>
    )
  }
  return (
    <>
      <ul>
        {assignments.map((assignment) => (
          <li key={assignment.id}>
            <AssigmentItem assignment={assignment} idGroup={id} />{" "}
          </li>
        ))}
      </ul>
    </>
  )
}
export default AssignmentsTypes
