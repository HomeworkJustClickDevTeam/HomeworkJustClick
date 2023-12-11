import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {selectRole} from "../../redux/roleSlice";
import NotFoundPage from "../errors/NotFoundPage";

export const EvaluationReportedPage = () => {
    const group = useAppSelector(selectGroup)
    const role = useAppSelector(selectRole)

    if (role !== "Teacher") {
        return <NotFoundPage/>
    }

    return (
        <>

        </>
    )
}