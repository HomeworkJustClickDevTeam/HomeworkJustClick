import {EvaluationPanelAssigment} from "../../types/EvaluationPanelAssigment.model";
import {useEffect, useState} from "react";
import {useAppDispatch} from "../../types/HooksRedux";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {getEvaluationPanelAssignmentPostgresService} from "../../services/postgresDatabaseServices";

export const useGetEvaluationPanelByUserIdAndAssigmentId = (
    userId: number | undefined | null,
    assigmentId: number | undefined | null) => {

    const [evaluationPanelAssigment, setEvaluationPanelAssigment] = useState<EvaluationPanelAssigment | undefined>(
        undefined
    )

    const dispatch = useAppDispatch()

    useEffect(() => {
        let mounted = true
        if ((userId !== undefined && userId !== null) &&
            (assigmentId !== undefined && assigmentId !== null)
        ) {
            dispatch(setIsLoading(true))
            getEvaluationPanelAssignmentPostgresService(assigmentId, userId).then(
                (response) => {
                    if (response !== null && response !== undefined) {
                        if (mounted) {
                            setEvaluationPanelAssigment(response.data)
                        }
                    }
                }
            ).catch((error) => {
                if (
                    error !== null &&
                    error !== undefined &&
                    error.response.status === 404
                ) {
                    if (mounted) {
                        setEvaluationPanelAssigment(undefined)
                    }
                } else {
                    console.log("Error fetching evaluation panel assigment:", error)
                }
            })
            dispatch(setIsLoading(false))
        }
        return () => {
            mounted = false
        }
    }, [userId, assigmentId])

    return evaluationPanelAssigment
}