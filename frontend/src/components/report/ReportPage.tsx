import {format, parseISO} from "date-fns";
import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
import {ReportAssignmentTable} from "./ReportAssignmentTable";
import {ReportGroupTable} from "./ReportGroupTable";
import {ReportCreate} from "./ReportCreate";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {GroupInterface} from "../../types/GroupInterface";
import {Chart} from "react-google-charts";
import {ReportPageUnfoldingElements} from "../../types/ReportPageUnfoldingElements";




export const ReportPage = () =>{
  const {state} = useLocation()
  const [report, setReport] = useState<AssignmentReportModel|GroupReportModel|undefined>(undefined)
  const [csvCreateFormShown, setCsvCreateFormShown] = useState(false)
  const [reportedObject, setReportedObject] = useState<AssignmentInterface|GroupInterface|undefined>(undefined)
  const [unfoldedElement, setUnfoldedElement] = useState<ReportPageUnfoldingElements|undefined>(undefined)
  const [histogramData, setHistogramData] = useState<any>([])
  const [histogramTitle, setHistogramTitle] = useState("")

  const handleUnfolding = (clickedElement: ReportPageUnfoldingElements) => {
    switch (clickedElement){
      case ReportPageUnfoldingElements.histogram:
        if(unfoldedElement === ReportPageUnfoldingElements.histogram) setUnfoldedElement(undefined)
        else setUnfoldedElement(clickedElement)
        break
      case ReportPageUnfoldingElements.table:
        if(unfoldedElement === ReportPageUnfoldingElements.table) setUnfoldedElement(undefined)
        else setUnfoldedElement(clickedElement)
        break
    }
  }

  const options = {
    title: histogramTitle,
    legend: { position: "top", maxLines: 1},
    hAxis:{format:'percent'}
  };

  useEffect(()=>{
    if(state !== null) {
      setReportedObject(state.reportedObject)
      setReport(state.report)
      if(state.report.assignment !== undefined){
        const preparedData:any = (state.report as AssignmentReportModel).students
          .filter(studentResult=>studentResult!==null)
          .map(studentResult=>{
            return [studentResult.student.firstname + ' ' + studentResult.student.lastname, studentResult.resultPercent/100]
          })
        preparedData.unshift(["Studenci", "Wynik procentowy"])
        setHistogramTitle('Wykres ilości studentów z danym wynikiem procentowym.')
        setHistogramData(preparedData)
      }
      else{
        let preparedData:any = (state.report as GroupReportModel).assignments
          .filter(assignmentResult=>assignmentResult.assignment !== null)
          .map((assignmentResult)=>{
          return [assignmentResult.assignment.title, assignmentResult.avgResultPercent/100]
        })
        preparedData.unshift(["Zadanie", "Średnia ilość procent"])
        setHistogramTitle('Wykres ilości zadań z daną średnią ilością procent studentów.')
        setHistogramData(preparedData)
      }
    }
  }, [state])

  if(report === undefined || state === undefined) return null
  return <div>
    {(reportedObject! as AssignmentInterface).title !== undefined &&
    (reportedObject! as AssignmentInterface).completionDatetime !== undefined &&
    (reportedObject! as AssignmentInterface).max_points !== undefined ?
      <div>
        <div>RAPORT
          ZADANIA: {(reportedObject! as AssignmentInterface).title} {format(parseISO((reportedObject! as AssignmentInterface).completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}</div>
        <div>Maksymalna możliwa liczba punktów: {(reportedObject! as AssignmentInterface).max_points}</div>
        <br/>
        <div>Najwyższa liczba punktów: {(report as AssignmentReportModel).maxResult}</div>
        <br/>
        <div>Najniższa liczba punktów: {(report as AssignmentReportModel).minResult}</div>
        <br/>
        <div>Średnia liczba punktów: {(report as AssignmentReportModel).avgResult}</div>
        <br/>
        <div>Średni procent: {(report as AssignmentReportModel).avgResultPercent}</div>
        <br/>
        <div>Ilość zadań przesłanych po terminie: {(report as AssignmentReportModel).late}</div>
        <br/>
      </div>
      : <div>RAPORT GRUPY: {(reportedObject! as GroupInterface).name}</div>}
    <button onClick={() => handleUnfolding(ReportPageUnfoldingElements.table)}>
      TABELA:<br/>
      {(reportedObject! as AssignmentInterface).max_points !== undefined && unfoldedElement === ReportPageUnfoldingElements.table
        ? <ReportAssignmentTable max_points={(reportedObject! as AssignmentInterface).max_points}
                                 assignmentReport={report as AssignmentReportModel}/>
        : unfoldedElement === ReportPageUnfoldingElements.table && <ReportGroupTable groupReport={report as GroupReportModel}/>}
    </button>
    <br/>
    <div onClick={() => handleUnfolding(ReportPageUnfoldingElements.histogram)}>
      HISTOGRAM:<br/>
      {unfoldedElement === ReportPageUnfoldingElements.histogram &&
        <Chart
          chartType="Histogram"
          width="100%"
          height = "400px"
          options={options}
          data={histogramData}
        />}
    </div>
      <div>
        <button onClick={() => setCsvCreateFormShown(true)}>Pobierz raport CSV</button>
        {csvCreateFormShown && reportedObject !== undefined &&
            <ReportCreate reportedObject={reportedObject} csvVersion={true}/>}
      </div>
  </div>
}