import {UserInterface} from "../types/UserInterface";

export const getUser = ():(UserInterface | undefined) =>{
  const userString = localStorage.getItem("user")
  if(userString !== null) return JSON.parse(userString)
  else return undefined
}

export const logout = () =>{
  localStorage.removeItem("user")
}