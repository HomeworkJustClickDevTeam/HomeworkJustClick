import React, {useState} from "react"
import AssignmentListElement from "./AssignmentListElement"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByStudent } from "../customHooks/useGetAssignmentsByStudent"
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {selectGroup} from "../../redux/groupSlice";
import {useGetEvaluationsByGroup} from "../customHooks/useGetEvaluationsByGroup";
import {useGetSolutionsByGroup} from "../customHooks/useGetSolutionsByGroup";
import {AssignmentsDisplayer} from "./AssignmentsDisplayer";


export default function AssignmentsMainPage() {
  const userState = useAppSelector(selectUserState)
  const userDoneAssignments = useGetAssignmentsByStudent(userState?.id, 'done')
  const userExpiredUndoneAssignments = useGetAssignmentsByStudent(userState?.id, 'expiredUndone')
  const userNonExpiredUndoneAssignments = useGetAssignmentsByStudent(userState?.id, 'nonExpiredUndone')

  if(userDoneAssignments.length === 0 && userExpiredUndoneAssignments.length === 0 && userNonExpiredUndoneAssignments.length === 0)
    return (<>Tu będą informacje o zadaniach z każdej grupy, w której jesteś uczeniem. Nie jesteś uczeniem w żadnej grupie.</>)

  return <>
    Zaległe niezrobione zadania:<br/>
    {userExpiredUndoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userExpiredUndoneAssignments}/>: <div>Brak</div>}
    Zadania do zrobienia:<br/>
    {userNonExpiredUndoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userNonExpiredUndoneAssignments}/>: <div>Brak</div>}
    Zrobione zadania:<br/>
    {userDoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userDoneAssignments}/> : <div>Brak</div>}
  </>
}
