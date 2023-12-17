import React, {SetStateAction, useState} from "react"
import {createEvaluationWithUserAndSolution} from "../../services/postgresDatabaseServices"
import {useNavigate} from "react-router-dom"
import {selectUserState} from "../../redux/userStateSlice"
import {useAppSelector} from "../../types/HooksRedux"
import {Button} from "../../types/Table.model";
import {EvaluationCreateModel} from "../../types/EvaluationCreate.model";

interface RatingPropsInterface {
    maxPoints: number | undefined
    points: string
    setPoints: React.Dispatch<SetStateAction<string>>
    solutionId: number
    groupId: number
    evaluationPanelButtons: Button[] | undefined
    assigmentCompletionDate: Date,
    solutionCreationDate: Date | string,
    penalty: number
}

export function Rating({
                           maxPoints,
                           points,
                           setPoints,
                           solutionId,
                           groupId,
                           assigmentCompletionDate,
                           solutionCreationDate,
                           evaluationPanelButtons,
                           penalty
                       }: RatingPropsInterface) {
    const [active, setActive] = useState<number>()
    const navigate = useNavigate()
    const userState = useAppSelector(selectUserState)
    const [pointsToSend,setPointsToSend] = useState<number|undefined>(undefined)
    const autoPenaltyCalculate = (points: number) => {
        if (assigmentCompletionDate > solutionCreationDate) {
            return points;
        }
        const pointsPenalty = (penalty / 100) * points
        return points - pointsPenalty

    }

    const handleMark = () => {
        if (pointsToSend !== undefined) {
            const body: EvaluationCreateModel = new EvaluationCreateModel(pointsToSend, userState!.id, solutionId, groupId, 0, false)
            createEvaluationWithUserAndSolution(userState!.id.toString(), solutionId.toString(), body)
                .then(() => navigate(`/group/${groupId}`))
                .catch((e) => console.log(e))
        }
    }

    function createButtonFromMaxPoints(buttons: any[]) {
        if (maxPoints) {
          if(maxPoints <= 10){
            for (let i = 0; i <= maxPoints; i++) {
              buttons.push(
                <button key={i} onClick={() => {
                  const points = autoPenaltyCalculate(i)
                  setPointsToSend(i)
                  setPoints(points.toString());
                  setActive(i)
                }}
                        className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg focus:bg-hover_blue`}>
                  {i}
                </button>
              )
            }
          }
          else{
            createCalculator(buttons)
          }
        }
    }


    function createButtonFromEvaluationPanel(buttons: any[], evaluationPanelButtons: Button[]) {
        evaluationPanelButtons.forEach((evaluationButton) => {
            buttons.push(<button key={evaluationButton.points} onClick={() => {
                const points = autoPenaltyCalculate(evaluationButton.points)
                setPoints(points.toString());
                setActive(evaluationButton.points)
                setPointsToSend(evaluationButton.points)
            }}
                                 className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg focus:bg-hover_blue`}>
                {evaluationButton.points}
            </button>)
        })
    }

    const setPointsResult = (characterToJoin:string) =>{
      let pointsAsString = points
      if(characterToJoin === '<-') {
        pointsAsString = pointsAsString.slice(0, -1)
        if (pointsAsString.length === 0) pointsAsString = '0'
      }
      else if(pointsAsString === '0' && characterToJoin!=="."){
        setPoints(characterToJoin)
        setPointsToSend(+characterToJoin)
        return
      }
      else pointsAsString += characterToJoin
      if(+pointsAsString <= maxPoints!){
        setPoints(pointsAsString!)
        setPointsToSend(+pointsAsString!)
      }
    }

    const createCalculator = (buttons: any[]) =>{
      for (let i = 1; i <= 9; i++) {
        buttons.push(
          <button key={i} onClick={() => {
            setPointsResult(i.toString());
          }}
                  className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg`}>
            {i}
          </button>
        )
      }
      buttons.push(
        <button key={'.'} onClick={() => {
          setPointsResult('.');
        }}
                className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg`}>
          {'.'}
        </button>
      )
      buttons.push(
        <button key={0} onClick={() => {
          setPointsResult('0');
        }}
                className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg `}>
          {0}
        </button>
      )
      buttons.push(
        <button key={'<-'} onClick={() => {
          setPointsResult('<-')
        }}
                className={`border border-black w-20 h-6 text-center rounded-md hover:bg-lilly-bg`}>
          {'<-'}
        </button>
      )

    }

    const renderButtons = () => {
        const buttons: any[] = []
        if (evaluationPanelButtons && !maxPoints) {
            createButtonFromEvaluationPanel(buttons, evaluationPanelButtons)
        } else {
            createButtonFromMaxPoints(buttons)
        }
        return buttons
    }

    return (
        <div className="mt-4">
            <div className="relative flex w-72 gap-2 flex-wrap">{renderButtons()}</div>
            <button onClick={() => handleMark()} className="mt-4 px-6 py-1 bg-main_blue text-white rounded">Prześlij
                Ocenę
            </button>
        </div>
    )
}
