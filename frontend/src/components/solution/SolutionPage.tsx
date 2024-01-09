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
      <div className='flex flex-col h-[calc(100dvh-315px)] overflow-y-hidden mb-3'>
          <div
            className="relative flex flex-col xl:flex-row  mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 fit pb-4 box-content overflow-y-auto pb-3 mb-3">

              <div className='flex flex-col gap-3'>
                  <div className="xl:absolute xl:top-3 xl:right-3   flex xl:flex-col ">
                      <div className="mt-[1px] mb-2 xl:mb-4 font-semibold">Punkty:</div>
                      <div className="ml-3 xl:ml-20 font-bold text-xl ">
                          {points} / {solutionExtended.assignment.maxPoints}
                      </div>
                  </div>
                  <div className='flex flex-col gap-3 border-b-2 border-main_blue pb-2 w-fit'>
                      <div className='flex flex-row mt-2'>
                          <p className="font-semibold w-48">Nazwa zadania: </p>
                          {solutionExtended.assignment.title}
                      </div>
                      <div className="text-border_gray flex flex-row">
                          <p className="font-semibold w-48">Opis zadania: </p>
                          <textarea disabled={true} className='border border-light-grey px-2 py-1 rounded-md shadow-md w-80 '>{solutionExtended.assignment.taskDescription}</textarea>
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
                            <textarea disabled={true} className='border border-light-grey px-2 py-1 rounded-md shadow-md w-80 '>{solutionExtended.comment}</textarea>
                        </div>
                      )}
                  </div>

                  <div className='flex flex-row '>
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
                    <div className='text-berry_red xl:border-none border-b-2 border-main_blue pb-3 w-fit xl:pb-8'>
                        Zadanie wysłane po terminie, zostanie automatycznie naliczona
                        kara {solutionExtended.assignment.autoPenalty}%
                    </div>)}
              </div>
              <div className='px-4 py-2 h-36 w-96 flex  text-bottom flex-col xl:pl-12'>
                  <div className='mb-2'>{(evaluation === undefined || reportedEvaluation !== undefined) ? (
                    <div>
                        {reportedEvaluation !== undefined &&
                          <div>Uwaga ucznia do zadania: {reportedEvaluation!.comment} </div>}
                        <div className='mb-2 mt-3 xl:mt-1'>{solutionExtended.id && group && fileFromDb !== undefined && (
                          <Link
                            to={`/group/${group.id}/advancedAssignment`}
                            state={{solutionExtended: solutionExtended}}
                            className=" underline font-semibold bottom-0 left-0 pb-3 mb-2 outline outline-2 px-3 pt-2 outline-main_blue rounded-md ">
                              Zaawansowane Sprawdzanie
                          </Link>
                        )}</div>
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
                  <div className='mb-5'>
                      <label className='flex align-top text-opacity-100 mt-4'> Komentarz: </label>
                      <textarea className='border rounded-sm border-border_gray ml-3 pl-2 pr-1 w-64 min-h-[120px] py-1'
                                onChange={(event) => setComment(event.target.value)} value={comment}
                                defaultValue={comment} maxLength={1500}/>
                  </div>
              </div>
          </div>

      </div>
    )
}

export default SolutionPage