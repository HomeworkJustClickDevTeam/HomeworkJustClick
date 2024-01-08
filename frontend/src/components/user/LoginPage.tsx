import React from "react"
import { Link, useNavigate } from "react-router-dom"
import login_left_circle from "./login_left_circle.svg"
import login_right_circle from "./login_right_circle.svg"
import { FaEye, FaEyeSlash } from "react-icons/fa"
import { UserLoginInterface } from "../../types/UserLoginInterface"
import { AppDispatch } from "../../redux/store"
import { loginUser } from "../../services/otherServices"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { FieldValues, useForm } from "react-hook-form"
import { unwrapResult } from "@reduxjs/toolkit"
import { useState } from "react"
import { selectGroupId, setGroupId } from "redux/groupIdSlice"

const LoginPage = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    setError,
  } = useForm({ shouldFocusError: false });
  const [showPassword, setShowPassword] = useState(false);

  const dispatch: AppDispatch = useAppDispatch()
  const groupId = useAppSelector(selectGroupId)
  const navigate = useNavigate()

  const onSubmit = async (data: FieldValues) => {
    const userData: UserLoginInterface = {
      email: data.email,
      password: data.password
    };
    try {
      unwrapResult(await dispatch(loginUser(userData)));
      if (groupId) {
        const groupIdToGo = groupId;
        dispatch(setGroupId(null))
        navigate(`/group/${groupIdToGo}`)
      }
    } catch (error) {
      console.log(error);
      setError('email', { type: 'manual', message: '' });
      setError('password', { type: 'manual', message: 'E-mail lub hasło są nieprawidłowe' });
    }
  }


  return (
    <div data-testid="login-page"
      className='flex w-screen flex-col items-center text-center font-lato select-none'>
      <img className="fixed left-[7%]  bottom-[3%] scale-50 xl:scale-100 -z-50" src={login_left_circle}
        alt="Kółko po lewej stronie"></img>
      <img className="fixed right-[9%] top-[18%] scale-50 translate-x-[25%] xl:transform-none -z-50"
        src={login_right_circle} alt="Kółko po prawej stronie"></img>
      <h1 className='mt-16 mb-8 xl:mt-32 xl:mb-16 text-4xl xl:text-6xl'>Zaloguj się</h1>
      <form onSubmit={handleSubmit(onSubmit)} className='flex flex-col items-center'>
        <div>
          <input
            {...register("email", {
              required: "Podaj adres e-mail",
              pattern: {
                value: /\S+@\S+\.\S+/,
                message: "Nieprawidłowy adres e-mail"
              },
              maxLength: {
                value: 255,
                message: "E-mail może zawierać maksymalnie 255 znaków"
              }
            })}
            name="email"
            placeholder="E-mail"
            className={`border-b-2 ${errors.email ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center w-[180px] xl:w-[320px] text-[12px] xl:text-[20px]`}
          />
          <div className="h-12">
            {errors.email && (
              <p className=" text-berry_red text-[9px] xl:text-[14px]">{`${errors.email.message}`}</p>
            )}
          </div>
        </div>
        <div className="relative">
          <input
            {...register("password", {
              required: "Podaj hasło",
              minLength: {
                value: 8,
                message: "Hasło musi mieć conajmnej 8 znaków"
              },
              maxLength: {
                value: 255,
                message: "Hasło może zawierać maksymalnie 255 znaków"
              }
            })}
            type={showPassword ? "text" : "password"}
            placeholder="Hasło"
            className={`border-b-2 ${errors.password ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center w-[180px] xl:w-[320px] text-[12px] xl:text-[20px]`}
          />
          <div
            className="absolute top-[4px] xl:top-[2px] right-0 p-1 cursor-pointer"
            onClick={() => setShowPassword(!showPassword)}>
            {showPassword ? (<FaEyeSlash className="fill-light_gray text-[12px] xl:text-[20px]" />) : (<FaEye className="fill-light_gray text-[12px] xl:text-[20px]" />)}
          </div>
          <div className="h-12">
            {errors.password && (
              <p className="w-[180px] xl:w-[320px] text-berry_red text-[9px] xl:text-[14px]">{`${errors.password.message}`}</p>
            )}
          </div>
        </div>
        <button type="submit"
          className=' border-main_blue w-40 h-8 rounded-[5px] border-2 xl:w-[288px] xl:h-[56px] xl:border-[3px] xl:rounded-[10px] text-main_blue xl:text-[28px] hover:bg-hover_gray hover:shadow-md active:bg-opacity-60'>Zaloguj
          się
        </button>
        {/* <p className='text-[8px] xl:text-base text-main_blue'>Nie pamiętasz hasła?</p> */}
        <p className='text-[8px] xl:text-base mt-8'>Nie masz jeszcze konta?</p>
        <Link to="/register">
          <button type="button"
            className=' bg-main_blue w-40 h-8 rounded-[5px] xl:rounded-[10px] xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none'>Zarejestruj
            się
          </button>
        </Link>
      </form>
    </div>
  )
}
export default LoginPage
