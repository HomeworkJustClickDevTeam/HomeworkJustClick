import React, {ChangeEvent, useContext, useState} from "react"
import {loginPostgresService} from "../../services/postgresDatabaseServices"
import {Link, useNavigate} from "react-router-dom"
import HomePageContext from "../../contexts/HomePageContext"
import login_left_circle from './login_left_circle.svg';
import login_right_circle from './login_right_circle.svg';
import {LoginUserInterface} from "../../types/LoginUserInterface";
import HomePage from "../home/HomePage";


const LoginPage = () => {
  const [user, setUser] = useState<LoginUserInterface>({
    email: "",
    password: "",
  })
  const {setHomePageIn} = useContext(HomePageContext)
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()

    try {
      const response = await loginPostgresService(user)
      if (response?.data.token) {
        localStorage.setItem("user", JSON.stringify(response.data))
        setHomePageIn(true)
        return <HomePage/>
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
      <div className='flex w-screen flex-col items-center text-center font-lato text-sm select-none'>
        <img className="fixed left-[7%]  bottom-[3%] scale-50 xl:scale-100 -z-50" src = {login_left_circle} alt="Kółko po lewej stronie"></img>
        <img className="fixed right-[9%] top-[18%] scale-50 translate-x-[25%] xl:transform-none -z-50" src= {login_right_circle} alt="Kółko po prawej stronie"></img>
        <h1 className='mt-32 mb-16 text-6xl'>Zaloguj się</h1>
        <form onSubmit={handleSubmit} className='flex flex-col items-center'>
          <input
              name="email"
              type="email"
              placeholder="Email"
              onChange={handleChange}
              className='border-b-2 border-b-light_gray mb-8 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <input
              name="password"
              type="password"
              placeholder="Hasło"
              onChange={handleChange}
              className='border-b-2 border-b-light_gray mb-12 text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]'
          />
          <button type="submit" className=' border-main_blue w-40 h-8 rounded-[5px] border-2 xl:w-[288px] xl:h-[56px] xl:border-[3px] xl:rounded-[10px] text-main_blue xl:text-[28px] hover:bg-hover_gray hover:shadow-md active:bg-opacity-60'>Zaloguj się</button>
          <p className='text-[8px] xl:text-base text-main_blue'>Nie pamiętasz hasła?</p>
          <p className='text-[8px] xl:text-base mt-4'>Nie masz jeszcze konta?</p>
          <Link to="/register">
            <button type="button" className=' bg-main_blue w-40 h-8 rounded-[5px] xl:rounded-[10px] xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none'>Zarejestruj się</button>
          </Link>
        </form>
      </div>
  )
}
export default LoginPage
