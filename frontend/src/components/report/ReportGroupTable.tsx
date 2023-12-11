import {GroupReportModel} from "../../types/GroupReport.model";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";

export const ReportGroupTable = ({groupReport}:{groupReport: GroupReportModel}) =>{

  const distinctStudents = () =>{
    const tempArray = groupReport.assignments.map((assignmentReport)=>{
      if(assignmentReport.students !== null){
        return assignmentReport.students.map((studentResult)=>{
          return {firstname: studentResult.student.firstname, lastname: studentResult.student.lastname}
        })
      }
    })
    return [... new Map]
  }
  return (<table>
    <tbody>
      <tr>
        <th>Zadanie</th>
        <th>Max punkty</th>
        <th>Najwyższy wynik</th>
        <th>Najniższy wynik</th>
        <th>Średnia</th>
        <th>Uczniowie</th>
        {groupReport.assignments.map((assignmentReport)=>{
          if(assignmentReport.students === null) return null
          return assignmentReport.students.map((studentResult) => {
            return <th>{studentResult.student.firstname} {studentResult.student.lastname}</th>
          })
        })}
      </tr>
      {groupReport.assignments.map((assignmentReport) => {
        return(
          <tr>
            <td>{assignmentReport.assignment.title}</td>
            <td>{assignmentReport.assignment.max_points}</td>
            <td>{assignmentReport.maxResult}</td>
            <td>{assignmentReport.minResult}</td>
            <td>{assignmentReport.avgResult} ({assignmentReport.avgResultPercent}%)</td>
            <td></td>
            {assignmentReport.students !== null && assignmentReport.students.map((studentResult)=>{
              return <td>{studentResult.result}</td>
            })}
          </tr>
        )
      })}
    </tbody>
  </table>)
}