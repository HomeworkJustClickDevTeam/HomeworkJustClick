import React, { ChangeEvent, useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../../services/postgresDatabase"
import { useNavigate } from "react-router-dom"
import { Action, LoginUser, userState } from "../../../types/types"
import DispatchContext from "../../../DispatchContext"

const Login = () => {
  const [user, setUser] = useState<LoginUser>({
    email: "",
    password: "",
  })
  const navigate = useNavigate()
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      const response = await postgresqlDatabase.post("/auth/authenticate", user)
      if (response.data) {
        const userState: userState = {
          token: response.data.token,
          userId: response.data.id,
        }
        const action: Action = { type: "login", data: userState }
        globalDispatch?.(action)
        navigate("/")
      } else {
        console.log("Zle haslo / uzytkownik")
      }
    } catch (e) {
      console.log(e)
    }
  }

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setUser((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  return (
    <>
      <h1>Zaloguj</h1>
      <form onSubmit={handleSubmit}>
        <input
          name="email"
          type="email"
          placeholder="Login użytkownika (email)"
          onChange={handleChange}
        />
        <input
          name="password"
          type="password"
          placeholder="hasło"
          onChange={handleChange}
        />
        <button type="submit">Zaloguj się</button>
      </form>
    </>
  )
}
export default Login
