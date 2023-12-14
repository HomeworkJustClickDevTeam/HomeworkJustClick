import {Link, useLocation} from "react-router-dom"
import {useState} from "react"
import {Rating} from "../evaluation/Rating"
import {SolutionFile} from "./SolutionFile"
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface"
import {useGetEvaluationBySolution} from "../customHooks/useGetEvaluationBySolution"
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {selectUserState} from "../../redux/userStateSlice";
import {useGetEvaluationPanelByUserIdAndAssigmentId} from "../customHooks/useGetEvaluationPanelByUserIdAndAssigmentId";
import {format, parseISO} from "date-fns";
import {useGetFile} from "../customHooks/useGetFile";

function SolutionPage() {
    let {state} = useLocation()
    const [solutionExtended] = useState<SolutionExtendedInterface>(
        state?.solution
    )
    const [points, setPoints] = useState<string>("0")
    const [showRating, setShowRating] = useState<boolean>(false)
    const evaluation = useGetEvaluationBySolution(solutionExtended.id);
    const userState = useAppSelector(selectUserState)
    const evaluationPanel = useGetEvaluationPanelByUserIdAndAssigmentId(userState!.id, solutionExtended.assignment.id)
    const group = useAppSelector(selectGroup)
    const fileFromDb = useGetFile(solutionExtended.id, "solution")
    const handleDisableRating = () => {
        setShowRating(false)
    }
    const handleShowRating = () => {
        setShowRating(true)
    }
    const checkIfPenaltyOn = () => {
        if (!evaluation) {
            if (solutionExtended.assignment.completionDatetime < solutionExtended.creationDateTime) {
                if (solutionExtended.assignment.auto_penalty > 0) {
                    return true
                }
            }
        }
        return false
    }

    return (
        <div
            className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-96 gap-2">
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
                    {points} /{solutionExtended.assignment.max_points}
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
                <div>
                    Zadanie wysłane po terminie, zostanie automatycznie naliczona
                    kara {solutionExtended.assignment.auto_penalty}%
                </div>}
            {evaluation === undefined ? (
                <div>
                    {solutionExtended.id && group && fileFromDb!==undefined && (
                        <Link
                            to={`/group/${group.id}/advancedAssignment`}
                            state={{solutionExtended: solutionExtended}}
                            className="absolute underline font-semibold bottom-0 left-0 mb-2 ml-4">
                            Zaawansowane Sprawdzanie
                        </Link>
                    )}
                    {showRating ? (
                        <div>
                            <Rating
                                maxPoints={!evaluationPanel ? solutionExtended.assignment.max_points : undefined}
                                points={points}
                                setPoints={setPoints}
                                solutionId={solutionExtended.id}
                                groupId={solutionExtended.assignment.groupId}
                                assigmentCompletionDate={solutionExtended.assignment.completionDatetime}
                                solutionCreationDate={solutionExtended.creationDateTime}
                                penalty={solutionExtended.assignment.auto_penalty}
                                evaluationPanelButtons={evaluationPanel ? evaluationPanel.evaluationPanel.buttons : undefined}
                            />
                            <button
                                onClick={handleDisableRating}
                                className="mt-1 font-semibold underline"
                            >
                                Schowaj Punkty
                            </button>
                        </div>
                    ) : (
                        <button
                            onClick={handleShowRating}
                            className="mt-1 font-semibold underline"
                        >
                            Pokaż Punkty
                        </button>
                    )}
                </div>
            ) : null}
        </div>
    )
}

export default SolutionPage
