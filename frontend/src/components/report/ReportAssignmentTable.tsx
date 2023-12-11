import {AssignmentReportModel} from "../../types/AssignmentReport.model";

export const ReportAssignmentTable = ({assignmentReport, max_points}:{assignmentReport: AssignmentReportModel, max_points: number}) =>{
  return (
    <table>
      <tbody>
        <tr>
          <th>Uczeń</th>
          {assignmentReport.students.map((studentResult)=><td>{studentResult.student.firstname} {studentResult.student.lastname}</td>)}
        </tr>
        <tr>
          <th>Punkty</th>
          {assignmentReport.students.map((studentResult) =><td>{studentResult.result}/{max_points}</td>)}
        </tr>
        <tr>
          <th>Procenty</th>
          {assignmentReport.students.map((studentResult)=><td>{studentResult.resultPercent} %</td>)}
        </tr>
      </tbody>
    </table>
  )
}