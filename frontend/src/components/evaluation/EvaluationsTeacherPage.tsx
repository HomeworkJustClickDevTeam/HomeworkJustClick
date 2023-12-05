import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import AssignmentListElement from "../assignments/AssignmentListElement";
import {useState} from "react";
import {EvaluationsCreateReport} from "./EvaluationsCreateReport";

export const EvaluationsTeacherPage = () => {
  const group = useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group?.id)
  const [showReportWindow, setShowReportWindow] = useState(false)
  return (
    <div>
      {assignments.map((assignment) => {
        return <AssignmentListElement idGroup={group!.id.toString()} assignment={assignment} createReportButton={true} handleReportButtonClick={() => setShowReportWindow(prevState => !prevState)}/>
      })}
      {showReportWindow && <EvaluationsCreateReport/>}
  </div>
  )
}