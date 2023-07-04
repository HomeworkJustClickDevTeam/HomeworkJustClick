
import {UserStateInterface} from "./UserStateInterface";

export type ActionType =
  | { type: "login"; data: UserStateInterface }
  | { type: "logout" }
  | { type: "homePageOut" }
  | { type: "homePageIn" }