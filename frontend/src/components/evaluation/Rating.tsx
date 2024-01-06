import React, {SetStateAction, useState} from "react"
import {
  createEvaluationWithUserAndSolutionPostgresService, deleteEvaluationReportPostgresService,
  updateEvaluationByIdPostgresService
} from "../../services/postgresDatabaseServices"
import {useNavigate} from "react-router-dom"
import {selectUserState} from "../../redux/userStateSlice"
import {useAppSelector} from "../../types/HooksRedux"
import {Button} from "../../types/Table.model";
import {EvaluationCreateModel} from "../../types/EvaluationCreate.model";
import {toast} from "react-toastify";
import {EvaluationReport} from "../../types/EvaluationReport.model";
import {EvaluationReportResponse} from "../../types/EvaluationReportResponseModel";

interface RatingPropsInterface {
    maxPoints: number | undefined
    points: string
    setPoints: React.Dispatch<SetStateAction<string>>
    solutionId: number
    groupId: number
    evaluationPanelButtons: Button[] | undefined
    assigmentCompletionDate: Date,
    solutionCreationDate: Date | string,
    penalty: number,
    reportedEvaluation:EvaluationReportResponse|undefined
}

export function Rating({maxPoints,
                       points,
                       setPoints,
                       solutionId,
                       groupId,
                       assigmentCompletionDate,
                       solutionCreationDate,
                       evaluationPanelButtons,
                       penalty,
                       reportedEvaluation}: RatingPropsInterface) {
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
    const createEvaluation = (body: EvaluationCreateModel) => {
      createEvaluationWithUserAndSolutionPostgresService(userState!.id.toString(), solutionId.toString(), body)
        .then(() => {
          toast.success("Zadanie zostało ocenione.", {autoClose: 2000})
          navigate(`/group/${groupId}`)})
        .catch((e) => {
          toast.error("Nie udało się ocenić zadania.", {autoClose: 2000})
          console.log(e)
        })
    }

    const updateEvaluation = (body: EvaluationCreateModel) => {
      updateEvaluationByIdPostgresService(reportedEvaluation!.evaluation.id.toString(), body)
        .then(() => {
          deleteEvaluationReportPostgresService(reportedEvaluation!.id)
            .then(() => {
              toast.success("Ocena została poprawiona.", {autoClose: 2000})
              navigate(`/group/${groupId}`)
            })
            .catch((e) => {
              console.log(e)
            })
        })
        .catch((e) => {
          toast.error("Nie udało się poprawić oceny.", {autoClose: 2000})
          console.log(e)
        })
    }

    const handleMark = () => {
      if (pointsToSend !== undefined) {
        const body: EvaluationCreateModel = new EvaluationCreateModel(pointsToSend, userState!.id, solutionId, groupId, 0, false)
        if(reportedEvaluation){
          updateEvaluation(body)
        }
        else{
          createEvaluation(body)
        }
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
                setPoints(evaluationButton.points.toString());
                setPointsToSend(autoPenaltyCalculate(evaluationButton.points))
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
        setPointsToSend(autoPenaltyCalculate(+characterToJoin))
        return
      }
      else pointsAsString += characterToJoin
      if(+pointsAsString <= maxPoints!){
        setPoints(pointsAsString!)
        setPointsToSend(autoPenaltyCalculate(+pointsAsString!))
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
        if (evaluationPanelButtons) {
            createButtonFromEvaluationPanel(buttons, evaluationPanelButtons)
        } else {
            createCalculator(buttons)
        }
        return buttons
    }

    return (
        <div className="mt-4">
            <div className="relative flex w-72 gap-2 flex-wrap">{renderButtons()}</div>
            <button onClick={() => handleMark()} className="mt-4 px-6 py-1 bg-main_blue text-white rounded mb-4">Prześlij
                Ocenę
            </button>
        </div>
    )
}
