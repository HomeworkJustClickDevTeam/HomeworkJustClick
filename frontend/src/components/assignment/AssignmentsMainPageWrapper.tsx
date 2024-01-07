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
import {AssignmentsViewPage} from "./AssignmentsViewPage";


export default function AssignmentsMainPageWrapper() {
  const userState = useAppSelector(selectUserState)
  const userDoneAssignments = useGetAssignmentsByStudent(userState?.id, 'done')
  const userExpiredUndoneAssignments = useGetAssignmentsByStudent(userState?.id, 'expiredUndone')
  const userNonExpiredUndoneAssignments = useGetAssignmentsByStudent(userState?.id, 'nonExpiredUndone')

  if(userDoneAssignments.length === 0 && userExpiredUndoneAssignments.length === 0 && userNonExpiredUndoneAssignments.length === 0)
      return (<div className='bg-lilly-bg mx-[7.5%] rounded-xl pt-32  h-96 mt-6 text-center '><span className='underline underline-offset-4 hover:text-pink hover:decoration-white hover:cursor-default select-none'>Tu będą informacje o zadaniach z każdej grupy, w której jesteś uczeniem. Nie jesteś uczeniem w żadnej grupie albo nie dostałeś jeszcze żadnych zadań do zrobienia.</span></div>)

  return <div className='mt-4 flex flex-col h-[calc(100vh-80px)] overflow-y-hidden '>
    <AssignmentsViewPage expiredUndoneAssignments={userExpiredUndoneAssignments}
                         nonExpiredUndoneAssignments={userNonExpiredUndoneAssignments}
                         doneAssignments={userDoneAssignments}
                         backgroundColor={"bg-lilly-bg"}/>

  </div>
}
