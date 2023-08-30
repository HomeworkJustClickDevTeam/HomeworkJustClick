import React, {useContext, useEffect, useState} from "react"
import {assigmentFilterStudent} from "../../filter/AssigmentFilterStudent"
import {useNavigate, useParams} from "react-router-dom"
import AssigmentListElement from "./AssigmentListElement"
import Loading from "../animations/Loading"
import groupRoleContext from "../../contexts/GroupRoleContext"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {getUser} from "../../services/otherServices";

function AssignmentsTypesPage({type}: { type: string }) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const {idGroup = ""} = useParams()
  const userState = getUser()
  const {role} = useContext(groupRoleContext)
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

    if (role === "Student") {
      typeOfAssigment()
    } else {
      navigate(`-/group/${idGroup}`)
    }
  }, [type])
  if (userState === undefined) {
    navigate("/")
  }
  const {doneAssignments, expiredAssignments, noneExpiredAssignments} =
    assigmentFilterStudent({
      idGroup: idGroup,
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
            <AssigmentListElement assignment={assignment} idGroup={idGroup}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsTypesPage
