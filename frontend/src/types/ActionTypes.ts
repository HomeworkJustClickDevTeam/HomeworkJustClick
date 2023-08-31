import {UserInterface} from "./UserInterface";
import {GroupInterface} from "./GroupInterface";

export type ActionTypes =
  | {type: "logIn", userState: UserInterface}
  | {type: "logOut"}
  | {type: "setGroupView", group?: GroupInterface}
  | {type: "setGroupViewRole", role: string}
  | {type: "setHomePageIn", homePageIn: boolean}