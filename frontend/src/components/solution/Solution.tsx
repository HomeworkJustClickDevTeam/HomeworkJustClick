import {Link, Outlet, useLocation} from "react-router-dom"
import { useEffect, useState } from "react"
import { SolutionExtended } from "../../types/types"
import AssigmentItem from "../assigments/assigmentDisplayer/assigmentItem/AssigmentItem"
import postgresqlDatabase from "../../services/postgresDatabase"
import { Rating } from "../evaluation/Rating"
import { SolutionFile } from "./file/SolutionFile"

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
    <>
      <AssigmentItem
        idGroup={`${solutionExtended.assignment.groupId}`}
        assignment={solutionExtended.assignment}
      />
      <h1>
        Points: {points} /{solutionExtended.assignment.max_points}
      </h1>
      <SolutionFile solutionId={solutionExtended.id} />
      {!isCheck ? (
          <>
              <Link to={`/group/${solutionExtended.assignment.groupId}/solution/${solutionExtended.user.id}/${solutionExtended.assignment.id}/example`} >Zaawansowane Sprawdzanie</Link>
              <br/>
              {showRating ? (
          <div>

            <Rating
              maxPoints={solutionExtended.assignment.max_points}
              points={points}
              setPoints={setPoints}
              solutionId={solutionExtended.id}
              groupId={solutionExtended.assignment.groupId}
            />
            <button onClick={handleDisableRating}>Schowaj Punkty</button>
          </div>

        ) : (
          <button onClick={handleShowRating}>Pokaz Punkty</button>
        )}</>
      ) : null}
      <br/>

    </>
  )
}
export default Solution
