import { Link } from "react-router-dom"

function HomeGuest() {
  return (
    <div className='flex w-screen flex-col items-center text-center font-lato font-normal'>
        <div className='pt-[20vh] pb-8 xl:pb-[8vh]'>
            <h1 className='text-3xl xl:text-[81px] font-medium pb-[calc(4px+1vh)] tracking-wide '>HomeworkJustClick</h1>
            <p className='text-s xl:text-[36px] font-medium '>Prosto, szybko, wygodnie!</p>
        </div>
      <Link to="/login">
        <button type="button" className='border-2 rounded-md border-main_blue w-40 xl:w-[288px] xl:h-[56px] py-0.5 text-main_blue xl:text-[28px]'>Zaloguj się</button>
      </Link>
        <p className='text-[8px] xl:text-base text-main_blue'>Nie pamiętasz hasła?</p>
        <p className='text-[8px] xl:text-base mt-4 xl:mt-[5vh]'>Nie masz jeszcze konta?</p>
      <Link to="/register">
        <button type="button" className='border-2 border-main_blue rounded-md bg-main_blue w-40 xl:w-[288px] xl:h-[56px] py-0.5 text-white xl:text-[28px]'>Zarejestruj się</button>
      </Link>
    </div>
  )
}
export default HomeGuest
