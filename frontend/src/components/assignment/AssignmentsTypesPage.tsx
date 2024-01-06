import React, { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByGroupAndStudent } from "../customHooks/useGetAssignmentsByGroupAndStudent"
import { AssignmentsType } from "../../types/AssignmentsType"
import {
  useGetAssignmentsWithEvaluationByGroupAndStudent
} from "../customHooks/useGetAssignmentsWithEvaluationByStudentAndGroup";
import {GetResultFromAssignmentsWithEvaluation} from "../../utils/GetResultFromAssignmentsWithEvaluation";

function AssignmentsTypesPage({type}: { type: AssignmentsType }) {
  const navigate = useNavigate()
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const userState = useAppSelector(selectUserState)
  const assignments = useGetAssignmentsByGroupAndStudent(group?.id, userState?.id, type)
  const assignmentsWithEvaluation = useGetAssignmentsWithEvaluationByGroupAndStudent(group!.id, userState!.id)

  useEffect(() => {
    if (role !== "Student") {
      navigate(`-/group/${group?.id}`)
    }
  }, [type, role])
  return (
    <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden'>
      <ul className='flex flex-col box-content overflow-y-auto mb-4'>
        {assignments.map((assignment) => (
          <li key={assignment.id}>
            <AssignmentListElement
              assignment={assignment}
              resultPoints={GetResultFromAssignmentsWithEvaluation(assignmentsWithEvaluation, assignment.id)}
              idGroup={group?.id as unknown as string}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsTypesPage
