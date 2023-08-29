import React, {useContext, useState} from "react"
import {postEvaluationWithUserAndSolution} from "../../services/postgresDatabaseServices"
import {useNavigate} from "react-router-dom"
import {getUser} from "../../services/otherServices";
import HomePage from "../home/HomePage";

interface RatingPropsInterface {
  maxPoints: number
  points: number | undefined
  setPoints: (arg0: number) => void
  solutionId: number
  groupId: number
}
export function Rating({
  maxPoints,
  points,
  setPoints,
  solutionId,
  groupId,
}: RatingPropsInterface) {
  const [active, setActive] = useState<number>()
  const userState = getUser()
  const navigate = useNavigate()

  if(userState === undefined){
    navigate("/")
  }
  const handleMark = () => {
    const body = { result: points, grade: 0 }
    postEvaluationWithUserAndSolution(userState?.id as unknown as string, solutionId.toString(), body)
      .then(() => navigate(`/group/${groupId}`))
      .catch((e) => console.log(e))
  }

  const renderButtons = () => {
    const buttons = []
    for (let i = 0; i <= maxPoints; i++) {
      buttons.push(
        <button key={i} onClick={() => {setPoints(i); setActive(i)}} className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg focus:bg-hover_blue`}>
          {i}
        </button>
      )
    }
    return buttons
  }

  return (
    <div className="mt-4">
      <div className="relative flex w-72 gap-2 flex-wrap">{renderButtons()}</div>
      <button onClick={handleMark} className="mt-4 px-6 py-1 bg-main_blue text-white rounded">Przeslij Ocene</button>
    </div>
  )
}
