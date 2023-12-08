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
    <ul>
      <li>Imię i nazwisko: {userProfile?.name} {userProfile?.lastname}</li>
      <li>Indeks: {userProfile?.index}</li>
      <li>
        <dl>
          <dt>Zrobione zadania:
            <dd>
              <ul>
                {doneAssignments?.map((assignment) => {
                  return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()}
                                                                         assignment={assignment}
                                                                         idGroup={group?.id as unknown as string}/></li>)
                })}
              </ul>
            </dd>
          </dt>
          <dt>Niezrobione zadania:
            <dd>
              <ul>
                {undoneAssignments?.map((assignment) => {
                  return (<li key={assignment.id}><AssignmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment}
                                                     idGroup={group?.id as unknown as string}/></li>)
                })}
              </ul>
            </dd>
          </dt>
          <dt>Spoźnione nieoddane zadania:
            <dd>
              <ul>
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
  )
}