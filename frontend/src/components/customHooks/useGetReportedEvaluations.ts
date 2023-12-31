import {useAppDispatch} from "../../types/HooksRedux";
import {useEffect, useState} from "react";
import {EvaluationReportResponse} from "../../types/EvaluationReportResponseModel";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {getEvaluationReportByGroup} from "../../services/postgresDatabaseServices";

export const useGetReportedEvaluations = (groupId: number | undefined | null) => {
    const dispatch = useAppDispatch();
    const [evaluationReportsResponse, setEvaluationReportsResponse] = useState<EvaluationReportResponse[]>();

    useEffect(() => {
            let mounted = true
            if (groupId !== undefined && groupId !== null) {
                dispatch(setIsLoading(true))
                getEvaluationReportByGroup(groupId)
                    .then((response) => {
                        if (response !== null && response !== undefined) {
                            if (mounted) {
                                setEvaluationReportsResponse(response.data)
                            }
                        }
                    })
                    .catch((error) => {
                        if (
                            error !== null &&
                            error !== undefined &&
                            error.response.status === 404
                        ) {
                            if (mounted) {
                                setEvaluationReportsResponse([])
                            }
                        } else {
                            console.log("Error fetching user:", error)
                        }
                    })
                dispatch(setIsLoading(false))
            }

            return () => {
                mounted = false
            }
        },
        [groupId]
    )
    return evaluationReportsResponse
}