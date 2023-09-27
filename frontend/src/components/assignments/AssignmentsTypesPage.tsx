import React, { useEffect} from "react"
import { useNavigate } from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByGroupAndStudent } from "../customHooks/useGetAssignmentsByGroupAndStudent"
import { AssignmentsType } from "../../types/AssignmentsType"

function AssignmentsTypesPage({type}: { type: AssignmentsType }) {
  const navigate = useNavigate()
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const userState = useAppSelector(selectUserState)
  const assignments = useGetAssignmentsByGroupAndStudent(group?.id, userState?.id, type)

  useEffect(() => {
    if (role !== "Student") {
      navigate(`-/group/${group?.id}`)
    }
  }, [type])
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
