import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import AssignmentListElement from "../assignments/AssignmentListElement";
import {useState} from "react";
import {AssignmentInterface} from "../../types/AssignmentInterface";

export const EvaluationsStudentPage = () =>{
  const group = useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group!.id)
  const [unfoldedPieChartAssignment, setUnfoldedPieChartAssignment] = useState<AssignmentInterface|undefined>(undefined)

  const handleAssignmentClick = (clickedAssignment: AssignmentInterface) =>{
    if(clickedAssignment.id === unfoldedPieChartAssignment?.id) setUnfoldedPieChartAssignment(undefined)
    else setUnfoldedPieChartAssignment(clickedAssignment)
  }

  return (
      <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden'>
    <div className='flex flex-col box-content overflow-y-auto'>
      {assignments.map((assignment)=>{
        return <AssignmentListElement assignment={assignment} key={assignment.id} unfoldedPieChartAssignment={unfoldedPieChartAssignment} handleAssignmentClick={handleAssignmentClick}/>
      })}
   </div>
      </div>)
}