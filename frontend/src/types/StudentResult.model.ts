import {StudentModel} from "./Student.model";

export class StudentResultModel {
  constructor(
    public student: StudentModel,
    public result: number,
    public resultPercent: number,
    public late: boolean,
    public description: string
  ) {
  }
}