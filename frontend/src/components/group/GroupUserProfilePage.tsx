import {useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import postgresqlDatabase from "../../services/postgresDatabase";
import {AxiosError} from "axios";
import Loading from "../animations/Loading";
import userContext from "../../UserContext";
import {assigmentFilterStudent} from "../assigments/filter/AssigmentFilterStudent";
import AssigmentListElement from "../assigments/AssigmentListElement";
import {UserInterface} from "../../types/UserInterface";
import {AssignmentInterface} from "../../types/AssignmentInterface";

export default function GroupUserProfilePage(){
  const {userProfileId,id} = useParams()
  const [userProfile, setUserProfile] = useState<UserInterface | undefined>(undefined)
  const [doneAssignments, setDoneAssignments] = useState<AssignmentInterface[] | undefined>(undefined)
  const [expiredUndoneAssignments, setExpiredUndoneAssignments] = useState<AssignmentInterface[] | undefined>(undefined)
  const [undoneAssignments, setUndoneAssignments] = useState<AssignmentInterface[] | undefined>(undefined)
  const {userState} = useContext(userContext)

  useEffect(()=>{
    postgresqlDatabase
      .get(`/user/${userProfileId}`)
      .then((response) => setUserProfile(response.data))
      .catch((error:AxiosError) => console.log(error))

    postgresqlDatabase
      .get(`/assignments/doneByGroupAndStudent/${id}/${userProfileId}`)
      .then((r) => setDoneAssignments(r.data))
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setDoneAssignments([]) : console.log(e))

    postgresqlDatabase
      .get(`/assignments/expiredUndoneByGroupAndStudent/${id}/${userProfileId}`)
      .then((r) => setExpiredUndoneAssignments(r.data))
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setExpiredUndoneAssignments([]) : console.log(e))

    postgresqlDatabase
      .get(`/assignments/undoneByGroupAndStudent/${id}/${userProfileId}`)
      .then((r) => setUndoneAssignments(r.data))
      .catch((e:AxiosError) =>(e.response?.status === 404) ? setUndoneAssignments([]) : console.log(e))
  }, [])


  if((userProfile || doneAssignments || expiredUndoneAssignments || undoneAssignments) === undefined){
    return <Loading/>
  }
  return(
    <ul>
      <li>Imię i nazwisko: {userProfile?.firstname} {userProfile?.lastname}</li>
      <li>Indeks: {userProfile?.index}</li>
      <li>
        <dl>
          <dt>Zrobione zadania:
            <dd>
              <ul>
                {doneAssignments?.map((assignment) =>{
                  return(<li key={assignment.id}><AssigmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment} idGroup={id as string} /></li>)
                })}
              </ul>
            </dd>
          </dt>
          <dt>Niezrobione zadania:
            <dd>
              <ul>
                {undoneAssignments?.map((assignment) =>{
                  return(<li><AssigmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment} idGroup={id as string} /></li>)
                })}
              </ul>
            </dd>
          </dt>
          <dt>Spoźnione nieoddane zadania:
            <dd>
              <ul>
                {expiredUndoneAssignments?.map((assignment) =>{
                  return(<li><AssigmentListElement optionalUserId={userProfile?.id?.toString()} assignment={assignment} idGroup={id as string} /></li>)
                })}
              </ul>
            </dd>
          </dt>
        </dl>
      </li>
    </ul>
  )
}