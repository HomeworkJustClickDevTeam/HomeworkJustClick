import {GroupReportModel} from "../../types/GroupReport.model";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {useGetAssignmentsByGroup} from "../customHooks/useGetAssignmentsByGroup";
import {useEffect, useState} from "react";

export const ReportGroupTable = ({groupReport}:{groupReport: GroupReportModel}) =>{
  const [studentsFullNames, setStudentsFullNames] = useState<Set<string>>(new Set())
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
      studentsFullNamesColumn.push(<th className='pb-2 text-sm px-3 border-r-2 border-b-2 border-light_gray font-semibold text-center' key={studentFullName}>{studentFullName}</th>)
    })
    return studentsFullNamesColumn
  }

  const renderStudentsResults = (assignmentTitle:string):JSX.Element[] =>{
    let studentsResultsNumbersColumn:JSX.Element[]=[]
    studentsFullNames.forEach((studentFullName)=>{
      studentsResultsNumbersColumn.push(<td className=' px-6  text-center border border-border_gray border-r-2 border-l-2 ' key={studentFullName}>{studentsResults.get(assignmentTitle)!.get(studentFullName)}</td>)
    })
    return studentsResultsNumbersColumn
  }

  return (
      <table className='my-4 w-full  overflow-x-auto'>
    <tbody >
      <tr>
        <th className='pb-2 text-sm px-3 border-r-2 border-b-2 border-light_gray uppercase font-semibold leading-10'>Zadanie</th>
        <th className='pb-2 text-sm px-2 border-r-2 border-b-2 border-light_gray uppercase font-semibold'>Max punkty</th>
        <th className='pb-2 text-sm px-2 border-r-2 border-b-2 border-light_gray uppercase font-semibold'>Najwyższy wynik</th>
        <th className='pb-2 text-sm px-2 border-r-2 border-b-2 border-light_gray uppercase font-semibold'>Najniższy wynik</th>
        <th className='pb-2 text-sm px-2 border-r-2 border-b-2 border-light_gray uppercase font-semibold'>Średnia</th>
        <th className='pb-2 text-sm px-2 border-r-2 border-b-2 border-light_gray font-semibold w-16'>Studenci: </th>
        {renderStudentsFullNames()}
      </tr>
      {groupReport.assignments
        .filter((assignmentReport)=> assignmentReport.students!==null)
        .map((assignmentReport, index) => {
          return(
            <tr key={index}>
              <td className='pr-4 text-sm py-2 border-r-2 border-b-2 border-light_gray font-semibold text-center'>{assignmentReport.assignment.title}</td>
              <td className=' px-6 overflow-hidden text-center border border-border_gray border-r-2'>{assignmentReport.assignment.maxPoints}</td>
              <td className=' px-6 overflow-hidden text-center border border-border_gray border-r-2'>{assignmentReport.maxResult}</td>
              <td className=' px-6 overflow-hidden text-center border border-border_gray border-r-2'>{assignmentReport.minResult}</td>
              <td className=' px-6 overflow-hidden text-center border border-border_gray border-r-2'>{assignmentReport.avgResult} ({assignmentReport.avgResultPercent}%)</td>
              <td className='pattern-dots pattern-main_blue pattern-bg-white pattern-size-6 pattern-opacity-40'></td>
              {renderStudentsResults(assignmentReport.assignment.title)}
            </tr>
          )
      })}
    </tbody>
  </table>)
}