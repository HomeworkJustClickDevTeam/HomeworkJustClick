import {UserInterface} from "../types/UserInterface";
import {LoginUserInterface} from "../types/LoginUserInterface";
import {loginPostgresService} from "./postgresDatabaseServices";

export const getUser = (): (UserInterface | undefined) => {
  const userString = localStorage.getItem("user")
  console.log("USER:", userString)
  if (userString !== null) return JSON.parse(userString)
  else return undefined
}

export const login = async (user: LoginUserInterface) => {
  await loginPostgresService(user)
    .then((response) => {
      if (response?.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data))
      } else {
        console.log("Zle haslo / uzytkownik")
      }
    })
    .catch((error) => console.log(error))
}

export const logout = () => {
  localStorage.removeItem("user")
}