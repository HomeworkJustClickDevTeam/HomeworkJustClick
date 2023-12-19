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
  return <div className='relative flex h-[calc(100vh-325px)] overflow-hidden mb-3 justify-center'><div className='absolute h-[99.5%] border border-border_gray rounded-md  w-[1200px] box-content overflow-y-auto shadow-xl pl-8'>
    {(reportedObject! as AssignmentInterface).title !== undefined &&
    (reportedObject! as AssignmentInterface).completionDatetime !== undefined &&
    (reportedObject! as AssignmentInterface).maxPoints !== undefined ?
      <div className='flex flex-col   pt-4'>
        <div className='text-center underline underline-offset-4 mb-6'><span className='font-bold'>RAPORT ZADANIA:</span> <span className='font-semibold'>{(reportedObject! as AssignmentInterface).title}</span> ( {format(new Date((reportedObject! as AssignmentInterface).completionDatetime.toString()), "dd.MM.yyyy, HH:mm")} )</div>
        <div className=''>Maksymalna możliwa liczba punktów: <span className='font-semibold ml-1'>{(reportedObject! as AssignmentInterface).maxPoints}</span></div>
        <br/>
        <div>Najwyższa liczba punktów: <span className='font-semibold ml-1'>{(report as AssignmentReportModel).maxResult}</span></div>
        <br/>
        <div>Najniższa liczba uzyskanych punktów: <span className='font-semibold ml-1'>{(report as AssignmentReportModel).minResult}</span></div>
        <br/>
        <div>Średnia liczba uzyskanych punktów: <span className='font-semibold ml-1'>{(report as AssignmentReportModel).avgResult}</span></div>
        <br/>
        <div>Średni procent: <span className='font-semibold ml-1'>{(report as AssignmentReportModel).avgResultPercent}%</span></div>
        <br/>
        <div>Ilość zadań przesłanych po terminie: <span className='text-berry_red font-semibold ml-1'>{(report as AssignmentReportModel).late}</span></div>
        <br/>
      </div>
      : <div>RAPORT GRUPY: {(reportedObject! as GroupInterface).name}</div>}
    <button onClick={() => handleUnfolding(ReportPageUnfoldingElements.table)} className='w-36 text-left'>
      <span className='underline underline-offset-2'>{unfoldedElement === ReportPageUnfoldingElements.table ? 'Zwiń tabelę:' : 'Rozwiń tabelę: '}</span><br/>
      {(reportedObject! as AssignmentInterface).maxPoints !== undefined && unfoldedElement === ReportPageUnfoldingElements.table
        ? <ReportAssignmentTable maxPoints={(reportedObject! as AssignmentInterface).maxPoints}
                                 assignmentReport={report as AssignmentReportModel}/>
        : unfoldedElement === ReportPageUnfoldingElements.table && (report as GroupReportModel).assignments !== undefined && <ReportGroupTable groupReport={report as GroupReportModel}/>}
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
      <div className='mb-3 w-full'>
        <button onClick={() => setCsvCreateFormShown(true)}>Pobierz raport CSV</button>
        <div className='flex justify-center'>{csvCreateFormShown && reportedObject !== undefined &&
            <ReportCreate reportedObject={reportedObject} csvVersion={true}/>}</div>
      </div>
  </div>
  </div>
}