import { UserInterface } from "../types/UserInterface"
import { LoginUserInterface } from "../types/LoginUserInterface"
import { Dispatch } from "react"
import { Action, AnyAction } from "redux"
import { logOut, setUser } from "../redux/userStateSlice"
import { AppDispatch } from "../redux/store"
import { createAsyncThunk } from "@reduxjs/toolkit"
import { loginPostgresService } from "./postgresDatabaseServices"


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

export const loginUser = createAsyncThunk(
  "userState/login",
  async (userData:LoginUserInterface) => {
    try{
      const response = await loginPostgresService(userData)
      localStorage.setItem("user", JSON.stringify(response.data))
      return response.data
    }catch (error) {return error}
  }
)
