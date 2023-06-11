import { Link } from "react-router-dom"

function HomeGuest() {
  return (
    <div className='flex w-screen flex-col items-center text-center font-lato font-normal text-sm'>
        <div className='pt-28 pb-8 max-w-sm'>
            <h1 className='text-3xl font-medium pb-1 tracking-wide'>Homework JustClick</h1>
            <p className='text-s font-medium'>Prosto, szybko, wygodnie!</p>
        </div>
      <Link to="/login">
        <button type="button" className='border-2 rounded-md border-main_blue w-40 py-0.5 text-main_blue'>Zaloguj się</button>
      </Link>
        <p className='text-[8px] text-main_blue'>Nie pamiętasz hasła?</p>
        <p className='text-[8px] mt-4'>Nie masz jeszcze konta?</p>
      <Link to="/register">
        <button type="button" className='border-2 border-main_blue rounded-md bg-main_blue w-40 py-0.5 text-white'>Zarejestruj się</button>
      </Link>
    </div>
  )
}
export default HomeGuest
