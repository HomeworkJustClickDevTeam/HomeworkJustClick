import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import AssignmentListElement from "../assignments/AssignmentListElement";
import React, {useState} from "react";
import {CreateReport} from "../report/CreateReport";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";

export const EvaluationsTeacherPage = () => {
  const group = useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group?.id)
  const [chosenAssignment, setChosenAssignment] = useState<AssignmentInterface|undefined>(undefined)
  const [report, setReport] = useState<AssignmentReportModel|undefined>(undefined)

  const handleGenerateReportButtonClick = (assignment: AssignmentInterface) =>{
    if(assignment === chosenAssignment) setChosenAssignment(undefined)
    else setChosenAssignment(assignment)
  }
  const handleReportCreation = (event: React.FormEvent<HTMLFormElement>) => {
      event.preventDefault()
      console.debug(event.target)
  }
  return (
    <div>
      <div style={{float:"left", width:"50%"}}>
        {assignments.map((assignment) => {
          return <AssignmentListElement key={assignment.id} idGroup={group!.id.toString()} assignment={assignment} createReportButton={true} handleGenerateReportButtonClick={()=>handleGenerateReportButtonClick(assignment)}/>
        })}
      </div>
      <div style={{float:"left", width:"50%"}}>
        {chosenAssignment && <CreateReport handleReportCreation={handleReportCreation} evaluationName={chosenAssignment.title} date={new Date(chosenAssignment.completionDatetime)} maxPoints={chosenAssignment.max_points}/>}
      </div>
  </div>
  )
}