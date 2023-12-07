import {format} from "date-fns";
import React, {useEffect, useState} from "react";
import {AssignmentCreateReportModel} from "../../types/AssignmentCreateReport.model";
import {GroupCreateReportModel} from "../../types/GroupCreateReport.model";
import {GroupInterface} from "../../types/GroupInterface";
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {f} from "msw/lib/core/RequestHandler-bb5cbb8f";

interface CreateReportPropsInterface{
  reportedObject: GroupInterface|AssignmentInterface
  handleReportCreation: (createdReportData: AssignmentCreateReportModel|GroupCreateReportModel, event: React.FormEvent<HTMLFormElement>)=>void
}
export const CreateReport = ({reportedObject,
                              handleReportCreation
}:CreateReportPropsInterface) =>{
  const [reportCreate, setReportCreate] = useState<AssignmentCreateReportModel|GroupCreateReportModel|undefined>(undefined)

  const handleChangingState = (newState: AssignmentCreateReportModel|GroupCreateReportModel) => {
    if('title' in newState) {
      setReportCreate(newState as AssignmentCreateReportModel)
    }
    else {
      setReportCreate(newState as GroupCreateReportModel)
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
        hist: []
      })
    }
    else {
      setReportCreate({
        assignmentId: reportedObject.id,
        maxResult: false,
        minResult: false,
        avgResult: false,
        late: false,
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

      <form onSubmit={(event) => handleReportCreation(reportCreate, event)}>
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