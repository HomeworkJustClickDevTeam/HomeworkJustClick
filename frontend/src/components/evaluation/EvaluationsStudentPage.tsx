import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import AssignmentListElement from "../assignments/AssignmentListElement";

export const EvaluationsStudentPage = () =>{
  const group = useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group!.id)
  return (
    <div>
      {assignments.map((assignment)=>{
        return <AssignmentListElement assignment={assignment} key={assignment.id}/>
      })}
   </div>)
}