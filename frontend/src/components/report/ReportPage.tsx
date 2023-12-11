import {format} from "date-fns";
import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
import {ReportAssignmentTable} from "./ReportAssignmentTable";
import {ReportGroupTable} from "./ReportGroupTable";
import {ReportCreate} from "./ReportCreate";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {GroupInterface} from "../../types/GroupInterface";

export const ReportPage = () =>{
  const {state} = useLocation()
  const [report, setReport] = useState<AssignmentReportModel|GroupReportModel|undefined>(undefined)
  const [csvCreateFormShown, setCsvCreateFormShown] = useState(false)
  const [reportedObject, setReportedObject] = useState<AssignmentInterface|GroupInterface|undefined>(undefined)

  useEffect(()=>{
    if(state !== null) {
      setReportedObject(state.reportedObject)
      setReport(state.report)
    }
  }, [state])

  if(report === undefined || state === undefined) return null
  return <div>
    {(reportedObject! as AssignmentInterface).title !== undefined &&
    (reportedObject! as AssignmentInterface).completionDatetime !== undefined &&
    (reportedObject! as AssignmentInterface).max_points !== undefined ?
        <div>
          <div>RAPORT ZADANIA: {state.title} {format(new Date((reportedObject! as AssignmentInterface).completionDatetime), "dd.MM.yyyy, HH:mm")}</div><div>Maksymalna możliwa liczba punktów: {(reportedObject! as AssignmentInterface).max_points}</div><br/>
          <div>Najwyższa liczba punktów: {(report as AssignmentReportModel).maxResult}</div><br/>
          <div>Najniższa liczba punktów: {(report as AssignmentReportModel).minResult}</div><br/>
          <div>Średnia liczba punktów: {(report as AssignmentReportModel).avgResult}</div><br/>
          <div>Średni procent: {(report as AssignmentReportModel).avgResultPercent}</div><br/>
          <div>Ilość zadań przesłanych po terminie: {(report as AssignmentReportModel).late}</div><br/>
        </div>
      : <div>RAPORT GRUPY: {(reportedObject! as GroupInterface).name}</div>}
    {(reportedObject! as AssignmentInterface).max_points !== undefined ? <ReportAssignmentTable max_points={(reportedObject! as AssignmentInterface).max_points} assignmentReport={report as AssignmentReportModel}/> : <ReportGroupTable groupReport={report as GroupReportModel}/>}
    <div>
      <button onClick={()=>setCsvCreateFormShown(true)}>Pobierz raport CSV</button>
      {csvCreateFormShown && reportedObject !== undefined && <ReportCreate reportedObject={reportedObject} csvVersion={true}/>}
    </div>
  </div>
}