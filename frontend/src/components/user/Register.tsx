import React, { ChangeEvent, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import { useNavigate } from "react-router-dom"
import { RegisterUser } from "../../types/types"

const Register = () => {
  const [secondPassword, setSecondPassword] = useState<string>("")
  const [user, setUser] = useState<RegisterUser>({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  })
  const navigate = useNavigate()

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!!user.password && user.password === secondPassword) {
      try {
        await postgresqlDatabase.post("/auth/register", user)
        navigate("/")
      } catch (e) {
        console.log("Error not send")
      }
    }
  }
  const handleChangeUser = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target
    setUser((prevState) => ({
      ...prevState,
      [name]: value,
    }))
  }

  const handleChangeSecondPassword = (event: ChangeEvent<HTMLInputElement>) => {
    setSecondPassword(event.target.value)
  }

  return (
      <div className='flex w-screen flex-col items-center text-center font-lato font-normal text-sm'>
        <h1 className='mt-[10vh] mb-10 max-w-sm text-3xl font-medium'>Dołącz do nas !</h1>
        <form onSubmit={handleSubmit} className='flex flex-col text-center items-center'>
          <input
              type="text"
              name="firstname"
              value={user.firstname}
              placeholder="Imię"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
          />
          <input
              type="text"
              name="lastname"
              value={user.lastname}
              placeholder="Nazwisko"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
          />
          <input
              type="email"
              name="email"
              value={user.email}
              placeholder="Adres e-mail"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
          />
          <input
              type="password"
              name="password"
              value={user.password}
              placeholder="Hasło"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
          />
          <input
              type="password"
              name="secondPassword"
              value={secondPassword}
              placeholder="Powtórz hasło jeszcze raz"
              onChange={handleChangeSecondPassword}
              className='border-b-2 border-b-light_gray mb-6 text-center placeholder:text-light_gray placeholder:text-[12px] w-60'
          />
          <button type="submit" className='border-2 border-main_blue rounded-md bg-main_blue w-40 py-0.5 text-white'>Zajerestruj się</button>
        </form>
      </div>
  )
}
export default Register
