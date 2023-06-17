import {Assigment, Solution} from "../../types/types";
import {useEffect, useState} from "react";
import {Evaluation} from "../../types/Evaluation";
import postgresqlDatabase from "../../services/postgresDatabase";
import {AxiosError} from "axios";
import Loading from "../animations/Loading";

export default function CheckedSolution(props:{solution:Solution, assignment:Assigment}){
  const [evaluation, setEvaluation] = useState<Evaluation | undefined>(undefined)
  const [isLoading, setIsLoading] = useState<boolean>(true)


  useEffect(()=>{
    postgresqlDatabase
      .get(`/evaluation/bySolution/${props.solution?.id}`)
      .then((response) => setEvaluation(response.data))
      .catch((error:AxiosError) => console.log(error))
  },[])

  if (evaluation === undefined) {
    return <Loading />
  }
  return(
    <>
      Zadanie: {props.assignment.title} <br/>
      Opis zadania: {props.assignment.taskDescription}<br/>
      Wynik: {evaluation?.result}/{props.assignment.max_points}

    </>
  )
}