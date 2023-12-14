import React from "react"
import AssignmentListElement from "./AssignmentListElement"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByStudent } from "../customHooks/useGetAssignmentsByStudent"
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {selectGroup} from "../../redux/groupSlice";


export default function AssignmentsStudentDisplayedPage({allAssignments}:{allAssignments:boolean}) {
  const userState = useAppSelector(selectUserState)
  const group = useAppSelector(selectGroup)
  const userAssignments = useGetAssignmentsByStudent(userState?.id)
  const groupAssignments = useGetAssignmentsByGroup(group?.id)

  return (
    <div>
      <ul>
        {(allAssignments ? userAssignments : groupAssignments).map((assignment) => (
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
