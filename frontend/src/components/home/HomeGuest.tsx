import { Link } from "react-router-dom"

function HomeGuest() {
  return (
    <div className='sm:flex w-screen flex-col items-center text-center font-lato font-normal text-sm'>
        <div className='pt-[20vh] pb-8 md:pb-[8vh] max-w-sm'>
            <h1 className='text-3xl xl:text-[81px] font-medium pb-1 tracking-wide '>HomeworkJustClick</h1>
            <p className='text-s xl:text-[36px] font-medium '>Prosto, szybko, wygodnie!</p>
        </div>
      <Link to="/login">
        <button type="button" className='border-2 rounded-md border-main_blue w-40 xl:w-[288px] xl:h-[56px] py-0.5 text-main_blue xl:text-[28px]'>Zaloguj się</button>
      </Link>
        <p className='text-[8px] xl:text-base text-main_blue'>Nie pamiętasz hasła?</p>
        <p className='text-[8px] xl:text-base mt-4 md:mt-[5vh]'>Nie masz jeszcze konta?</p>
      <Link to="/register">
        <button type="button" className='border-2 border-main_blue rounded-md bg-main_blue w-40 xl:w-[288px] xl:h-[56px] py-0.5 text-white xl:text-[28px]'>Zarejestruj się</button>
      </Link>
    </div>
  )
}
export default HomeGuest
