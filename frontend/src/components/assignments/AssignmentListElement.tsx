import { Link } from "react-router-dom"
import {format, parseISO} from "date-fns"
import { AssignmentPropsInterface } from "../../types/AssignmentPropsInterface"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {AssignmentPieChart} from "../evaluation/AssignmentPieChart";
import React from "react";
import {AssignmentListElementPropsInterface} from "../../types/AssignmentListElementPropsInterface";



function AssignmentListElement({assignment,
                                 unfoldedPieChartAssignment,
                                 handleAssignmentClick,
                                 idGroup,
                                 optionalUserId,
                                 createReportButton=false,
                                 handleGenerateReportButtonClick}: AssignmentListElementPropsInterface) {
  return (
    <>
      <Link to={idGroup!==undefined ? `/group/${idGroup}/assignment/${assignment.id}`: "#"} onClick={() => {handleAssignmentClick && handleAssignmentClick(assignment)}} state={optionalUserId}
            className="flex ml-[7.5%] mt-4 border-border_gray border h-16 rounded-lg font-lato text-xl items-center justify-between w-[700px]">
        <div className="flex-col ml-10 ">
          <div>{assignment.title}</div>
          <div className="text-border_gray">{format(parseISO(assignment.completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}</div>
        </div>
        {createReportButton && handleGenerateReportButtonClick && <Link to={`#`} type={"button"} onClick={()=>{handleGenerateReportButtonClick()}}>Wygeneruj raport</Link>}
        <div className="mr-10 font-semibold text-[28px]">{"/" + assignment.max_points}</div>
      </Link>
      {unfoldedPieChartAssignment?.id === assignment.id && <div><br/><AssignmentPieChart assignment={assignment}/></div>}
    </>
  )
}

export default AssignmentListElement
