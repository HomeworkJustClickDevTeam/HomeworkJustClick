import {Chart} from "react-google-charts";
import {AssignmentModel} from "../../types/Assignment.model";
import {useEffect, useState} from "react";
import {AssignmentReportModel} from "../../types/AssignmentReport.model";
import {createReportAssignment} from "../../services/postgresDatabaseServices";

export const AssignmentPieChart = ({assignment}:{assignment:AssignmentModel}) =>{
  const [studentsResults, setStudentsResults] = useState<any>(undefined)
  useEffect(() => {
    createReportAssignment({
      assignmentId: assignment.id,
      avgResult: false,
      late: false,
      maxResult: false,
      hist: [],
      minResult: false
    })
      .then((response)=>{
        if(response?.status === 200){
          const assignmentReport: AssignmentReportModel = response.data
          if(assignmentReport.students !== null){
            const resultsMap = new Map<number, number>()
            assignmentReport.students.map(studentResult=>{
              if(!resultsMap.has(studentResult.result)) resultsMap.set(studentResult.result, 1)
              else resultsMap.set(studentResult.result, resultsMap.get(studentResult.result)!+1)
            })
            const preparedData:any = []
            resultsMap.forEach((value, key)=>{
              preparedData.push([key.toString(), value])
            })
            preparedData.unshift(["Wynik puntowy", "Ilość studentów z tym wynikiem"])
            setStudentsResults(preparedData)
          }
          else setStudentsResults(response.data.description)
        }
      })
      .catch(error => console.error(error))

  }, []);

  if(studentsResults===undefined || studentsResults.length === 1) return (null)
  return <Chart
    chartType={"PieChart"}
    data={studentsResults}
  />
}