import {useContext} from "react"
import {FaChevronLeft} from 'react-icons/fa';
import {Link, useNavigate} from "react-router-dom"
import LogOut from "../user/LogOut"
import dispatchContext from "../../contexts/HomePageContext";
import {getUser} from "../../services/otherServices";
import HomePageContext from "../../contexts/HomePageContext";

function HeaderLoggedInState() {
  const navigate = useNavigate()
  const userState = getUser()
  const {homePageIn} = useContext(HomePageContext)
  return (
      <section className='relative flex h-16 text-white font-lato font-normal bg-main_blue items-center select-none'>
          {!homePageIn && (
              <Link className='absolute flex pl-[3vw] w-[9vw] hover:bg-hover_blue h-full items-center' to="#" onClick={() => navigate(-1)}>
                  <FaChevronLeft className='pr-[calc(0.5vw)]'/>
                  Powrót
              </Link>
          )}

              <Link className='flex items-center ml-[9vw] px-[3vw] hover:bg-hover_blue h-full' to="/">Moje Grupy</Link>
              <Link className='flex items-center px-[2.5vw] hover:bg-hover_blue h-full' to={`/${userState?.id}/assignments`}>
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
