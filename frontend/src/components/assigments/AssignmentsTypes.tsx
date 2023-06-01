import { Assigment, PropsForType } from "../../types/types"
import { useContext, useEffect, useState } from "react"
import { assigmentFilterStudent } from "./filter/AssigmentFilterStudent"
import { useParams } from "react-router-dom"
import userContext from "../../UserContext"
import AssigmentItem from "./assigmentDisplayer/assigmentItem/AssigmentItem"
import Loading from "../animations/Loading"

function AssignmentsTypes({ type }: PropsForType) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const { id = "" } = useParams()
  const { userState } = useContext(userContext)
  const { doneAssignments, expiredAssignments, noneExpiredAssignments } =
    assigmentFilterStudent({
      id: id,
      userId: userState.userId,
      setAssignments: setAssignments,
    })
  useEffect(() => {
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
  }, [assignments])
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
        {assignments.map((assigment) => (
          <li key={assigment.id}>
            <AssigmentItem assignment={assigment} idGroup={id} />{" "}
          </li>
        ))}
      </ul>
    </>
  )
}
export default AssignmentsTypes
