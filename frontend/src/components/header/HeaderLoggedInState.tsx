import { useContext } from "react"
import userContext from "../../UserContext"
import { FaChevronLeft } from 'react-icons/fa';
import { Link, useNavigate } from "react-router-dom"
import LogOut from "../user/LogOut"

function HeaderLoggedInState() {
  const userState = useContext(userContext)
  const navigate = useNavigate()
  return (
      <section className='relative flex h-16 text-white font-lato font-normal bg-main_blue items-center select-none'>
          {!userState.homePageIn && (
              <Link className='absolute flex pl-[3vw] w-[9vw] hover:bg-hover_blue h-full items-center' to="#" onClick={() => navigate(-1)}>
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
          <div className='absolute flex right-0 hover:bg-hover_blue h-full items-center pr-5 pl-4'>
            <LogOut/>
          </div>

      </section>

  )
}
export default HeaderLoggedInState