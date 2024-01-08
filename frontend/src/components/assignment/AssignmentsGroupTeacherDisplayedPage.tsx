import {Link, useLocation} from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import { selectRole } from "../../redux/roleSlice"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByGroup } from "../customHooks/useGetAssignmentsByGroup"
import React, {useEffect, useState} from "react";
import {AssignmentModel} from "../../types/Assignment.model";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {createReportAssignment, createReportGroup} from "../../services/postgresDatabaseServices";
import {ReportCreate} from "../report/ReportCreate";
import {GroupInterface} from "../../types/GroupInterface";

function AssignmentsGroupTeacherDisplayedPage() {
  const role = useAppSelector(selectRole)
  const {state} = useLocation()
  const group= useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group?.id)
  const [chosenObjectsReport, setChosenObjectsReport] = useState<AssignmentModel| GroupInterface |undefined>(undefined)



  useEffect(()=>{
    if(group !== null && state?.groupReport === true && ((chosenObjectsReport !== undefined && !('name' in chosenObjectsReport))|| chosenObjectsReport === undefined)) setChosenObjectsReport(group)
    else if (state?.groupReport === true && chosenObjectsReport !== undefined && 'name' in chosenObjectsReport) setChosenObjectsReport(undefined)
  }, [state])
  const handleGenerateReportButtonClick = (assignment: AssignmentModel) =>{
    if(assignment === chosenObjectsReport) setChosenObjectsReport(undefined)
    else setChosenObjectsReport(assignment)
  }

  return (
    <div className='flex flex-col h-[calc(100dvh-325px)] overflow-y-hidden w-full'>
      <div className='relative h-full box-content overflow-y-auto w-full'>
        <ul className="flex flex-col  mb-4">
          {assignments.map((assignment) => (
              <li key={assignment.id} className='flex inline-block '>
                <AssignmentListElement idGroup={group!.id.toString()}
                                       assignment={assignment}
                                       createReportButton={true}
                                       handleGenerateReportButtonClick={() => handleGenerateReportButtonClick(assignment)}/>{" "}

              </li>
            ))}
        </ul>
        <div className='fixed left-[calc(7.5%+700px)] top-[350px] bg-white'>
          {chosenObjectsReport && <ReportCreate closeReportCreator={()=> setChosenObjectsReport(undefined)} csvVersion={false} reportedObject={chosenObjectsReport}/>}
        </div>
      </div>

    </div>
  )
}

export default AssignmentsGroupTeacherDisplayedPage
