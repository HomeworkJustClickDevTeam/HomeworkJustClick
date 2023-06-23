import { Link } from "react-router-dom"
import KolkoPoLewejStronie from './kolko_po_lewej_stronie.svg';
import KolkoPoPrawejStronie from './kolko_po_prawej_stronie.svg';

function HomeGuest() {
  return (
    <div className='flex w-screen flex-col items-center text-center font-lato font-normal select-none'>
       <img className="fixed left-[12%]  bottom-[15%] scale-50 xl:scale-100 -z-50 " src = {KolkoPoLewejStronie} alt="Kółko po lewej stronie"></img>
       <img className="fixed right-0 top-[12%] scale-50 translate-x-[25%] xl:transform-none -z-50 " src= {KolkoPoPrawejStronie} alt="Kółko po prawej stronie"></img>
        <div className='pt-[20vh] pb-8 xl:pb-[8vh]'>
            <h1 className='text-3xl xl:text-[81px] font-medium pb-[calc(4px+1vh)] tracking-wide '>HomeworkJustClick</h1>
            <p className='text-s xl:text-[36px] font-medium '>Prosto, szybko, wygodnie!</p>
        </div>
      <Link to="/login">
      <button type="button" className=' border-main_blue w-40 h-8 rounded-[5px] border-2 xl:w-[288px] xl:h-[56px] xl:border-[3px] xl:rounded-[10px] text-main_blue xl:text-[28px] hover:bg-hover_gray hover:shadow-md active:bg-opacity-60'>Zaloguj się</button>
      </Link>
        <p className='text-[8px] xl:text-base text-main_blue'>Nie pamiętasz hasła?</p>
        <p className='text-[8px] xl:text-base mt-4 md:mt-[5vh]'>Nie masz jeszcze konta?</p>
      <Link to="/register">
        <button type="button" className=" bg-main_blue w-40 h-8 rounded-[5px]  xl:w-[288px] xl:h-[56px] text-white xl:text-[28px] hover:bg-hover_blue hover:shadow-md active:shadow-none">Zarejestruj się</button>
      </Link>
    </div>
  )
}
export default HomeGuest
