import {AssignmentWithEvaluationModel} from "../types/AssignmentWithEvaluation.model";

export const GetResultFromAssignmentsWithEvaluation = (assignmentsWithEvaluation: AssignmentWithEvaluationModel[], filterId: number): number|undefined =>{
  const output = assignmentsWithEvaluation.find((assignmentsWithEvaluation)=>assignmentsWithEvaluation.id === filterId)
  if(output !== undefined && output?.evaluation !== null)
    return output.evaluation.result
  else
    return undefined
}