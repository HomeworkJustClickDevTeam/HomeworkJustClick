import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {selectRole} from "../../redux/roleSlice";
import React, {useEffect} from "react";
import {useGetReportedEvaluations} from "../customHooks/useGetReportedEvaluations";
import {Link, useNavigate} from "react-router-dom";


export const EvaluationReportedPage = () => {
    const navigate = useNavigate()
    const group = useAppSelector(selectGroup)
    const role = useAppSelector(selectRole)
    const reportedEvaluations = useGetReportedEvaluations(group?.id)

    useEffect(() => {
        if (role !== "Teacher") {
            navigate(`-/group/${group?.id}`)
        }
    }, [role])

    return (
        <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden'>
            <ul className='flex flex-col box-content overflow-y-scroll mb-4'>
                {reportedEvaluations?.map((evaluationReportResponse) => (
                    <li key={evaluationReportResponse.id
                        + evaluationReportResponse.solution.id
                        + evaluationReportResponse.evaluation.id}>
                        <Link
                            to={`/group/${group?.id}/solution/${evaluationReportResponse.solution.user.id}/${evaluationReportResponse.solution.assignment.id}`}
                            state={{solution: evaluationReportResponse.solution}}
                            className="flex relative border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center text-center gap-2">
                            <div className="flex-col pl-10 w-40 text-left">
                                <div>{evaluationReportResponse.solution.user.firstname}<br/> {evaluationReportResponse.solution.user.lastname}
                                </div>
                            </div>
                            <div className="font-semibold underline text-left w-[40%]">
                                {evaluationReportResponse.solution.assignment.title}
                            </div>
                            <p className="absolute right-0 mr-10 font-semibold text-[28px]">
                                {evaluationReportResponse.evaluation.grade} / {evaluationReportResponse.solution.assignment.maxPoints}
                            </p>
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    )
}