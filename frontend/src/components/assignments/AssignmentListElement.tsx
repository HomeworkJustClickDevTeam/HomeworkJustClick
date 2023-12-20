import { Link } from "react-router-dom"
import {format, parseISO} from "date-fns"
import { AssignmentPropsInterface } from "../../types/AssignmentPropsInterface"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {AssignmentPieChart} from "../evaluation/AssignmentPieChart";
import React, {useState} from "react";
import {AssignmentListElementPropsInterface} from "../../types/AssignmentListElementPropsInterface";
import { BsThreeDots } from "react-icons/bs";



function AssignmentListElement({assignment,
                                 unfoldedPieChartAssignment,
                                 handleAssignmentClick,
                                 idGroup,
                                 optionalUserId,
                                 createReportButton=false,
                                 handleGenerateReportButtonClick}: AssignmentListElementPropsInterface) {

  return (
    <div className='ml-[7.5%]'>
      <Link to={idGroup!==undefined ? `/group/${idGroup}/assignment/${assignment.id}`: "#"} onClick={() => {handleAssignmentClick && handleAssignmentClick(assignment)}} state={optionalUserId}
            className="flex flex-row mt-4 border-border_gray border h-16 rounded-lg font-lato text-xl items-center  w-[900px] ">
        <div className="flex-col ml-10 basis-3/5 ">
          <div className='text-xl w-full overflow-hidden'>{assignment.title}</div>
          <div className="text-border_gray">{format(new Date(assignment.completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}</div>
        </div>
          <div className='basis-1/5 text-lg mr-4 h-full leading-[60px]'>
              {createReportButton && handleGenerateReportButtonClick && <Link to={`#`} type={"button"} onClick={()=>{handleGenerateReportButtonClick()}}>Wygeneruj raport</Link>}</div>
        <div className="mr-10 text-right font-semibold text-[28px] basis-1/5">{"/" + assignment.maxPoints}</div>
      </Link>
      {unfoldedPieChartAssignment?.id === assignment.id && <div><br/><AssignmentPieChart assignment={assignment}/></div>}
    </div>
  )
}

export default AssignmentListElement
