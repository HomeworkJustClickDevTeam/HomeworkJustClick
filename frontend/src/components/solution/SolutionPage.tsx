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
import {AdvancedEvaluationExtensionType} from "../../types/AdvancedEvaluationExtensionType";
import { FaDownload } from "react-icons/fa6";

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
    console.table(solutionExtended.assignment)
    return (
        <div className='flex flex-col h-[calc(100vh-340px)] overflow-y-hidden mb-4'>
            <div
              className="relative flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 gap-2 box-content overflow-y-auto grid grid-cols-2">
                <div className='flex flex-col gap-3'>
                    <div className='flex flex-col gap-3 border-b-2 border-main_blue pb-2 w-fit'>
                        <div className='flex flex-row mt-2'>
                            <p className="font-semibold w-48">Nazwa zadania: </p>
                            {solutionExtended.assignment.title}
                        </div>
                        <div className="text-border_gray flex flex-row">
                            <p className="font-semibold w-48">Opis zadania: </p>
                            <p className='border border-light-grey px-2 py-1 rounded-md shadow-md w-72'>{solutionExtended.assignment.taskDescription}</p>
                        </div>
                        <div className='flex flex-row'>
                            <p className='w-48'>Data ukończenia:  </p>
                            {format(new Date(solutionExtended.assignment.completionDatetime.toString()), "dd.MM.yyyy HH:mm")}
                        </div>
                    </div>
                    <div className='mt-1'>
                        {solutionExtended.comment.length > 0 && (
                          <div className="text-border_gray flex flex-row">
                              <p className="font-semibold w-48 ">Komentarz ucznia: </p>
                              <p className='border border-light-grey px-2 py-1 rounded-md shadow-md w-72'>{solutionExtended.comment}</p>
                          </div>
                        )}
                    </div>
                    <div className="fixed right-[calc(7.5%+28px)] top-[340px] mr-8 mt-2 flex flex-col">
                        <div className="mb-4 font-semibold">Punkty:</div>
                        <div className="ml-20 font-bold text-xl ">
                            {points} / {solutionExtended.assignment.maxPoints}
                        </div>
                    </div>
                    <div className='flex flex-row'>
                        <p className="w-48">Przesłane pliki: </p>
                        {fileFromDb !== undefined ? (<div className='flex flex-row'>
                                <FaDownload className='mr-2 mt-1'/><SolutionFile fileFromDb={fileFromDb}/></div>
                        ) : (
                          <p>Brak</p>
                        )}
                    </div>
                    <div className='flex'>
                        <p className='w-48'>Data przesłania zadania: </p>
                        {format(new Date(solutionExtended.creationDateTime.toString()), "dd.MM.yyyy HH:mm")}
                    </div>
                    {(checkIfPenaltyOn() &&
                      <div className='text-berry_red'>
                          Zadanie wysłane po terminie, zostanie automatycznie naliczona
                          kara {solutionExtended.assignment.autoPenalty}%
                      </div>)}
                </div>
                <div className='px-4 py-2 h-36 w-96 flex rounded-md text-bottom flex-col'>
                    <div className='mb-2'>{(evaluation === undefined || reportedEvaluation !== undefined) ? (
                        <div>
                            {reportedEvaluation !== undefined &&
                                <div>Uwaga ucznia do zadania: {reportedEvaluation!.comment} </div>}
                            {solutionExtended.id && group && fileFromDb !== undefined && solutionExtended.assignment.advancedEvaluation && AdvancedEvaluationExtensionType.includes("." + fileFromDb.format) && (
                                <Link
                                    to={`/group/${group.id}/advancedAssignment`}
                                    state={{solutionExtended: solutionExtended}}
                                    className=" underline font-semibold bottom-0 left-0 pb-5">
                                    Zaawansowane Sprawdzanie
                                </Link>
                            )}
                        </div>

                    ) : null}</div>
                    <div>
                        <p className='mb-2 '>Wpisz ocenę: </p>
                        <div className='border border-light_gray rounded-md w-fit pl-7 pb-3 shadow-md focus:border-main_blue'>
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
                    <label className='flex align-top text-opacity-100 mt-4'> Komentarz: </label>
                        <textarea className='border rounder-sm border-border_gray ml-3 pl-2 pr-1 w-64 min-h-[120px]'
                                  onChange={(event) => setComment(event.target.value)} value={comment}
                                  defaultValue={comment}/>

                </div>
            </div>

        </div>
    )
}

export default SolutionPage
