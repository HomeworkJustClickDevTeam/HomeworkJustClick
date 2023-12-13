import { useParams } from "react-router-dom"
import AssignmentListElement from "../assignments/AssignmentListElement"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetUser } from "../customHooks/useGetUser"
import { useGetAssignmentsByGroupAndStudent } from "../customHooks/useGetAssignmentsByGroupAndStudent"

export default function GroupUserProfilePage() {
  const {userProfileId} = useParams()
  const group= useAppSelector(selectGroup)
  const userProfile = useGetUser(userProfileId as unknown as number)
  const doneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "done")
  const expiredUndoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "expiredUndone")
  const undoneAssignments = useGetAssignmentsByGroupAndStudent(group?.id, userProfileId as unknown as number, "undone")

  return (
      <div className='flex flex-col h-[calc(100vh-320px)] overflow-y-hidden'>
        <p className='border-t border-t-black ml-32 w-[495px] mt-0.5'></p>
        <ul className='flex flex-col gap-1 pt-2 pb-6 box-content overflow-y-scroll'>
          <li className='ml-32'>Imię i nazwisko: {userProfile?.name} {userProfile?.lastname}</li>
          <li className='ml-32'>Indeks: {userProfile?.index}</li>
          <li>
            <dl>
              <dt >
                <p className='ml-32 underline underline-offset-2'>Zrobione zadania:</p>
                <dd>
                  <ul className='ml-12'>
                    {doneAssignments?.map((assignment) => {
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()}
                                                                             assignment={assignment}
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
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment}
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
                      return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment}
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