import React from "react"
import AssignmentListElement from "./AssignmentListElement"
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
