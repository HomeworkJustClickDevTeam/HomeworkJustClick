import {UserInterface} from "../types/UserInterface";
import {LoginUserInterface} from "../types/LoginUserInterface";
import {getUserPostgresService, loginPostgresService} from "./postgresDatabaseServices";
import {Dispatch} from "react";
import {ActionTypes} from "../types/ActionTypes";


export const parseJwt = (token:string):any => {
  try{
    return JSON.parse((window.atob(token.split('.')[1])))
  }catch (error){
    return null
  }
}
export const getUser = (): (UserInterface | undefined) => {
  const userString = localStorage.getItem("user")
  if (userString !== null) return JSON.parse(userString)
  else return undefined
}

export const login = async (user: LoginUserInterface, setApplicationState: Dispatch<ActionTypes>) => {
  await loginPostgresService(user)
    .then((response) => {
      if (response?.data.token) {
        setApplicationState({type:"setUser", userState:response.data})
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