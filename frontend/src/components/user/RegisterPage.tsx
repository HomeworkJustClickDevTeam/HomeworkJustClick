import React, {ChangeEvent, useState} from "react"
import {postAuthRegisterPostgresService} from "../../services/postgresDatabase"
import {useNavigate} from "react-router-dom"
import KolkoLewe from './left_circle.svg';
import Smile from './Smile.svg';
import {UserRegisterInterface} from "../../types/UserRegisterInterface";


const RegisterPage = () => {
  const [user, setUser] = useState<UserRegisterInterface>({
    firstname: "",
    lastname: "",
    password: "",
    secondPassword: "",
    email: ""
  })
  const navigate = useNavigate()

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!!user.password && user.password === user.secondPassword) {
      try {
        await postAuthRegisterPostgresService(user)
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

  return (
      <div className='flex w-screen flex-col font-lato font-normal text-sm select-none'>
        <img className="fixed left-[4%]  bottom-[6%] scale-50 xl:scale-100 -z-50" src = {KolkoLewe} alt="Kółko po lewej stronie"></img>
        <div className='flex justify-center items-center text-center flex-col'>
          <h1 className='mt-16 mb-16 text-4xl xl:text-[64px] ml-[7%]'>Dołącz do nas!
            <img className="relative right-0  scale-50 translate-x-[25%] xl:transform-none -z-50 inline-block pl-10" src= {Smile} alt="Kółko smile"></img>
          </h1>
          {/*<img className="absolute right-0 top-0 mr-[24%] mt-[8%]" src= {Smile} alt="Kółko smile"></img>*/}
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
                value={user.secondPassword}
                placeholder="Powtórz hasło jeszcze raz"
                onChange={handleChangeUser}
                className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
            />
            <button type="submit" className=' bg-main_blue w-40 h-8 rounded-[5px] xl:rounded-[10px] xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none'>Zajerestruj się</button>
          </form>
        </div>
      </div>
  )
}
export default RegisterPage
