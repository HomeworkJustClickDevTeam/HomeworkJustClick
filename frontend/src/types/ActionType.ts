import {UserStateInterface} from "./UserStateInterface";

export type ActionType =
  | { type: "login"}
  | { type: "logout" }
  | { type: "homePageOut" }
  | { type: "homePageIn" }