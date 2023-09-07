import React, { useContext, useEffect, useState } from "react"
import { assignmentFilterStudent } from "../../filter/AssignmentFilterStudent"
import { useNavigate } from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import Loading from "../animations/Loading"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useSelector } from "react-redux"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { selectUserState } from "../../redux/userStateSlice"

function AssignmentsTypesPage({type}: { type: string }) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const navigate = useNavigate()
  const group= useSelector(selectGroup)
  const role = useSelector(selectRole)
  const userState = useSelector(selectUserState)

  useEffect(() => {
    function typeOfAssignment() {
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
      typeOfAssignment()
    } else {
      navigate(`-/group/${group?.id}`)
    }
  }, [type])
  const {doneAssignments, expiredAssignments, noneExpiredAssignments} =
    assignmentFilterStudent({
      idGroup: group?.id as unknown as string,
      userId: userState?.id as unknown as string,
      setAssignments: setAssignments,
    })
  if (isLoading) {
    return (
      <>
        <Loading/>
      </>
    )
  }
  return (
    <div>
      <ul>
        {assignments.map((assignment) => (
          <li key={assignment.id}>
            <AssignmentListElement assignment={assignment} idGroup={group?.id as unknown as string}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsTypesPage
