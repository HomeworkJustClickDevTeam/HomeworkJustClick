import {UserInterface} from "../types/UserInterface";
import {LoginUserInterface} from "../types/LoginUserInterface";
import {getUserPostgresService, loginPostgresService} from "./postgresDatabaseServices";
import {Dispatch} from "react";
import {ActionTypes} from "../types/ActionTypes";

export const getUser = (): (UserInterface | undefined) => {
  const userString = localStorage.getItem("user")
  if (userString !== null) return JSON.parse(userString)
  else return undefined
}

export const login = async (user: LoginUserInterface, setApplicationState: Dispatch<ActionTypes>) => {
  await loginPostgresService(user)
    .then((response) => {
      if (response?.data.token) {
        setApplicationState({type:"logIn", userState:response.data})
        localStorage.setItem("user", JSON.stringify(response.data))
      } else {
        console.log("Zle haslo / uzytkownik")
      }
    })
    .catch((error) => console.log(error))
}

export const logout = (setApplicationState: Dispatch<ActionTypes>) => {
  setApplicationState({type:"logOut"})
  localStorage.removeItem("user")
}

export const checkToken = () => {
  let userState = getUser()
  if(userState !== undefined){
    getUserPostgresService(userState.id as unknown as string)
      .catch((error)=>{
        if(error){
          localStorage.removeItem("user")
          userState = undefined
        }
      })
    return userState

  }
  else return undefined
}