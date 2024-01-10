import {AssignmentReportModel} from "../../types/AssignmentReport.model";

export const ReportAssignmentTable = ({assignmentReport, maxPoints}:{assignmentReport: AssignmentReportModel, maxPoints: number}) =>{
  return (

    <table className='my-6 w-full  overflow-x-auto'>
      <tbody className='w-full'>
        <tr className='w-full whitespace-nowrap '>
          <th className='pr-4 text-sm py-2 border-r-2 border-b-2 border-light_gray uppercase'>Uczeń</th>
          {assignmentReport.students!.map((studentResult,index)=><td className='w-[200px] px-6 overflow-hidden text-center border-x border-border_gray border-r-2' key={index}>{studentResult.student.firstname} {studentResult.student.lastname}</td>)}
        </tr>
        <tr className='w-full '>
          <th className='pr-4 text-sm py-2 border-r-2 border-b-2 border-light_gray uppercase'>Punkty</th>
          {assignmentReport.students!.map((studentResult, index) =><td className={(studentResult.result/maxPoints)>0.5 ?'w-[200px] px-6 overflow-hidden text-center border border-border_gray' : 'text-berry_red w-[200px] px-6 overflow-hidden text-center border border-[#DEDEDE] border-x-border_gray border-border_gray border-r-2'} key={index}>{studentResult.result}/{maxPoints}</td>)}
        </tr>
        <tr className='w-full'>
          <th className='pr-4 text-sm py-2 border-r-2 border-light_gray uppercase'>Procenty</th>
          {assignmentReport.students!.map((studentResult, index)=><td className={studentResult.resultPercent>50 ? 'w-[200px] px-6 overflow-hidden text-center border-x border-border_gray border-r-2' : 'text-berry_red w-[200px] px-6 overflow-hidden text-center border-x border-border_gray border-r-2'} key={index}>{studentResult.resultPercent}%</td>)}
        </tr>
      </tbody>
    </table>

  )
}