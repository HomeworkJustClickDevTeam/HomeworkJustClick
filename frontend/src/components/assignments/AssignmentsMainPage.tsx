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
    return (<>Tu będą informacje o zadaniach z każdej grupy, w której jesteś uczeniem. Nie jesteś uczeniem w żadnej grupie albo nie dostałeś jeszcze żadnych zadań do zrobienia.</>)

  return <div className='mt-4 flex flex-col h-[calc(100vh-80px)] overflow-y-hidden '>

      <section className='flex flex-col box-content overflow-y-auto mb-4'>
          <div className='bg-lilly-bg mx-[7.5%] rounded-xl pt-4 pb-4'>
              <div>
            <p className='ml-[5.5%] font-semibold text-lg'>Zaległe niezrobione zadania:</p>
          {userExpiredUndoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userExpiredUndoneAssignments}/>: <p className='ml-[7.5%] mt-2'>Brak</p>}
              </div>
              <div>
              <p className='ml-[5.5%] font-semibold mt-3 text-lg'>Zadania do zrobienia:</p>
          {userNonExpiredUndoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userNonExpiredUndoneAssignments}/>: <p className='ml-[7.5%] mt-2'>Brak</p>}
              </div>
              <div>
          <p className='ml-[5.5%] font-semibold text-lg mt-3'>Zrobione zadania:</p>
          {userDoneAssignments.length > 0 ? <AssignmentsDisplayer assignments={userDoneAssignments}/> : <p className='ml-[7.5%] mt-2'>Brak</p>}
              </div>
          </div>
    </section>
  </div>
}
