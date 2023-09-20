import React, { useContext, useEffect, useState } from "react"
import { getAssignmentsByStudentPostgresService } from "../../services/postgresDatabaseServices"
import AssignmentListElement from "./AssignmentListElement"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useNavigate } from "react-router-dom"
import { selectUserState } from "../../redux/userStateSlice"
import { useSelector } from "react-redux"
import { useAppSelector } from "../../types/HooksRedux"

export default function AssignmentsStudentDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)

  useEffect(() => {
    if (userState !== null) {
      getAssignmentsByStudentPostgresService(userState.id.toString())
        .then((response) => {
          const assignmentFromServer = response.data as AssignmentInterface[]
          setAssignments(assignmentFromServer)
        })
    }
  }, [])

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
