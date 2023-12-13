import {SolutionFile} from "./SolutionFile"
import {AssignmentInterface} from "../../types/AssignmentInterface"
import {SolutionInterface} from "../../types/SolutionInterface"
import {useGetEvaluationBySolution} from "../customHooks/useGetEvaluationBySolution"
import {Link} from "react-router-dom";
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface";
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {roleSlice, selectRole} from "../../redux/roleSlice";
import {ReportGrade} from "./ReportGrade";
import {useGetFile} from "../customHooks/useGetFile";

export default function SolutionCheckedPage(props: {
    solution: SolutionInterface
    assignment: AssignmentInterface
}) {
    const evaluation = useGetEvaluationBySolution(props.solution.id)
    const userState = useAppSelector(selectUserState)
    const userRole = useAppSelector(selectRole)
    const fileFromDb = useGetFile(props.solution.id, "solution")

    return (
        <div
            className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2">
            <p>
                <span className="font-semibold">Zadanie: </span>
                {props.assignment.title}{" "}
            </p>
            <p>
                <span className="font-semibold">Opis zadania: </span>
                {props.assignment.taskDescription}
            </p>
            {props.solution.comment.length > 0 && (
                <p>
                    <span className="font-semibold">Komentarz ucznia: </span>
                    {props.solution.comment}
                </p>
            )}
            <p className="font-semibold">Przesłane pliki: </p>
            {fileFromDb !== undefined ? (
                <SolutionFile fileFromDb={fileFromDb}/>
            ) : (
                <p>Brak</p>
            )}
            <br/>
            <div className="absolute bottom-0 left-0 mb-6 ml-4">
                <p>Wynik: </p>
                <p className="font-bold text-xl mt-4">
                    {evaluation?.result}/{props.assignment.max_points}
                </p>
            </div>
            {(userRole === 'Student' && evaluation) && <ReportGrade evaluationId={evaluation.id}/>}
            <Link
                to={`/group/${props.solution.groupId}/advancedAssignment`}
                state={{
                    solutionExtended: {
                        id: props.solution.id,
                        user: userState!,
                        assignment: props.assignment,
                        comment: props.solution.comment
                    }
                }}
                className="absolute underline font-semibold bottom-0 left-0 mb-2 ml-4">
                Komentarze prowadzącego do pliku
            </Link>
        </div>
    )
}
