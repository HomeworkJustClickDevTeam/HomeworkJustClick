import {format} from "date-fns";

interface ReportPagePropsInterface{
  title:string,
  maxResultedPoints: number,
  minResultedPoints: number,
  avgResultedPoints: number
  maxPoints: number
  avgPercent: number
  date: Date
  lateHanded: number
}
export const ReportPage = ({title,
                             avgResultedPoints,
                             minResultedPoints,
                             maxResultedPoints,
                             maxPoints,
                             avgPercent,
                             lateHanded,
                             date}: ReportPagePropsInterface) =>{
  return <div>
    <div>RAPORT: {title} {format(date, "dd.MM.yyyy, HH:mm")}</div><div>{maxPoints}</div><br/>
    <div>Najwyższa liczba punktów: {maxResultedPoints}</div><br/>
    <div>Najniższa liczba punktów: {minResultedPoints}</div><br/>
    <div>Średnia liczba punktów: {avgResultedPoints}</div><br/>
    <div>Średni procent: {avgPercent}</div><br/>
    <div>Ilość zadań przesłanych po terminie: {lateHanded}</div><br/>
  </div>
}