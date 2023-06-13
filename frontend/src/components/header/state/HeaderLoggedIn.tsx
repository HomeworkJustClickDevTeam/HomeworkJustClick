import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link } from "react-router-dom"
import { FaChevronLeft } from 'react-icons/fa';

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  return (
    <div className='relative flex h-6 text-white font-lato font-normal text-[11px] bg-main_blue items-center leading-6 '>
      {!userState.homePageIn && (

        <Link className='absolute flex pl-3 w-20 hover:bg-hover_blue h-full' to="#" onClick={() => window.history.back()}>
            <FaChevronLeft className='mt-[6px] pr-1'/>
            Powrót
        </Link>
      )}
      <Link className=' ml-20 px-6 hover:bg-hover_blue h-full' to="/">Moje Grupy</Link>
      <Link className='px-6 hover:bg-hover_blue h-full' to={`/${userState.userState.userId}/assignments`}>
        Moje zadanie domowe
      </Link>
        <Link className='px-6 hover:bg-hover_blue h-full' to={`/settings`}>
            Ustawienia
        </Link>
    </div>
  )
}
export default HeaderLoggedIn
