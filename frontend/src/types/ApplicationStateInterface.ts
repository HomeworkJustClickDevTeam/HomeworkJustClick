import {UserInterface} from "./UserInterface";
import {GroupInterface} from "./GroupInterface";

export interface ApplicationStateInterface {
  userState?: UserInterface
  role?: string
  group?: GroupInterface
  homePageIn: boolean
}
