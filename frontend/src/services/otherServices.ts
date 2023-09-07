import { UserInterface } from "../types/UserInterface"
import { LoginUserInterface } from "../types/LoginUserInterface"
import { loginPostgresService } from "./postgresDatabaseServices"
import { Dispatch } from "react"
import { Action, AnyAction } from "redux"
import { logOut, setUser } from "../redux/userStateSlice"
import { AppDispatch } from "../redux/store"


export const parseJwt = (token:string):any => {
  try{
    return JSON.parse((window.atob(token.split('.')[1])))
  }catch (error){
    return null
  }
}
export const getUser = (): (UserInterface | null) => {
  const userString = localStorage.getItem("user")
  if (userString !== null) return JSON.parse(userString)
  else return null
}

export const login = async (user: LoginUserInterface, dispatch:AppDispatch) => {
  await loginPostgresService(user)
    .then((response) => {
      if (response?.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data))
        dispatch(setUser(response.data))
      } else {
        console.log("Zle haslo / uzytkownik")
      }
    })
    .catch((error) => console.log(error))
}

export const logout = async (dispatch:AppDispatch) => {
  localStorage.removeItem("user")
  dispatch(logOut())
}