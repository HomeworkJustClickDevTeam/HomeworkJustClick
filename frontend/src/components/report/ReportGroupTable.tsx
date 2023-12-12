import {GroupReportModel} from "../../types/GroupReport.model";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {useEffect, useState} from "react";

export const ReportGroupTable = ({groupReport}:{groupReport: GroupReportModel}) =>{
  const [studentsFullNames, setStudentsFullNames] = useState<Set<string>>(new Set)
  const [studentsResults] = useState<Map<string,Map<string, number|undefined>>>(()=>{
    const output = new Map<string, Map<string, number|undefined>>()
    groupReport.assignments
      .filter((assignmentReport)=>assignmentReport.students !== null)
      .forEach(assignmentReport => {
        let studentResultMap = new Map<string, number|undefined>()
        assignmentReport.students.forEach((studentResult)=>{
          const studentFullName = studentResult.student.firstname + " " + studentResult.student.lastname
          if(!studentsFullNames.has(studentFullName)) setStudentsFullNames(prevState => new Set(prevState.add(studentFullName)))
          studentResultMap.set(studentFullName, studentResult.result)
        })
        output.set(assignmentReport.assignment.title, studentResultMap)
      })
    return output
    })
  const renderStudentsFullNames = ():JSX.Element[] =>{
    let studentsFullNamesColumn:JSX.Element[] = []
    studentsFullNames.forEach(( studentFullName)=>{
      studentsFullNamesColumn.push(<th>{studentFullName}</th>)
    })
    return studentsFullNamesColumn
  }

  const renderStudentsResults = (assignmentTitle:string):JSX.Element[] =>{
    let studentsResultsNumbersColumn:JSX.Element[]=[]
    studentsFullNames.forEach((studentFullName)=>{
      studentsResultsNumbersColumn.push(<td>{studentsResults.get(assignmentTitle)!.get(studentFullName)}</td>)
    })
    return studentsResultsNumbersColumn
  }

  return (<table>
    <tbody>
      <tr>
        <th>Zadanie</th>
        <th>Max punkty</th>
        <th>Najwyższy wynik</th>
        <th>Najniższy wynik</th>
        <th>Średnia</th>
        <th>Studenci</th>
        {renderStudentsFullNames()}
      </tr>
      {groupReport.assignments
        .filter((assignmentReport)=> assignmentReport.students!==null)
        .map((assignmentReport) => {
          return(
            <tr>
              <td>{assignmentReport.assignment.title}</td>
              <td>{assignmentReport.assignment.max_points}</td>
              <td>{assignmentReport.maxResult}</td>
              <td>{assignmentReport.minResult}</td>
              <td>{assignmentReport.avgResult} ({assignmentReport.avgResultPercent}%)</td>
              <td></td>
              {renderStudentsResults(assignmentReport.assignment.title)}
            </tr>
          )
      })}
    </tbody>
  </table>)
}