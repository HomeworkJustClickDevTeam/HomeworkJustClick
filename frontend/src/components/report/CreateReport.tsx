import {format} from "date-fns";
import React, {useEffect, useState} from "react";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {GroupInterface} from "../../types/GroupInterface";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {f} from "msw/lib/core/RequestHandler-bb5cbb8f";
import {createReportAssignment, createReportGroup} from "../../services/postgresDatabaseServices";
import {Link, useNavigate} from "react-router-dom";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {GroupReportModel} from "../../types/GroupReport.model";
export const CreateReport = ({reportedObject
}:{reportedObject: GroupInterface|AssignmentInterface}) =>{
  const [reportCreate, setReportCreate] = useState<AssignmentCreateReportModel|GroupCreateReportModel|undefined>(undefined)
  const navigate = useNavigate()
  const group = useAppSelector(selectGroup)
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
    if('assignmentId' in reportCreate){
      const response = await createReportAssignment(reportCreate)
      if(response?.status === 200){
        const assignmentReport = response.data as AssignmentReportModel
        navigate(`/group/${group!.id}/report`, {state:{reportObject: assignmentReport,
            date: 'completionDatetime' in reportedObject ? reportedObject.completionDatetime : undefined,
            title: 'title' in reportedObject ? reportedObject.title : undefined,
            maxPoints: 'max_points' in reportedObject ? reportedObject.max_points : undefined}})
      }

    }
    else {
      const response = await createReportGroup(reportCreate)
      if(response?.status === 200){
        const groupReport = response.data as GroupReportModel
        navigate(`/group/${group!.id}/report`, {state:{reportObject: groupReport,
                                                                  name: 'name' in reportedObject ? reportedObject.name : undefined}})
      }

    }
  }

  useEffect(() => {
    if('name' in reportedObject){
      setReportCreate({
        groupId: reportedObject.id,
        maxResult: false,
        minResult: false,
        avgResult: false,
        late: false,
        hist: [0]
      })
    }
    else {
      setReportCreate({
        assignmentId: reportedObject.id,
        maxResult: false,
        minResult: false,
        avgResult: false,
        late: false,
        hist: [0]
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

      <form onSubmit={(event) => handleReportCreation(event)}>
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
        </fieldset>
        <button type={"submit"}>Utwórz</button>
        <button type={'reset'}>X</button>
      </form>
    </div>
  }
  else {return null}
}