import {format} from "date-fns";
import React from "react";

interface EvaluationsCreateReportPropsInterface{
  evaluationName: string
  date: Date
  maxPoints: number
  handleReportCreation: (event: React.FormEvent<HTMLFormElement>)=>void
}
export const CreateReport = ({evaluationName,
                              date,
                              maxPoints,
                              handleReportCreation}:EvaluationsCreateReportPropsInterface) =>{
  return <div style={{borderStyle: "solid", borderWidth: "1px", height: "100"}}>
    <div style={{textAlign: "center"}}>RAPORT</div><br/>
    <div>Nazwa zadania: {evaluationName}</div><br/>
    <div>Termin: {format(date, "dd.MM.yyyy, HH:mm")}</div><br/>
    <div>Punkty do zdobycia: {maxPoints}</div><br/>
    <form onSubmit={(event) => handleReportCreation(event)}>
      <fieldset>
        <legend>Elementy do wygenerowania:</legend>
        Średnia punktów: <input type={'checkbox'} id={"avgResultedPoints"}/><br/>
        Najwyższy wynik: <input type={'checkbox'} id={"maxResultedPoints"}/><br/>
        Najniższy wynik: <input type={'checkbox'} id={"minResultedPoints"}/><br/>
        Ilość zadań przesłanych po terminie: <input type={'checkbox'} id={"handedLate"}/><br/>
      </fieldset>
      <button type={"submit"}>Utwórz</button> <button type={'reset'}>X</button>
    </form>
  </div>

}