import React, {useState} from "react"
import {createEvaluationWithUserAndSolution} from "../../services/postgresDatabaseServices"
import {useNavigate} from "react-router-dom"
import {selectUserState} from "../../redux/userStateSlice"
import {useAppSelector} from "../../types/HooksRedux"
import {Button} from "../../types/Table.model";
import {EvaluationCreateModel} from "../../types/EvaluationCreate.model";

interface RatingPropsInterface {
    maxPoints: number
    points: number | undefined
    setPoints: (arg0: number) => void
    solutionId: number
    groupId: number
    evaluationPanelButtons: Button[] | undefined
}

export function Rating({
                           maxPoints,
                           points,
                           setPoints,
                           solutionId,
                           groupId,
                           evaluationPanelButtons,
                       }: RatingPropsInterface) {
  const [active, setActive] = useState<number>()
  const navigate = useNavigate()
  const userState = useAppSelector(selectUserState)
  const handleMark = () => {
    if(points !== undefined){
      const body:EvaluationCreateModel = new EvaluationCreateModel(points, userState!.id, solutionId, groupId, 0, false)
      createEvaluationWithUserAndSolution(userState!.id.toString(), solutionId.toString(), body)
        .then(() => navigate(`/group/${groupId}`))
        .catch((e) => console.log(e))
    }
  }

    function createButtonFromMaxPoints(buttons: any[]) {
        for (let i = 0; i <= maxPoints; i++) {
            buttons.push(
                <button key={i} onClick={() => {
                    setPoints(i);
                    setActive(i)
                }}
                        className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg focus:bg-hover_blue`}>
                    {i}
                </button>
            )
        }
    }


    function createButtonFromEvaluationPanel(buttons: any[], evaluationPanelButtons: Button[]) {
        evaluationPanelButtons.forEach((evaluationButton) => {
            buttons.push(<button key={evaluationButton.points} onClick={() => {
                setPoints(evaluationButton.points);
                setActive(evaluationButton.points)
            }}
                                 className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg focus:bg-hover_blue`}>
                {evaluationButton.points}
            </button>)
        })
    }

    const renderButtons = () => {
        const buttons: any[] = []
        if (evaluationPanelButtons) {
            createButtonFromEvaluationPanel(buttons, evaluationPanelButtons)
        } else {
            createButtonFromMaxPoints(buttons)
        }
        return buttons
    }

  return (
    <div className="mt-4">
      <div className="relative flex w-72 gap-2 flex-wrap">{renderButtons()}</div>
      <button onClick={()=>handleMark()} className="mt-4 px-6 py-1 bg-main_blue text-white rounded">Prześlij Ocenę</button>
    </div>
  )
}
