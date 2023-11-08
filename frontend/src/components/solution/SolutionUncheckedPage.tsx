import { SolutionFile } from "./SolutionFile"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { SolutionInterface } from "../../types/SolutionInterface"

export default function SolutionUncheckedPage(props: {
  solution: SolutionInterface
  assignment: AssignmentInterface
}) {
  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
      <p><span className='font-semibold'>Zadanie: </span>{props.assignment.title} </p>
      <p><span className='font-semibold'>Opis zadania: </span>{props.assignment.taskDescription}</p>
      <div>
        <p className="font-semibold">Przesłane pliki: </p>
        {props.solution ? <SolutionFile solutionId={props.solution.id}/> : <p>Brak</p>}
      </div>
      <br/>
      <div className="absolute bottom-0 left-0 mb-6 ml-4">
        <p>Wynik: </p>
        <p className='font-bold text-xl mt-4'>? /{props.assignment.max_points} - Praca jeszcze nie została
          sprawdzona </p>
      </div>

    </div>
  )
}
