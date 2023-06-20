import { useContext } from "react"
import { RatingProps } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import userContext from "../../UserContext"
import { useNavigate } from "react-router-dom"

export function Rating({
  maxPoints,
  points,
  setPoints,
  solutionId,
  groupId,
}: RatingProps) {
  const { userState } = useContext(userContext)
  const navigate = useNavigate()
  const handleMark = () => {
    const body = { result: points, grade: 0 }
    console.log(userState.userId)
    console.log(solutionId)
    postgresqlDatabase
      .post(
        `/evaluation/withUserAndSolution/${userState.userId}/${solutionId}`,
        body
      )
      .then(() => navigate(`/group/${groupId}`))
      .catch((e) => console.log(e))
  }

  const renderButtons = () => {
    const buttons = []
    for (let i = 1; i <= maxPoints; i++) {
      buttons.push(
        <button key={i} onClick={() => setPoints(i)}>
          {i}
        </button>
      )
    }
    return buttons
  }

  return (
    <div>
      <div>{renderButtons()}</div>
      <button onClick={handleMark}>Przeslij Ocene</button>
    </div>
  )
}
