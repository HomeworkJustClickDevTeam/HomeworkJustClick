import React, {useContext, useEffect, useState} from "react"
import {getAssignmentsByStudentPostgresService} from "../../services/postgresDatabaseServices"
import AssignmentListElement from "./AssignmentListElement"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {getUser} from "../../services/otherServices";
import {useNavigate} from "react-router-dom";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const {applicationState} = useContext(ApplicationStateContext)
  const navigate = useNavigate()

  useEffect(() => {
    if (applicationState?.userState !== undefined) {
      getAssignmentsByStudentPostgresService(applicationState.userState.id.toString())
        .then((response) => {
          const assignmentFromServer = response.data as AssignmentInterface[]
          setAssignments(assignmentFromServer)
        })
    }
  }, [])
  if (applicationState?.userState === undefined) {
    navigate("/")
  }
  return (
    <div>
      <ul>
        {assignments.map((assignment) => (
          <li key={assignment.id}>
            <AssignmentListElement
              assignment={assignment}
              idGroup={`${assignment.groupId}`}
            />
          </li>
        ))}
      </ul>
    </div>
  )
}
