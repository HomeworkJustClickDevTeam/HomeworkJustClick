import {Link, Outlet, useLocation} from "react-router-dom"
import { useEffect, useState } from "react"
import { SolutionExtended } from "../../types/types"
import AssigmentItem from "../assigments/assigmentDisplayer/assigmentItem/AssigmentItem"
import postgresqlDatabase from "../../services/postgresDatabase"
import { Rating } from "../evaluation/Rating"
import { SolutionFile } from "./file/SolutionFile"
import {format} from "date-fns";

function Solution() {
  let { state } = useLocation()
  const [solutionExtended] = useState<SolutionExtended>(state?.solution)
  const [points, setPoints] = useState<number>()
  const [showRating, setShowRating] = useState<boolean>(false)
  const [isCheck, setIsCheck] = useState<boolean>(false)
  useEffect(() => {
    postgresqlDatabase
      .get(`/evaluation/bySolution/${solutionExtended.id}`)
      .then((r) => {
        setPoints(r.data.result)
        setIsCheck(true)
      })

  }, [])

  const handleDisableRating = () => {
    setShowRating(false)
  }
  const handleShowRating = () => {
    setShowRating(true)
  }



  return (
    <div className='relative flex flex-col mt-6 border border-light_gray border-1 rounded-md pt-4 px-4 h-96 gap-2 mx-[7.5%]'>
        <div><span className="font-semibold">Nazwa zadania: </span>{solutionExtended.assignment.title}</div>
        <div className="text-border_gray"><span className="font-semibold">Opis zadania: </span>{solutionExtended.assignment.taskDescription}</div>
      <div className="absolute right-0 top-0 mr-8 mt-2 flex flex-col">
          <div className="mb-4 font-semibold">Punkty: </div>
          <div className="ml-20 font-bold text-xl ">{points} /{solutionExtended.assignment.max_points}</div>
      </div>
        <div className="flex ">
            <p className="mr-2">Przesłane pliki: </p>
            <SolutionFile solutionId={solutionExtended.id} />
        </div>
      {!isCheck ? (
          <div >
              <Link to={`/group/${solutionExtended.assignment.groupId}/solution/${solutionExtended.user.id}/${solutionExtended.assignment.id}/example`} className="absolute underline font-semibold bottom-0 left-0 mb-2 ml-4">Zaawansowane Sprawdzanie</Link>
              {showRating ? (
          <div>

            <Rating
              maxPoints={solutionExtended.assignment.max_points}
              points={points}
              setPoints={setPoints}
              solutionId={solutionExtended.id}
              groupId={solutionExtended.assignment.groupId}
            />
            <button onClick={handleDisableRating} className="mt-1 font-semibold underline">Schowaj Punkty</button>
          </div>

        ) : (
          <button onClick={handleShowRating} className="mt-1 font-semibold underline">Pokaż Punkty</button>
        )}</div>
      ) : null}


    </div>
  )
}
export default Solution
