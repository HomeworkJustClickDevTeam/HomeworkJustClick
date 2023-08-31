import React, {useContext, useEffect, useState} from "react"
import {assigmentFilterStudent} from "../../filter/AssigmentFilterStudent"
import {useNavigate, useParams} from "react-router-dom"
import AssigmentListElement from "./AssigmentListElement"
import Loading from "../animations/Loading"
import groupRoleContext from "../../contexts/ApplicationStateContext"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {getUser} from "../../services/otherServices";

function AssignmentsTypesPage({type}: { type: string }) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const {applicationState} = useContext(groupRoleContext)
  const navigate = useNavigate()


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

    if (applicationState?.role === "Student") {
      typeOfAssigment()
    } else {
      navigate(`-/group/${applicationState?.group?.id}`)
    }
  }, [type])
  if (applicationState?.userState === undefined) {
    navigate("/")
  }
  const {doneAssignments, expiredAssignments, noneExpiredAssignments} =
    assigmentFilterStudent({
      idGroup: applicationState?.group?.id as unknown as string,
      userId: applicationState?.userState?.id as unknown as string,
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
            <AssigmentListElement assignment={assignment} idGroup={applicationState?.group?.id as unknown as string}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsTypesPage
