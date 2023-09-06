import {ApplicationStateInterface} from "../types/ApplicationStateInterface";
import {getUser, logout} from "../services/otherServices";
import {ActionTypes} from "../types/ActionTypes";
import {PropsWithChildren, ReactElement, useEffect, useReducer} from "react";
import ApplicationStateContext from "../contexts/ApplicationStateContext";
import {useNavigate} from "react-router-dom";
import {useUpdateUserState} from "./customHooks/useUpdateUserState";

export const ApplicationProvider = ({children}:PropsWithChildren):ReactElement =>{
  const navigate = useNavigate()
  useUpdateUserState()

  const initialApplicationState:ApplicationStateInterface = {
    userState: getUser(),
    role: undefined,
    group: undefined,
    homePageIn: true
  }
  function reducer(state: ApplicationStateInterface, action: ActionTypes): ApplicationStateInterface{
    switch (action.type){
      case "logOut":
        navigate("/home")
        return {userState: undefined, role: undefined, group:undefined, homePageIn:true}
      case "setUser":
        return {userState: action.userState, role: state.role, group: state.group, homePageIn:state.homePageIn}
      case "setGroupView":
        return {group: action.group, role: state.role, userState: state.userState, homePageIn:state.homePageIn}
      case "setGroupViewRole":
        return {role: action.role, userState: state.userState, group: state.group, homePageIn:state.homePageIn}
      case "setHomePageIn":
        return {role: state.role, userState: state.userState, group: state.group, homePageIn: action.homePageIn}
    }
  }

  const [applicationState, setApplicationState] = useReducer(reducer, initialApplicationState)
  useEffect(() => {
    console.log("APP STATE:", applicationState)
  }, [applicationState]);

  return (
    <ApplicationStateContext.Provider value={{applicationState, setApplicationState}}>
      {children}
    </ApplicationStateContext.Provider>
  )
}