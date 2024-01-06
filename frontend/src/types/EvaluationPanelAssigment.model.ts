import {Table} from "./Table.model";
import {AssignmentModel} from "./Assignment.model";

export class EvaluationPanelAssigment{
    constructor(public id:number,public evaluationPanel: Table, public assignment: AssignmentModel ) {
    }
}