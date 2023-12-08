import {format} from "date-fns";
import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";

export const ReportPage = () =>{
  const {state} = useLocation()
  const [report, setReport] = useState<AssignmentReportModel|GroupReportModel|undefined>(undefined)

  useEffect(()=>{
    console.log(state.date)
    if(state !== null) setReport(state.reportObject)
  }, [state])

  if(report === undefined) return null
  return <div>
    {state.title !== undefined && state.date !== undefined && state.max_points !== undefined ?
        <div><div>RAPORT ZADANIA: {state.title} {format(new Date(state.date), "dd.MM.yyyy, HH:mm")}</div><div>{state.max_points}</div><br/></div>
      :      <div>RAPORT GRUPY: {state.name}</div>}
    <div>Najwyższa liczba punktów: {Math.round(report.maxResult * 100)/100}</div><br/>
    <div>Najniższa liczba punktów: {Math.round(report.minResult * 100)/100}</div><br/>
    <div>Średnia liczba punktów: {Math.round(report.avgResult * 100)/100}</div><br/>
    <div>Średni procent: {Math.round(report.avgResultPercent * 100)/100}</div><br/>
    <div>Ilość zadań przesłanych po terminie: {report.late}</div><br/>
  </div>
}