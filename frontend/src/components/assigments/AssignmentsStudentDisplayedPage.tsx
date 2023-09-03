import React, {useContext, useEffect, useState} from "react"
import {getAssignmentsByStudentPostgresService} from "../../services/postgresDatabaseServices"
import AssigmentListElement from "./AssigmentListElement"
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
          const assigmentFromServer = response.data as AssignmentInterface[]
          setAssignments(assigmentFromServer)
        })
    }
  }, [])
  if (applicationState?.userState === undefined) {
    navigate("/")
  }
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
