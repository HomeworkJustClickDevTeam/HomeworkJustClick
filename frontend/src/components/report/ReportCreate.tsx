import {format} from "date-fns";
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
      console.log(reportCreate)
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
    return <div style={{borderStyle: "solid", borderWidth: "1px", height: "100"}}>
      <div style={{textAlign: "center"}}>RAPORT</div>
      <br/>
      {'title' in reportedObject?
        <div>
          <div>Nazwa zadania: {reportedObject.title}</div>
          <br/>
          <div>Termin: {format(new Date((reportedObject as AssignmentInterface).completionDatetime), "dd.MM.yyyy, HH:mm")}</div>
          <br/>
          <div>Punkty do zdobycia: {(reportedObject as AssignmentInterface).max_points}</div>
          <br/>
        </div>
        : <div>Nazwa grupy: {(reportedObject as GroupInterface).name}</div>}<br/>

      <form onSubmit={(event) => {csvVersion ? handleCsvCreation(event) : handleReportCreation(event)}}>
        {csvVersion &&
            <fieldset>
              <legend>Elementy do wygenerowania:</legend>
              Średnia punktów: <input checked={reportCreate.avgResult}
                                      type={'checkbox'}
                                      onChange={() => handleChangingState({...reportCreate, avgResult: !reportCreate.avgResult})}/><br/>
              Najwyższy wynik: <input checked={reportCreate.maxResult}
                                      type={'checkbox'}
                                      onChange={() => handleChangingState({...reportCreate, maxResult: !reportCreate.maxResult})}/><br/>
              Najniższy wynik: <input checked={reportCreate.minResult}
                                      type={'checkbox'}
                                      onChange={() => handleChangingState({...reportCreate, minResult: !reportCreate.minResult})}/><br/>
              Ilość zadań przesłanych po terminie: <input checked={reportCreate.late}
                                                          type={'checkbox'}
                                                          onChange={() => handleChangingState({...reportCreate, late: !reportCreate.late})}/><br/>
          </fieldset>}
        <div>Przedziały do histogramu (%): <input type={"text"} value={histTextValue} onChange={(event) => setHistTextValue(event.target.value)}/></div>
        <button type={"submit"}>Utwórz</button>
        <button type={'reset'}>X</button>
      </form>
    </div>
  }
  else {return null}
}