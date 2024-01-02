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

    const checkIfPenaltyOn = () => {
        if (!evaluation) {
            if (solutionExtended.assignment.completionDatetime < solutionExtended.creationDateTime) {
                if (solutionExtended.assignment.autoPenalty > 0) {
                    return true
                }
            }
        }
        return false
    }

    useEffect(() => {
        if (evaluation) {
            setPoints(evaluation.result.toString())
        }
    }, [evaluation?.result])
    return (
        <div className='flex flex-col h-[calc(100vh-340px)] overflow-y-hidden mb-4'>
            <div
                className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 gap-2 box-content overflow-y-auto">
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
                        {points} /{solutionExtended.assignment.maxPoints}
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
                {(checkIfPenaltyOn()) &&
                    <div className='text-berry_red'>
                        Zadanie wysłane po terminie, zostanie automatycznie naliczona
                        kara {solutionExtended.assignment.autoPenalty}%
                    </div>}
                {evaluation === undefined ? (
                    <div>
                        {solutionExtended.id && group && fileFromDb !== undefined && (
                            <Link
                                to={`/group/${group.id}/advancedAssignment`}
                                state={{solutionExtended: solutionExtended}}
                                className="absolute underline font-semibold bottom-0 left-0 mb-2 ml-4">
                                Zaawansowane Sprawdzanie
                            </Link>
                        )}
                        <div>
                            <Rating
                                maxPoints={!evaluationPanel ? solutionExtended.assignment.maxPoints : undefined}
                                points={points}
                                setPoints={setPoints}
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
                {
                    reportedEvaluation && <div>Uwaga ucznia do zadania: {reportedEvaluation.comment} </div>
                }
            </div>
        </div>
    )
}

export default SolutionPage
