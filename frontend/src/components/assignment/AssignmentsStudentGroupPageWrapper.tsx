import {selectGroup} from "../../redux/groupSlice";
import {useSelector} from "react-redux";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {AssignmentsDisplayer} from "./AssignmentsDisplayer";
import {useGetAssignmentsByStudent} from "../customHooks/useGetAssignmentsByStudent";
import {useGetAssignmentsByGroupAndStudent} from "../customHooks/useGetAssignmentsByGroupAndStudent";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {AssignmentsViewPage} from "./AssignmentsViewPage";

export const AssignmentsStudentGroupPageWrapper = () =>{
  const group = useAppSelector(selectGroup)
  const user = useAppSelector(selectUserState)
  const doneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, user!.id, 'done')
  const expiredUndoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, user!.id, 'expiredUndone')
  const nonExpiredUndoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, user!.id, 'nonExpiredUndone')


  return <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden'>
    <AssignmentsViewPage expiredUndoneAssignments={expiredUndoneAssignments}
                         nonExpiredUndoneAssignments={nonExpiredUndoneAssignments}
                         doneAssignments={doneAssignments}
                         backgroundColor={""}/>
  </div>
}