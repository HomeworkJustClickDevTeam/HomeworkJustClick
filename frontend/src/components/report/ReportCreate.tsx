import {format, parseISO} from "date-fns";
import React, {useEffect, useState} from "react";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {GroupInterface} from "../../types/GroupInterface";
import {AssignmentModel} from "../../types/Assignment.model";
import {f} from "msw/lib/core/RequestHandler-bb5cbb8f";
import {
  createReportAssignment,
  createReportAssignmentCSV,
  createReportGroup, createReportGroupCSV
} from "../../services/postgresDatabaseServices";
import {Link, useNavigate} from "react-router-dom";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
import {el} from "date-fns/locale";
import {useUpdateEffect} from "usehooks-ts";
import * as fs from "fs";
import * as domain from "domain";
import {toast} from "react-toastify";


export const ReportCreate = ({reportedObject, csvVersion, closeReportCreator}:
                               {reportedObject: GroupInterface|AssignmentModel,
                                csvVersion: boolean,
                                closeReportCreator: ()=>void}) =>{
  const [reportCreate, setReportCreate] = useState<AssignmentCreateReportModel|GroupCreateReportModel|undefined>(undefined)
  const navigate = useNavigate()
  const group = useAppSelector(selectGroup)
  const [histTextValue, setHistTextValue] = useState("")
  const handleChangingState = (newState: AssignmentCreateReportModel|GroupCreateReportModel) => {
    if('title' in newState) {
      setReportCreate(newState as AssignmentCreateReportModel)
    }
    else {
      setReportCreate(newState as GroupCreateReportModel)
    }
  }

  function checkForDuplicates(array:number[]) {
    return (new Set(array)).size !== array.length;
  }
  const checkHistFormat = () =>{
    const histTextWithoutWhitespaces = histTextValue.replace(/\s+/g, '')
    if(!(/^(0|[1-9][0-9]?|100)(,(0|[1-9][0-9]?|100))*$/.test(histTextWithoutWhitespaces))){//returns undefined if histTextValue is not in correct format
      toast.error("Liczby histogramu muszą być 0-100 oddzielone spacjami")
      return undefined
    }
    const histTextAsArray = histTextWithoutWhitespaces.split(",").map(number=>parseInt(number))
    if(checkForDuplicates(histTextAsArray)) {
      toast.error("Liczby histogramu nie mogą się powtarzać")
      return undefined
    }
    return histTextAsArray
  }
  const handleReportCreation = async (event:React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if(reportCreate === undefined) return
    try {
      if('assignmentId' in reportCreate){
        const response = await createReportAssignment(reportCreate)
        if(response?.status === 200){
          const assignmentReport = response.data as AssignmentReportModel
          if(assignmentReport.students === null || assignmentReport.students?.length === 0){
            toast.error("Żaden uczeń nie zrobił zadania w tej grupie, nie ma możliwości stworzenia raportu")
            return
          }
          navigate(`/group/${group!.id}/report`, {state:{
              report: assignmentReport,
              date: 'completionDatetime' in reportedObject ? reportedObject.completionDatetime : undefined,
              reportedObject: reportedObject}})
        }
      }
      else {
        const response = await createReportGroup(reportCreate)
        if(response?.status === 200){
          const groupReport = response.data as GroupReportModel
          if(groupReport.assignments === null){
            toast.error("Brak zadań w tej grupie, nie ma możliwości stworzenia raportu")
            return
          }
          navigate(`/group/${group!.id}/report`, {state:{report: groupReport,
              reportedObject: reportedObject}})
        }
      }
    }
    catch (e) {console.error(e)}
  }

  const handleCsvCreation = async (event:React.FormEvent<HTMLFormElement>) =>{
    event.preventDefault()
    if(reportCreate === undefined) return
    try {
      const histTextAsArrayNumbers = checkHistFormat()
      if(histTextAsArrayNumbers === undefined) return
      const reportCreateWithHist = {...reportCreate, hist: histTextAsArrayNumbers}
      let response = null
      if('assignmentId' in reportCreateWithHist) response = await createReportAssignmentCSV(reportCreateWithHist)
      else response = await createReportGroupCSV(reportCreateWithHist)
      if(response?.status === 200){
        const file = new Blob([response.data], {type:'text/plain;charset=utf8'})
        const href = URL.createObjectURL(file)
        const link = document.createElement('a')
        link.href = href
        link.download = 'csvReport.csv'
        link.click()
        document.removeChild(link)
        URL.revokeObjectURL(href)
      }
    }
    catch (e) {console.error(e)}
  }

  useEffect(() => {
    if('name' in reportedObject){
      setReportCreate({
        groupId: reportedObject.id,
        maxResult: true,
        minResult: true,
        avgResult: true,
        late: true,
        hist: []
      })
    }
    else {
      setReportCreate({
        assignmentId: reportedObject.id,
        maxResult: true,
        minResult: true,
        avgResult: true,
        late: true,
        hist: []
      })
    }
  }, []);

  if(reportCreate !== undefined) {
    return <div className='min-w-[400px] min-h-[200px]  border-2 border-light_gray  rounded-md pl-3 shadow-lg align-center hover:backdrop-blur'>
      <div className='text-center mb-6 mt-2 font-bold'>RAPORT</div>

      {'title' in reportedObject?
        <div>
          <div > <span className='font-semibold mr-1'>Nazwa zadania:</span> {reportedObject.title}</div>
          <br/>
          <div><span className='font-semibold mr-1'>Termin:</span> {format(new Date((reportedObject as AssignmentModel).completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}</div>
          <br/>
          <div><span className='font-semibold mr-1'>Punkty do zdobycia:</span> {(reportedObject as AssignmentModel).maxPoints}</div>
          <br/>
        </div>
          : <div><span className='font-semibold mr-1 mt-2'>Nazwa grupy:</span> {(reportedObject as GroupInterface).name}</div>}

      <form onSubmit={(event) => {csvVersion ? handleCsvCreation(event) : handleReportCreation(event)}}>
        {csvVersion &&
            <fieldset>
                <legend className='mb-2 cn underline underline-offset-4'>Elementy do wygenerowania:</legend>
                <div className='mb-1'>Średnia punktów: <input checked={reportCreate.avgResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          avgResult: !reportCreate.avgResult
                                        })}/></div>
                <div className='mb-1'>Najwyższy wynik: <input checked={reportCreate.maxResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          maxResult: !reportCreate.maxResult
                                        })}/></div>
              <div className='mb-1'>Najniższy wynik: <input checked={reportCreate.minResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          minResult: !reportCreate.minResult
                                        })}/></div>
              <div className='mb-1'>Ilość zadań przesłanych po terminie: <input checked={reportCreate.late}
                                                            type={'checkbox'}
                                                            onChange={() => handleChangingState({
                                                              ...reportCreate,
                                                              late: !reportCreate.late
                                                            })}/></div>
                <div className='mb-6 '><span className='font-semibold mr-1 '>Przedziały do histogramu :</span> <input
                    className='w-12 px-2 py-1 mr-2 ml-2 border-b solid black' type={"text"} value={histTextValue}
                    onChange={(event) => setHistTextValue(event.target.value)}/>%
                </div>
            </fieldset>}
        <div className='flex justify-between pr-4 mb-2  align-bottom mt-4'>
          <button className='absolute bottom-3 left-3 bg-main_blue text-white rounded-md text-sm p-1 px-2' type={"submit"}>Utwórz</button>
          <button type={'button'} onClick={() => closeReportCreator()} className='absolute right-3 bottom-3 bg-berry_red text-white rounded-md text-sm p-1 w-8'>X</button>
        </div>
      </form>
    </div>

  }
  else {return null}
}