import React, { useEffect, useState } from "react"
import { getAssignmentsByStudentPostgresService } from "../../services/postgresDatabaseServices"
import AssignmentListElement from "./AssignmentListElement"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { useNavigate } from "react-router-dom"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByStudent } from "../customHooks/useGetAssignmentsByStudent"

export default function AssignmentsStudentDisplayedPage() {
  const userState = useAppSelector(selectUserState)
  const assignments = useGetAssignmentsByStudent(userState?.id)

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
