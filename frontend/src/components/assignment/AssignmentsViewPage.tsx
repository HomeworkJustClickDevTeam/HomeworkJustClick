import {AssignmentModel} from "../../types/Assignment.model";
import {AssignmentsDisplayer} from "./AssignmentsDisplayer";
import React from "react";

export const AssignmentsViewPage = (props:{
  expiredUndoneAssignments: AssignmentModel[],
  nonExpiredUndoneAssignments: AssignmentModel[],
  doneAssignments: AssignmentModel[],
  backgroundColor: string}) =>{
  return <section className='flex flex-col box-content overflow-y-auto mb-4 '>
    <div className={`${props.backgroundColor} ml-[7.5%] rounded-xl pt-4 pb-4`}>
      <div>
        <p className='ml-[5.5%] font-semibold text-lg'>Zaległe niezrobione zadania:</p>
        {<AssignmentsDisplayer assignments={props.expiredUndoneAssignments}/>}
      </div>
      <div>
        <p className='ml-[5.5%] font-semibold mt-3 text-lg'>Zadania do zrobienia:</p>
        {<AssignmentsDisplayer assignments={props.nonExpiredUndoneAssignments}/>}
      </div>
      <div>
        <p className='ml-[5.5%] font-semibold text-lg mt-3'>Zrobione zadania:</p>
        {<AssignmentsDisplayer assignments={props.doneAssignments}/>}
      </div>
    </div>
  </section>
}