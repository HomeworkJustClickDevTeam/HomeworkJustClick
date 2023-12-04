import {Table} from "./Table.model";
import {AssignmentInterface} from "./AssignmentInterface";

export class EvaluationPanelAssigment{
    constructor(public id:number,public evaluationPanel: Table, public assignment: AssignmentInterface ) {
    }
}