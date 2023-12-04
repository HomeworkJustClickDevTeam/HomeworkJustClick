import { Link, useLocation } from "react-router-dom"
import { useState } from "react"
import { Rating } from "../evaluation/Rating"
import { SolutionFile } from "./SolutionFile"
import { SolutionExtendedInterface } from "../../types/SolutionExtendedInterface"
import { useGetEvaluationBySolution } from "../customHooks/useGetEvaluationBySolution"
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";

function SolutionPage() {
  let { state } = useLocation()
  const [solutionExtended] = useState<SolutionExtendedInterface>(
    state?.solution
  )
  const [points, setPoints] = useState<number>()
  const [showRating, setShowRating] = useState<boolean>(false)
  const evaluation = useGetEvaluationBySolution(solutionExtended.id)
  const group = useAppSelector(selectGroup)

  const handleDisableRating = () => {
    setShowRating(false)
  }
  const handleShowRating = () => {
    setShowRating(true)
  }

  function calculateMaxPoints() {
    const todayDate = new Date()
    if (solutionExtended.assignment.completionDatetime > todayDate) {
      return solutionExtended.assignment.max_points
    } else {
      const pointsPenalty =
        (solutionExtended.assignment.auto_penalty / 100) *
        solutionExtended.assignment.max_points
      return solutionExtended.assignment.max_points - pointsPenalty
    }
  }

  return (
    <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-96 gap-2">
      <div>
        <span className="font-semibold">Nazwa zadania: </span>
        {solutionExtended.assignment.title}
      </div>
      <div className="text-border_gray">
        <span className="font-semibold">Opis zadania: </span>
        {solutionExtended.assignment.taskDescription}
      </div>
      {solutionExtended.comment.length > 0 && (
        <div className="text-border_gray">
          <span className="font-semibold">Komentarz ucznia: </span>
          {solutionExtended.comment}
        </div>
      )}
      <div className="absolute right-0 top-0 mr-8 mt-2 flex flex-col">
        <div className="mb-4 font-semibold">Punkty:</div>
        <div className="ml-20 font-bold text-xl ">
          {points} /{solutionExtended.assignment.max_points}
        </div>
      </div>
      <div className="flex ">
        <p className="mr-2">Przesłane pliki: </p>
        {solutionExtended.id ? (
          <SolutionFile solutionId={solutionExtended.id} />
        ) : (
          <p>Brak</p>
        )}
      </div>
      {evaluation === undefined ? (
        <div>
          {solutionExtended.id && group && (
            <Link
              to={`/group/${group.id}/advancedAssignment`}
              state={{ solutionExtended: solutionExtended }}
              className="absolute underline font-semibold bottom-0 left-0 mb-2 ml-4">
              Zaawansowane Sprawdzanie
            </Link>
          )}
          {showRating ? (
            <div>
              <Rating
                maxPoints={calculateMaxPoints()}
                points={points}
                setPoints={setPoints}
                solutionId={solutionExtended.id}
                groupId={solutionExtended.assignment.groupId}
              />
              <button
                onClick={handleDisableRating}
                className="mt-1 font-semibold underline"
              >
                Schowaj Punkty
              </button>
            </div>
          ) : (
            <button
              onClick={handleShowRating}
              className="mt-1 font-semibold underline"
            >
              Pokaż Punkty
            </button>
          )}
        </div>
      ) : null}
    </div>
  )
}

export default SolutionPage
