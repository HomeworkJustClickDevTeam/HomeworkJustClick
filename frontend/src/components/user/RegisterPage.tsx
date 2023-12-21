import { registerPostgresService } from "../../services/postgresDatabaseServices"
import { useNavigate } from "react-router-dom"
import left_circle from "./left_circle.svg"
import Smile from "./Smile.svg"
import { UserRegisterInterface } from "../../types/UserRegisterInterface"
import { FieldValues, useForm } from "react-hook-form"
import { error } from "console"

const RegisterPage = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    getValues,
    setError
  } = useForm({ shouldFocusError: false });

  const navigate = useNavigate()

  const onSubmit = async (data: FieldValues) => {
    const userFormData: UserRegisterInterface = {
      firstname: data.firstname,
      lastname: data.lastname,
      email: data.email,
      password: data.password
    };
    try {
      await registerPostgresService(userFormData)
      navigate("/home")
    } catch (error) {
      setError('firstname', { type: 'manual', message: '' });
      setError('lastname', { type: 'manual', message: '' });
      setError('email', { type: 'manual', message: '' });
      setError('password', { type: 'manual', message: '' });
      setError('confirmPassword', { type: 'manual', message: 'Błąd podczas rejestracji' });
    }
  }


  return (
    <div className='flex w-screen flex-col font-lato font-normal text-sm select-none'>
      <img className="fixed left-[4%]  bottom-[6%] scale-50 xl:scale-100 -z-50" src={left_circle}
        alt="Kółko po lewej stronie"></img>
      <div className='flex justify-center items-center text-center flex-col'>
        <h1 className='mt-16 mb-16 text-4xl xl:text-[64px] ml-[5%]'>Dołącz do nas!
          <img className="relative right-0  scale-50 translate-x-[25%] xl:transform-none -z-50 inline-block pl-10"
            src={Smile} alt="Kółko smile"></img>
        </h1>
        {/*<img className="absolute right-0 top-0 mr-[24%] mt-[8%]" src= {Smile} alt="Kółko smile"></img>*/}
        <form onSubmit={handleSubmit(onSubmit)} className='flex flex-col text-center items-center gap-y-5 xl:gap-y-9'>
          <div>
            <input
              {...register("firstname", {
                required: "Podaj imię",
                pattern: {
                  value: /^[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż]+$/i,
                  message: "Imię może zawierać tylko litery"
                }
              })}
              type="text"
              placeholder="Imię"
              className={`border-b-2 ${errors.firstname ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]`}
            />
            <div className="h-3">
              {errors.firstname && (
                <p className=" text-berry_red text-[9px] xl:text-[14px]">{`${errors.firstname.message}`}</p>
              )}
            </div>
          </div>
          <div>
            <input
              {...register("lastname", {
                required: "Podaj nazwisko",
                pattern: {
                  value: /^[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż]+-?[A-Za-zĄąĆćĘęŁłŃńÓóŚśŹźŻż]+$/i,
                  message: "Nazwisko może zawierać tylko litery i \"-\" "
                }
              })}
              type="text"
              placeholder="Nazwisko"
              className={`border-b-2 ${errors.lastname ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]`}
            />
            <div className="h-3">
              {errors.lastname && (
                <p className=" text-berry_red text-[9px] xl:text-[14px]">{`${errors.lastname.message}`}</p>
              )}
            </div>
          </div>
          <div>
            <input
              {...register("email", {
                required: "Podaj adres e-mail",
                pattern: {
                  value: /\S+@\S+\.\S+/,
                  message: "Nieprawidłowy adres e-mail"
                }
              })}
              placeholder="Adres e-mail"
              className={`border-b-2 ${errors.email ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]`}
            />
            <div className="h-3">
              {errors.email && (
                <p className=" text-berry_red text-[9px] xl:text-[14px]">{`${errors.email.message}`}</p>
              )}
            </div>
          </div>
          <div>
            <input
              {...register("password", {
                required: "Podaj hasło",
                minLength: {
                  value: 8,
                  message: "Hasło musi mieć conajmnej 8 znaków"
                },
                pattern: {
                  value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/,
                  message: 'Hasło musi zawierać conajmniej jedną małą i wielką literę oraz cyfrę'
                }
              })}
              type="password"
              name="password"
              placeholder="Hasło"
              className={`border-b-2 ${errors.password ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]`}
            />
            <div className="h-3">
              {errors.password && (
                <p className="w-60 xl:w-[320px] text-berry_red text-[9px] xl:text-[14px]">{`${errors.password.message}`}</p>
              )}
            </div>
          </div>
          <div>
            <input
              {...register("confirmPassword", {
                required: "Potwierdź hasło",
                validate: (value) =>
                  value === getValues("password") || "Hasła nie zgadzają się"
              })}
              type="password"
              name="confirmPassword"
              placeholder="Powtórz hasło"
              className={`border-b-2 ${errors.confirmPassword ? 'border-berry_red' : 'border-b-light_gray'} focus:outline-none focus:border-b-main_lily text-center placeholder:text-light_gray placeholder:text-[12px] w-60 xl:text-[20px] xl:placeholder:text-[20px] xl:w-[320px]`}
            />
            <div className="h-3">
              {errors.confirmPassword && (
                <p className=" text-berry_red text-[9px] xl:text-[14px]">{`${errors.confirmPassword.message}`}</p>
              )}
            </div>
          </div>
          <button type="submit"
            className=' bg-main_blue w-40 h-8 rounded-[5px] xl:rounded-[10px] xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none'>Zajerestruj się
          </button>
        </form>
      </div>
    </div>
  )
}
export default RegisterPage
