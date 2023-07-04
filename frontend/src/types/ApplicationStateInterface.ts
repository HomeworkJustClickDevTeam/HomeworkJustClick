import {UserStateInterface} from "./UserStateInterface";

export interface ApplicationStateInterface {
  loggedIn: boolean
  homePageIn: boolean
  userState: UserStateInterface
}