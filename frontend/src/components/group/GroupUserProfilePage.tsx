import { useParams } from "react-router-dom"
import AssignmentListElement from "../assignment/AssignmentListElement"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetUser } from "../customHooks/useGetUser"
import { useGetAssignmentsByGroupAndStudent } from "../customHooks/useGetAssignmentsByGroupAndStudent"
import {
  useGetAssignmentsWithEvaluationByGroupAndStudent
} from "../customHooks/useGetAssignmentsWithEvaluationByStudentAndGroup";
import {GetResultFromAssignmentsWithEvaluation} from "../../utils/GetResultFromAssignmentsWithEvaluation";

export default function GroupUserProfilePage() {
  const {userProfileId} = useParams()
  const group= useAppSelector(selectGroup)
  const userProfile = useGetUser(userProfileId as unknown as number)
  const doneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "done")
  const expiredUndoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "expiredUndone")
  const undoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "undone")
  const assignmentsWithEvaluation = useGetAssignmentsWithEvaluationByGroupAndStudent(group!.id, userProfileId as unknown as number)

  return (
      <div className='flex flex-col h-[calc(100dvh-332px)] overflow-y-hidden'>
        <p className='border-t border-t-main_blue ml-32 w-[495px] mt-0.5'></p>
        <ul className='flex flex-col gap-1 pt-2 pb-6 box-content overflow-y-auto'>
          <li className='ml-32 mb-1'><span className='font-semibold mr-1'>Imię i nazwisko: </span>{userProfile?.firstname} {userProfile?.lastname}</li>
          <li className='ml-32 mb-1'><span className='font-semibold mr-1'>Indeks: </span>{userProfile?.index}</li>
          <li>
            <dl>
              <dt >
                <p className='ml-32 underline underline-offset-2'>Zrobione zadania:</p>
                <dd>
                  <ul className='ml-12'>
                    {doneAssignments?.map((assignment) => {
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()}
                                                                             assignment={assignment}
                                                                             resultPoints={GetResultFromAssignmentsWithEvaluation(assignmentsWithEvaluation, assignment.id)}
                                                                             idGroup={group?.id as unknown as string}/></li>)
                    })}
                  </ul>
                </dd>
              </dt>
              <dt>
                <p className='ml-32 underline underline-offset-2 mt-4'>Niezrobione zadania:</p>
                <dd>
                  <ul className='ml-12'>
                    {undoneAssignments?.map((assignment) => {
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()}
                                                                             assignment={assignment}
                                                                             resultPoints={GetResultFromAssignmentsWithEvaluation(assignmentsWithEvaluation, assignment.id)}
                                                                             idGroup={group?.id as unknown as string}/></li>)
                    })}
                  </ul>
                </dd>
              </dt>
              <dt>
                <p className='ml-32 underline underline-offset-2 mt-4'>Spoźnione nieoddane zadania:</p>
                <dd>
                  <ul className='ml-12'>
                    {expiredUndoneAssignments?.map((assignment) => {
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()}
                                                                             resultPoints={GetResultFromAssignmentsWithEvaluation(assignmentsWithEvaluation, assignment.id)}
                                                                             assignment={assignment}
                                                                             idGroup={group?.id as unknown as string}/></li>)
                    })}
                  </ul>
                </dd>
              </dt>
            </dl>
          </li>
        </ul>
      </div>
  )
}