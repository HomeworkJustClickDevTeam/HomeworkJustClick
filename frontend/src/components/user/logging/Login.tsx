import React, { ChangeEvent, useContext, useState } from "react"
import common_request from "../../../services/default-request-database"
import { useNavigate } from "react-router-dom"
import { Action, LoginUser, userState } from "../../../types/types"
import DispatchContext from "../../../DispatchContext"

const Login = () => {
  const [user, setUser] = useState<LoginUser>({
    email: "",
    password: "",
  })
  const globalDispatch = useContext(DispatchContext)
  const navigate = useNavigate()
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      const response = await common_request.post("/auth/authenticate", user)
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
