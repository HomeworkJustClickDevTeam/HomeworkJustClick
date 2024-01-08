import {selectGroup} from "../../redux/groupSlice";
import {useSelector} from "react-redux";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {AssignmentsDisplayer} from "./AssignmentsDisplayer";

export const AssignmentsStudentGroupPage = () =>{
  const group = useSelector(selectGroup)
  const groupAssignments = useGetAssignmentsByGroup(group?.id)

  return <div className='flex flex-col h-[calc(100dvh-325px)] overflow-y-hidden'>
    <AssignmentsDisplayer assignments={groupAssignments}/>
  </div>
}