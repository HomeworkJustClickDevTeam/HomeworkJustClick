import {format, parseISO} from "date-fns";
import React, {useEffect, useState} from "react";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {GroupInterface} from "../../types/GroupInterface";
import {AssignmentInterface} from "../../types/AssignmentInterface";
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


export const ReportCreate = ({reportedObject, csvVersion
}:{reportedObject: GroupInterface|AssignmentInterface, csvVersion: boolean}) =>{
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
  const handleReportCreation = async (event:React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if(reportCreate === undefined) return
    try {
      if('assignmentId' in reportCreate){
        const response = await createReportAssignment(reportCreate)
        if(response?.status === 200){
          const assignmentReport = response.data as AssignmentReportModel
          navigate(`/group/${group!.id}/report`, {state:{report: assignmentReport,
              date: 'completionDatetime' in reportedObject ? reportedObject.completionDatetime : undefined,
              reportedObject: reportedObject}})
        }
      }
      else {
        const response = await createReportGroup(reportCreate)
        if(response?.status === 200){
          const groupReport = response.data as GroupReportModel
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
      let response = null
      if('assignmentId' in reportCreate) response = await createReportAssignmentCSV(reportCreate)
      else response = await createReportGroupCSV(reportCreate)
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

  useUpdateEffect(()=>{
    const checkHistFormat = (inputTextValue: string) =>{
      const histNumList = inputTextValue.replace(/\s+/g, '').split(",").map((numberAsString)=>{
        const numberAsInt = parseInt(numberAsString, 10)
        if(numberAsInt > 100 || numberAsInt < 1) return NaN
        return numberAsInt
      })
        .filter((number) => !isNaN(number))
      setHistTextValue(histNumList.join(","))
      const tempObj = {...reportCreate!, hist: [...histNumList]}
      handleChangingState(tempObj)
    }
    const timeoutId = setTimeout(()=>checkHistFormat(histTextValue), 500)

    return () => clearTimeout(timeoutId)
  }, [histTextValue])
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
    return <div className='w-[400px]  border-2 border-black rounded-md pl-3'>
      <div className='text-center mb-6 mt-2 font-bold'>RAPORT</div>

      {'title' in reportedObject?
        <div>
          <div > <span className='font-semibold mr-1'>Nazwa zadania:</span> {reportedObject.title}</div>
          <br/>
          <div><span className='font-semibold mr-1'>Termin:</span> {format(new Date((reportedObject as AssignmentInterface).completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}</div>
          <br/>
          <div><span className='font-semibold mr-1'>Punkty do zdobycia:</span> {(reportedObject as AssignmentInterface).max_points}</div>
          <br/>
        </div>
          : <div><span className='font-semibold mr-1'>Nazwa grupy:</span> {(reportedObject as GroupInterface).name}</div>}

      <form onSubmit={(event) => {csvVersion ? handleCsvCreation(event) : handleReportCreation(event)}}>
        {csvVersion &&
            <fieldset>
                <legend>Elementy do wygenerowania:</legend>
                Średnia punktów: <input checked={reportCreate.avgResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          avgResult: !reportCreate.avgResult
                                        })}/><br/>
                Najwyższy wynik: <input checked={reportCreate.maxResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          maxResult: !reportCreate.maxResult
                                        })}/><br/>
                Najniższy wynik: <input checked={reportCreate.minResult}
                                        type={'checkbox'}
                                        onChange={() => handleChangingState({
                                          ...reportCreate,
                                          minResult: !reportCreate.minResult
                                        })}/><br/>
                Ilość zadań przesłanych po terminie: <input checked={reportCreate.late}
                                                            type={'checkbox'}
                                                            onChange={() => handleChangingState({
                                                              ...reportCreate,
                                                              late: !reportCreate.late
                                                            })}/><br/>
                <div className='mb-3 '><span className='font-semibold mr-1'>Przedziały do histogramu :</span> <input
                    className='w-12 px-2 py-1 mr-2 ml-2 border-b solid black' type={"text"} value={histTextValue}
                    onChange={(event) => setHistTextValue(event.target.value)}/>%
                </div>
            </fieldset>}
        <div className='flex justify-between pr-4 mb-2 '>
          <button className='bg-main_blue text-white rounded-md text-sm p-1 px-2' type={"submit"}>Utwórz</button>
          <button type={'reset'} className='bg-berry_red text-white rounded-md text-sm p-1 w-8'>X</button>
        </div>
      </form>
    </div>
  }
  else {return null}
}