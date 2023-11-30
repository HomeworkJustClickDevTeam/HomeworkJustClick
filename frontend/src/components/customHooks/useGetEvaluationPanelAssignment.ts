import {useEffect, useState} from "react";
import {EvaluationPanelAssignmentInterface} from "../../types/EvaluationPanelAssignmentInterface";
import {useAppDispatch} from "../../types/HooksRedux";
import {setIsLoading} from "../../redux/isLoadingSlice";
import {getEvaluationPanelAssignmentPostgresService} from "../../services/postgresDatabaseServices";

export const useGetEvaluationPanelAssignment = (assignmentId: number | undefined | null, userId: number | undefined | null) =>{
  const [evaluationPanelAssignment, setEvaluationPanelAssignment] = useState<EvaluationPanelAssignmentInterface|undefined>(undefined)
  const dispatch = useAppDispatch()


  useEffect(() => {
    let mounted = true
    if (assignmentId !== undefined && assignmentId !== null && userId !== undefined && userId !== null) {
      dispatch(setIsLoading(true))
      getEvaluationPanelAssignmentPostgresService(assignmentId.toString(), userId.toString())
        .then((response)=> {
          if(response?.status === 200)
            if(mounted){
              setEvaluationPanelAssignment({
                id: response.data.id,
                assignmentId: response.data.assignment.id,
                evaluationPanelId: response.data.evaluationPanel.id
              })
            }
        })
        .catch(error => {
          if(error?.response.status === 403)
            setEvaluationPanelAssignment(undefined)
          else
            console.log(error)
        })
      dispatch(setIsLoading(false))
    }

    return () => {
      mounted = false
    }
  }, [assignmentId, userId]);

  return {evaluationPanelAssignment, setEvaluationPanelAssignment}
}