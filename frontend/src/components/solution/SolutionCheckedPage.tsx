import {SolutionFile} from "./SolutionFile"
import {AssignmentModel} from "../../types/Assignment.model"
import {SolutionInterface} from "../../types/SolutionInterface"
import {useGetEvaluationBySolution} from "../customHooks/useGetEvaluationBySolution"
import {Link} from "react-router-dom";
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {roleSlice, selectRole} from "../../redux/roleSlice";
import {ReportGrade} from "./ReportGrade";
import {useGetFile} from "../customHooks/useGetFile";
import {useGetCommentsImageByFile} from "../customHooks/useGetCommentsImageByFile";
import {useGetCommentsTextByFile} from "../customHooks/useGetCommentsTextByFile";
import {useGetReportedEvaluation} from "../customHooks/useGetReportedEvaluation";
import { FaDownload } from "react-icons/fa6";

export default function SolutionCheckedPage(props: {
    solution: SolutionInterface
    assignment: AssignmentModel
}) {
    const evaluation = useGetEvaluationBySolution(props.solution.id)
    const evaluationReport = useGetReportedEvaluation(evaluation?.id)
    const userState = useAppSelector(selectUserState)
    const userRole = useAppSelector(selectRole)
    const fileFromDb = useGetFile(props.solution.id, "solution")
    const {commentsImage} = useGetCommentsImageByFile(fileFromDb?.id, "size=1")
    const {comments: txtComments} = useGetCommentsTextByFile(fileFromDb?.id, "size=1")

    return (
        <div className='flex flex-col overflow-y-hidden h-[calc(100dvh-270px)] xl:h-[calc(100dvh-360px)] '>
      <div
        className="relative mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-2 h-fit gap-2 pb-3 box-content overflow-y-auto ">
        <div className=' flex flex-col xl:flex-row xl:border-none xl:border-b-2 border-main_blue pb-3 w-fit border-l-2 pl-2'>
          <div className='mr-8 xl:mr-12'>
              <div className='flex'>
                <p className="font-semibold mr-2">Zadanie: </p>
                {props.assignment.title}{" "}
              </div>
              <div>
                <label className='flex align-top mt-2 w-fit font-semibold mr-2'>Opis zadania: </label>
                <textarea disabled={true} className='mt-3  border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-96 lg:w-fit lg:min-w-[300px] min-h-[80px] lg:ml-0 ml-32 mb-5 xl:mb-0'>{props.assignment.taskDescription}</textarea>
              </div>
          </div>
              <div className='flex flex-col xl:border-l-2 xl:border-main_blue xl:pl-6'>
              <div className='mt-2'>
                  <p className="font-semibold">Przesłane pliki: </p>
                  <div>{fileFromDb !== undefined ? (
                      <div className='flex'><FaDownload className='mt-1 mr-2'/><SolutionFile fileFromDb={fileFromDb}/></div>
                  ) : (
                      <p>Brak</p>
                  )}
                  </div>
              </div>
              <div>
              {props.solution.comment.length > 0 && (
                <div>
                    <label className='flex align-top mt-2 w-fit font-semibold mr-2'>Komentarz ucznia: </label>
                  <textarea disabled={true} className='mt-3  border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-80 lg:w-fit lg:min-w-[300px] min-h-[150px] lg:ml-0 ml-32 mb-5 xl:mb-0'>{props.solution.comment}</textarea>
                </div>

              )}</div>



              <div className="fixed fixed top-[50%] left-[35%]">{(userRole === 'Student' && evaluation) && (
                (evaluationReport === undefined) ?
                  <ReportGrade evaluationId={evaluation.id}/>
                  : <div>Zadanie zostało zaznaczone jako niepoprawnie ocenione, z komentarzem:<br/>
                    <textarea disabled={true}>{evaluationReport!.comment}</textarea></div>
                )}
              </div>
              {((commentsImage.length !== 0) || (txtComments.length !== 0)) && <Link
                  to={`/group/${props.solution.groupId}/advancedAssignment`}
                  state={{
                    solutionExtended: {
                      id: props.solution.id,
                      user: userState!,
                      assignment: props.assignment,
                      comment: props.solution.comment
                    }
                  }}
                  className="underline font-semibold bottom-0 left-0 mb-2">
              Komentarze prowadzącego do pliku
          </Link>}
        </div>
        <div>{evaluation?.comment !== undefined && (
          <div className='xl:pl-8 '>
              <label className='flex align-top xl:mt-[62px] w-fit font-semibold mr-2'> Komentarz prowadzącego:</label>
              <textarea className='mt-3  border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-80  lg:min-w-[350px] min-h-[150px] lg:ml-0 ml-32 mb-5 xl:mb-0'
                        disabled={true}
                        defaultValue={evaluation?.comment}/>

          </div>
        )}</div>
        </div>
        <div className="absolute top-0 right-4 font-bold text-xl mt-4">
          <p>Wynik: </p>
          <p className="">
            {evaluation?.result} / {props.assignment.maxPoints}
          </p>
        </div>
      </div>
      </div>
    )
}
