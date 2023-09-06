import {UserInterface} from "./UserInterface";
import {GroupInterface} from "./GroupInterface";

export type ActionTypes =
  | {type: "setUser", userState: UserInterface | undefined}
  | {type: "logOut"}
  | {type: "setGroupView", group?: GroupInterface}
  | {type: "setGroupViewRole", role: string | undefined}
  | {type: "setHomePageIn", homePageIn: boolean}