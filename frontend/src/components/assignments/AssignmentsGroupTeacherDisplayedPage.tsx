import { Link } from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import { selectRole } from "../../redux/roleSlice"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByGroup } from "../customHooks/useGetAssignmentsByGroup"
import React, {useState} from "react";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {createReportAssignment, createReportGroup} from "../../services/postgresDatabaseServices";
import {CreateReport} from "../report/CreateReport";

function AssignmentsGroupTeacherDisplayedPage() {
  const role = useAppSelector(selectRole)
  const group= useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group?.id)
  const [chosenAssignment, setChosenAssignment] = useState<AssignmentInterface|undefined>(undefined)
  const [report, setReport] = useState<AssignmentReportModel|GroupReportModel|undefined>(undefined)

  const handleGenerateReportButtonClick = (assignment: AssignmentInterface) =>{
    if(assignment === chosenAssignment) setChosenAssignment(undefined)
    else setChosenAssignment(assignment)
  }
  const handleReportCreation = async (createdReportData: AssignmentCreateReportModel|GroupCreateReportModel, event:React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if('assignmentId' in createdReportData){
      const assignmentReport = await createReportAssignment(createdReportData)
    }
    else {
      const groupReport = await createReportGroup(createdReportData)
    }
  }

  return (
    <div className='relative h-[420px]'>
      {role === "Teacher" && chosenAssignment === undefined && (
        <Link to={`/group/${group?.id}/assignments/add`}>
          <button
            className='absolute mb-4 right-[7.5%] bottom-0 bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none'>Nowe
            zadanie +
          </button>
        </Link>
      )}
      <div>
        <div style={{float:"left", width:"50%"}}>
          {assignments.map((assignment) => {
            return <AssignmentListElement key={assignment.id}
                                          idGroup={group!.id.toString()}
                                          assignment={assignment}
                                          createReportButton={true}
                                          handleGenerateReportButtonClick={()=>handleGenerateReportButtonClick(assignment)}/>
          })}
        </div>
        <div style={{float:"left", width:"50%"}}>
          {chosenAssignment && <CreateReport handleReportCreation={handleReportCreation}
                                             reportedObject={chosenAssignment}/>}
        </div>
      </div>
    </div>
  )
}

export default AssignmentsGroupTeacherDisplayedPage
