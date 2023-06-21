import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link } from "react-router-dom"
import { FaChevronLeft } from 'react-icons/fa';

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  return (
      <div className='relative flex h-11 text-white font-lato font-normal text-[11px] bg-main_blue items-center'>
          {!userState.homePageIn && (
              <Link className='absolute flex pl-[3vw] w-[9vw] hover:bg-hover_blue h-full items-center' to="#" onClick={() => window.history.back()}>
                  <FaChevronLeft className='pr-[calc(0.5vw)]'/>
                  Powrót
              </Link>
          )}

              <Link className='flex items-center ml-[9vw] px-[3vw] hover:bg-hover_blue h-full' to="/">Moje Grupy</Link>
              <Link className='flex items-center px-[2.5vw] hover:bg-hover_blue h-full' to={`/${userState.userState.userId}/assignments`}>
                  Moje zadanie domowe
              </Link>
              <Link className='flex items-center px-[2.5vw] hover:bg-hover_blue h-full' to={`/settings`}>
                  Ustawienia
              </Link>

      </div>
  )
}
export default HeaderLoggedIn
