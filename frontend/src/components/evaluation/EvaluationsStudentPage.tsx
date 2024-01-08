import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import AssignmentListElement from "../assignment/AssignmentListElement";
import {useState} from "react";
import {AssignmentModel} from "../../types/Assignment.model";
import {useGetAssignmentsWithEvaluationByStudent} from "../customHooks/useGetAssignmentsWithEvaluationByStudent";
import {selectUserState} from "../../redux/userStateSlice";
import {GetResultFromAssignmentsWithEvaluation} from "../../utils/GetResultFromAssignmentsWithEvaluation";

export const EvaluationsStudentPage = () =>{
  const group = useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group!.id)
  const [unfoldedPieChartAssignment, setUnfoldedPieChartAssignment] = useState<AssignmentModel|undefined>(undefined)

  const user = useAppSelector(selectUserState)
  const assignmentsWithEvaluation = useGetAssignmentsWithEvaluationByStudent(user!.id)
  const handleAssignmentClick = (clickedAssignment: AssignmentModel) =>{
    if(clickedAssignment.id === unfoldedPieChartAssignment?.id) setUnfoldedPieChartAssignment(undefined)
    else setUnfoldedPieChartAssignment(clickedAssignment)
  }

  return (
      <div className='flex flex-col h-[calc(100dvh-325px)] overflow-y-hidden'>
    <div className='flex flex-col box-content overflow-y-auto'>
      {assignments.map((assignment)=>{
        return <AssignmentListElement
          assignment={assignment}
          key={assignment.id}
          resultPoints={GetResultFromAssignmentsWithEvaluation(assignmentsWithEvaluation, assignment.id)}
          unfoldedPieChartAssignment={unfoldedPieChartAssignment}
          handleAssignmentClick={handleAssignmentClick}/>
      })}
   </div>
      </div>)
}