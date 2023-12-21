import { UserInterface } from "../types/UserInterface"
import { UserLoginInterface } from "../types/UserLoginInterface"
import { createAsyncThunk } from "@reduxjs/toolkit"
import { loginPostgresService } from "./postgresDatabaseServices"


export const parseJwt = (token: string): any => {
  try {
    return JSON.parse((window.atob(token.split('.')[1])))
  } catch (error) {
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
  async (userData: UserLoginInterface) => {
    try {
      const response = await loginPostgresService(userData)
      localStorage.setItem("user", JSON.stringify(response.data))
      return response.data
    } catch (error) { throw error }
  }
)
