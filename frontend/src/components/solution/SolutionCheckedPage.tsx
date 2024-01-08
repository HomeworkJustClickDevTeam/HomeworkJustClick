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
      <div
        className="relative grid grid-cols-2 mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2">
        <div>
          <p>
            <span className="font-semibold">Zadanie: </span>
            {props.assignment.title}{" "}
          </p>
          <p>
            <span className="font-semibold">Opis zadania: </span>
            <textarea disabled={true}>{props.assignment.taskDescription}</textarea>
          </p>
          {props.solution.comment.length > 0 && (
            <p>
              <span className="font-semibold">Komentarz ucznia: </span>
              <textarea disabled={true}>{props.solution.comment}</textarea>
            </p>

            )}
          <p className="font-semibold">Przesłane pliki: </p>
          <div>{fileFromDb !== undefined ? (
            <SolutionFile fileFromDb={fileFromDb}/>
          ) : (
            <p>Brak</p>
          )}</div>
          <br/>
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
          <div className='px-4 py-2 h-36 w-96 flex rounded-md text-bottom'>
            <label className='flex align-top text-opacity-100'> Komentarz prowadzącego:
              <textarea className='h-32 border rounder-sm border-border_gray ml-3 pl-2 pr-1'
                        disabled={true}
                        defaultValue={evaluation?.comment}/>
            </label>
          </div>
        )}</div>
        <div className="absolute bottom-0 left-0 mb-6 ml-4">
          <p>Wynik: </p>
          <p className="font-bold text-xl mt-4">
            {evaluation?.result} / {props.assignment.maxPoints}
          </p>
        </div>

      </div>
    )
}
