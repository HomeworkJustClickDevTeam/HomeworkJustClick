import {SolutionExtendedInterface} from "./SolutionExtendedInterface";
import {EvaluationModel} from "./EvaluationExtendedModel";

export class EvaluationReportResponse {

    constructor(public id:number,public comment:String,public evaluation:EvaluationModel,public solution:SolutionExtendedInterface) {
    }
}