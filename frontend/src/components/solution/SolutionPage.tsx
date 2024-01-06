import {Link, useLocation} from "react-router-dom"
import {useEffect, useState} from "react"
import {Rating} from "../evaluation/Rating"
import {SolutionFile} from "./SolutionFile"
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface"
import {useGetEvaluationBySolution} from "../customHooks/useGetEvaluationBySolution"
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {selectUserState} from "../../redux/userStateSlice";
import {useGetEvaluationPanelByUserIdAndAssigmentId} from "../customHooks/useGetEvaluationPanelByUserIdAndAssigmentId";
import {format} from "date-fns";
import {useGetFile} from "../customHooks/useGetFile";
import {useGetReportedEvaluation} from "../customHooks/useGetReportedEvaluation";

function SolutionPage() {
    let {state} = useLocation()
    const [solutionExtended] = useState<SolutionExtendedInterface>(
        state?.solution
    )
    const [points, setPoints] = useState<string>("0")

    const evaluation = useGetEvaluationBySolution(solutionExtended.id);
    const reportedEvaluation = useGetReportedEvaluation(evaluation?.id)
    const userState = useAppSelector(selectUserState)
    const evaluationPanel = useGetEvaluationPanelByUserIdAndAssigmentId(userState!.id, solutionExtended.assignment.id)
    const group = useAppSelector(selectGroup)
    const fileFromDb = useGetFile(solutionExtended.id, "solution")
    const [comment, setComment] = useState<string>("")

    const checkIfPenaltyOn = () => {
        if (solutionExtended.assignment.completionDatetime < solutionExtended.creationDateTime) {
            if (solutionExtended.assignment.autoPenalty > 0) {
                return true
            }
        }
        return false
    }
    useEffect(() => {
        if (evaluation) {
            setPoints(evaluation.result.toString())
            setComment(evaluation.comment)
        }
    }, [evaluation?.result])
    return (
        <div className='flex flex-col h-[calc(100vh-340px)] overflow-y-hidden mb-4'>
            <div
              className="relative flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 gap-2 box-content overflow-y-auto grid grid-cols-2">
                <div>
                    <div>
                        <span className="font-semibold">Nazwa zadania: </span>
                        {solutionExtended.assignment.title}
                    </div>
                    <div className="text-border_gray">
                        <span className="font-semibold">Opis zadania: </span>
                        {solutionExtended.assignment.taskDescription}
                    </div>
                    <div>
                        <span>Data ukończenia:  </span>
                        {format(new Date(solutionExtended.assignment.completionDatetime.toString()), "dd.MM.yyyy HH:mm")}
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
                            {points} / {solutionExtended.assignment.maxPoints}
                        </div>
                    </div>
                    <div className="flex ">
                        <p className="mr-2">Przesłane pliki: </p>
                        {fileFromDb !== undefined ? (
                          <SolutionFile fileFromDb={fileFromDb}/>
                        ) : (
                          <p>Brak</p>
                        )}
                    </div>
                    <div>
                        <span>Data przesłania zadania: </span>
                        {format(new Date(solutionExtended.creationDateTime.toString()), "dd.MM.yyyy HH:mm")}
                    </div>
                    {(checkIfPenaltyOn() &&
                      <div className='text-berry_red'>
                          Zadanie wysłane po terminie, zostanie automatycznie naliczona
                          kara {solutionExtended.assignment.autoPenalty}%
                      </div>)}
                    {(evaluation === undefined || reportedEvaluation !== undefined) ? (
                      <div>
                          {reportedEvaluation !== undefined &&
                            <div>Uwaga ucznia do zadania: {reportedEvaluation!.comment} </div>}
                          {solutionExtended.id && group && fileFromDb !== undefined && (
                            <Link
                              to={`/group/${group.id}/advancedAssignment`}
                              state={{solutionExtended: solutionExtended}}
                              className=" underline font-semibold bottom-0 left-0 mb-2">
                                Zaawansowane Sprawdzanie
                            </Link>
                          )}
                          <div>
                              <Rating
                                comment={comment}
                                maxPoints={solutionExtended.assignment.maxPoints}
                                points={points}
                                setPoints={setPoints}
                                reportedEvaluation={reportedEvaluation}
                                solutionId={solutionExtended.id}
                                groupId={solutionExtended.assignment.groupId}
                                assigmentCompletionDate={solutionExtended.assignment.completionDatetime}
                                solutionCreationDate={solutionExtended.creationDateTime}
                                penalty={solutionExtended.assignment.autoPenalty}
                                evaluationPanelButtons={evaluationPanel ? evaluationPanel.evaluationPanel.buttons : undefined}
                              />
                          </div>
                      </div>

                    ) : null}
                </div>
                <div className='px-4 py-2 h-36 w-96 flex rounded-md text-bottom'>
                    <label className='flex align-top text-opacity-100'> Komentarz:
                        <textarea className='h-32 border rounder-sm border-border_gray ml-3 pl-2 pr-1'
                                  onChange={(event) => setComment(event.target.value)} value={comment}
                                  defaultValue={comment}/>
                    </label>
                </div>
            </div>

        </div>
    )
}

export default SolutionPage
