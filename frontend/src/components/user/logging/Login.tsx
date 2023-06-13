import React, { ChangeEvent, useContext, useState } from "react"
import postgresqlDatabase from "../../../services/postgresDatabase"
import {Link, useNavigate} from "react-router-dom"
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
    <div className='flex w-screen flex-col items-center text-center font-lato font-normal text-sm'>
      <h1 className='pt-24 pb-6 max-w-sm text-2xl font-medium pb-1'>Zaloguj się</h1>
      <form onSubmit={handleSubmit} className='flex flex-col items-center'>
        <input
          name="email"
          type="email"
          placeholder="Login użytkownika (email)"
          onChange={handleChange}
          className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
        />
        <input
          name="password"
          type="password"
          placeholder="Hasło"
          onChange={handleChange}
          className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
        />
        <button type="submit" className='border-2 rounded-md border-main_blue w-40 py-0.5 text-main_blue'>Zaloguj się</button>
        <p className='text-[8px] text-main_blue'>Nie pamiętasz hasła?</p>
        <p className='text-[8px] mt-4'>Nie masz jeszcze konta?</p>
        <Link to="/register">
          <button type="button" className='border-2 border-main_blue rounded-md bg-main_blue w-40 py-0.5 text-white'>Zarejestruj się</button>
        </Link>
      </form>
    </div>
  )
}
export default Login
