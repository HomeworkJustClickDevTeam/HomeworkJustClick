import React, { ChangeEvent, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import { useNavigate } from "react-router-dom"
import { RegisterUser } from "../../types/types"
import KolkoLewe from './kolko_lewe.svg';
import Smile from './Smile.svg';

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
        <img className="fixed left-[4%]  bottom-[6%] scale-50 xl:scale-100 -z-50" src = {KolkoLewe} alt="Kółko po lewej stronie"></img>
        {/* <img className="relative right-0  scale-50 translate-x-[25%] xl:transform-none -z-50" src= {Smile} alt="Kółko smile"></img> */}
        <h1 className='mt-[10vh] mb-16 text-4xl xl:text-[64px] inline-block'>Dołącz do nas!</h1>
        <form onSubmit={handleSubmit} className='flex flex-col text-center items-center'>
          <input
              type="text"
              name="firstname"
              value={user.firstname}
              placeholder="Imię"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <input
              type="text"
              name="lastname"
              value={user.lastname}
              placeholder="Nazwisko"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <input
              type="email"
              name="email"
              value={user.email}
              placeholder="Adres e-mail"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <input
              type="password"
              name="password"
              value={user.password}
              placeholder="Hasło"
              onChange={handleChangeUser}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <input
              type="password"
              name="secondPassword"
              value={secondPassword}
              placeholder="Powtórz hasło jeszcze raz"
              onChange={handleChangeSecondPassword}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <button type="submit" className=' bg-main_blue w-40 h-8 rounded-[5px] xl:rounded-[10px] xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none'>Zajerestruj się</button>
        </form>
      </div>
  )
}
export default Register
