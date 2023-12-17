import {AssignmentReportModel} from "../../types/AssignmentReport.model";

export const ReportAssignmentTable = ({assignmentReport, maxPoints}:{assignmentReport: AssignmentReportModel, maxPoints: number}) =>{
  return (
    <table>
      <tbody>
        <tr>
          <th>Uczeń</th>
          {assignmentReport.students.map((studentResult,index)=><td key={index}>{studentResult.student.firstname} {studentResult.student.lastname}</td>)}
        </tr>
        <tr>
          <th>Punkty</th>
          {assignmentReport.students.map((studentResult, index) =><td key={index}>{studentResult.result}/{maxPoints}</td>)}
        </tr>
        <tr>
          <th>Procenty</th>
          {assignmentReport.students.map((studentResult, index)=><td key={index}>{studentResult.resultPercent} %</td>)}
        </tr>
      </tbody>
    </table>
  )
}