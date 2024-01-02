import {AssignmentInterface} from "../../types/AssignmentInterface";
import AssignmentListElement from "./AssignmentListElement";
import React from "react";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {
  useGetAssignmentsWithEvaluationByGroupAndStudent
} from "../customHooks/useGetAssignmentsWithEvaluationByStudentAndGroup";
import {selectUserState} from "../../redux/userStateSlice";
import {useGetAssignmentsByStudent} from "../customHooks/useGetAssignmentsByStudent";
import {GetResultFromAssignmentsWithEvaluation} from "../../utils/GetResultFromAssignmentsWithEvaluation";
import {useGetAssignmentsWithEvaluationByStudent} from "../customHooks/useGetAssignmentsWithEvaluationByStudent";

export const AssignmentsDisplayer = ({assignments}:{assignments:AssignmentInterface[]}) =>{
  const group = useAppSelector(selectGroup)
  const user = useAppSelector(selectUserState)
  const groupAssignmentsWithEvaluation = useGetAssignmentsWithEvaluationByGroupAndStudent(group?.id, user!.id)
  const assignmentsWithEvaluation = useGetAssignmentsWithEvaluationByStudent(user!.id)
  return (
    <ul>
      {assignments.map(assignment=> (
        <li key={assignment.id}>
          <AssignmentListElement
            assignment={assignment}
            resultPoints={GetResultFromAssignmentsWithEvaluation(group === null ?
              assignmentsWithEvaluation : groupAssignmentsWithEvaluation,
              assignment.id)}
            idGroup={`${assignment.groupId}`}
          />
        </li>
      ))}
    </ul>
  )
}