import {useAppDispatch} from "../../types/HooksRedux";
import {useEffect, useState} from "react";
import {EvaluationReportResponse} from "../../types/EvaluationReportResponseModel";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {getEvaluationReportByEvaluationId} from "../../services/postgresDatabaseServices";

export const useGetReportedEvaluation = (evaluationId: number | undefined | null) => {
    const dispatch = useAppDispatch();
    const [evaluationReportResponse, setEvaluationReportResponse] = useState<EvaluationReportResponse>();

    useEffect(() => {
            let mounted = true
            if (evaluationId) {
                dispatch(setIsLoading(true))
                getEvaluationReportByEvaluationId(evaluationId)
                    .then((response) => {
                        if (response !== null && response !== undefined) {
                            if (mounted) {
                                setEvaluationReportResponse(response.data)
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
                                setEvaluationReportResponse(undefined)
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
        [evaluationId]
    )
    return evaluationReportResponse
}
