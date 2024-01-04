import {AssignmentInterface} from "./AssignmentInterface";
import {UserLoginInterface} from "./UserLoginInterface";

export class Notification {
    constructor(
        public id: number,
        public description: string,
        public read: boolean,
        public assignment: AssignmentInterface,
        public user: UserLoginInterface) {
    }
}