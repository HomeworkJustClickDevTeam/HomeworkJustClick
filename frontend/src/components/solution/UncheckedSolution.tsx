import { Assigment, Solution } from "../../types/types"

import { SolutionFile } from "./file/SolutionFile"

export default function UncheckedSolution(props: {
  solution: Solution
  assignment: Assigment
}) {
  return (
    <>
      Zadanie: {props.assignment.title} <br />
      Opis zadania: {props.assignment.taskDescription}
      <br />
      Wynik: ?/{props.assignment.max_points}
      <SolutionFile solutionId={props.solution.id} />
    </>
  )
}
