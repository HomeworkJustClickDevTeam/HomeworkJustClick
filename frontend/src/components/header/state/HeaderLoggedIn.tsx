import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link } from "react-router-dom"
import { FaChevronLeft } from 'react-icons/fa';

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  return (
    <div className='relative flex h-[5vh] text-white font-lato font-normal text-[11px] xl:text-[24px] bg-main_blue items-center leading-[5vh] '>
      {!userState.homePageIn && (

        <Link className='absolute flex pl-[2vw] w-[8vw] hover:bg-hover_blue h-full' to="#" onClick={() => window.history.back()}>
            <FaChevronLeft className='mt-[1.4vh] pr-[calc(0.5vw)]'/>
            Powrót
        </Link>
      )}
      <Link className=' ml-[9vw] px-[3vw] hover:bg-hover_blue h-full' to="/">Moje Grupy</Link>
      <Link className='px-[2.5vw] hover:bg-hover_blue h-full' to={`/${userState.userState.userId}/assignments`}>
        Moje zadanie domowe
      </Link>
        <Link className='px-[2.5vw] hover:bg-hover_blue h-full' to={`/settings`}>
            Ustawienia
        </Link>
    </div>
  )
}
export default HeaderLoggedIn
