import { Assigment, Solution } from "../../types/types"
import { useEffect, useState } from "react"
import { Evaluation } from "../../types/Evaluation"
import postgresqlDatabase from "../../services/postgresDatabase"
import { AxiosError } from "axios"
import Loading from "../animations/Loading"
import { SolutionFile } from "./SolutionFile"

export default function CheckedSolution(props: {
  solution: Solution
  assignment: Assigment
}) {
  const [evaluation, setEvaluation] = useState<Evaluation | undefined>(
    undefined
  )

  useEffect(() => {
    postgresqlDatabase
      .get(`/evaluation/bySolution/${props.solution?.id}`)
      .then((response) => setEvaluation(response.data))
      .catch((error: AxiosError) => console.log(error))
  }, [])

  if (evaluation === undefined) {
    // return <Loading />
  }
  return (
      <div className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
          <p><span className='font-semibold'>Zadanie: </span>{props.assignment.title} </p>
          <p><span className='font-semibold'>Opis zadania: </span>{props.assignment.taskDescription}</p>
          <p className="font-semibold">Przesłane pliki: </p>
          <SolutionFile solutionId={props.solution.id} />
      <br />
          <div className="absolute bottom-0 left-0 mb-6 ml-4">
              <p>Wynik: </p>
              <p className='font-bold text-xl mt-4'>{evaluation?.result}/{props.assignment.max_points}</p>
          </div>

    </div>
  )
}
