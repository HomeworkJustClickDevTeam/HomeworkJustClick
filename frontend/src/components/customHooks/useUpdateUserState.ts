import {useContext, useEffect, useState} from "react";
import {getUser, logout} from "../../services/otherServices";
import applicationStateContext from "../../contexts/ApplicationStateContext";

export const useUpdateUserState = () => {
  const {applicationState, setApplicationState} = useContext(applicationStateContext)

  useEffect(()=>{
    const user = getUser()
    if(user !== applicationState?.userState){
      if(user === undefined){
        logout(setApplicationState)
      }
      else{
        setApplicationState({type:"setUser", userState:user})
      }
    }

    window.addEventListener("storage", getUser, false)

    return () => {window.removeEventListener("storage", getUser)}
  }, [applicationState])

}