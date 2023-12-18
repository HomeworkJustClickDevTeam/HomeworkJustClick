import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {selectRole} from "../../redux/roleSlice";
import NotFoundPage from "../errors/NotFoundPage";
import {useState} from "react";

export const EvaluationReportedPage = () => {
    const group = useAppSelector(selectGroup)
    const role = useAppSelector(selectRole)
    const [page,SetPage] = useState<number>(1)
    const reportedEvaluations = useGetReportedEvaluations(group.id,page)

    if (role !== "Teacher") {
        return <NotFoundPage/>
    }

    return (
        <>

        </>
    )
}