import {selectGroup} from "../../redux/groupSlice";
import {useSelector} from "react-redux";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {AssignmentsDisplayer} from "./AssignmentsDisplayer";

export const AssignmentsStudentGroupPage = () =>{
  const group = useSelector(selectGroup)
  const groupAssignments = useGetAssignmentsByGroup(group?.id)

  return <>
    <AssignmentsDisplayer assignments={groupAssignments}/>
  </>
}