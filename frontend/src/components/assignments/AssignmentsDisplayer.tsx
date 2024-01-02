import {AssignmentInterface} from "../../types/AssignmentInterface";
import AssignmentListElement from "./AssignmentListElement";
import React from "react";

export const AssignmentsDisplayer = ({assignments}:{assignments:AssignmentInterface[]}) =>{
  return (
    <ul>
      {assignments.map(assignment=> (
        <li key={assignment.id}>
          <AssignmentListElement
            assignment={assignment}
            idGroup={`${assignment.groupId}`}
          />
        </li>
      ))}
    </ul>
  )
}