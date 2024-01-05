import { FaChevronLeft, FaCog } from "react-icons/fa"
import { Link, useLocation, useNavigate } from "react-router-dom"
import LogOut from "../user/LogOut"
import { selectHomePageIn } from "../../redux/homePageInSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import Logo from "./Logo.svg"

function HeaderLoggedInState() {

  const navigate = useNavigate()
  const homePageIn = useAppSelector(selectHomePageIn)
  const userState = useAppSelector(selectUserState)
  const location = useLocation()
  const isAssignmentsPage = location.pathname === '/' + userState?.id.toString() + '/assignments';
  const isGroupsPage = location.pathname === '/';

  return (
    <section id="headerLoggedInSection" className="relative flex h-10 xl:h-16 text-white text-[12px] xl:text-lg font-lato font-normal bg-main_blue items-center select-none">
      <Link to="/" className="scale-50 xl:scale-100 absolute left-1/2 transform -translate-x-1/2 ">
        <img src={Logo} alt="Logo" />
      </Link>
      <div className="flex items-center h-full">
        <Link to="#" className={`flex mr-2 xl:mr-3 hover:bg-hover_blue h-full items-center px-2 xl:px-4 ${homePageIn ? 'invisible' : 'visible'}`} onClick={() => navigate(-1)}>
          <FaChevronLeft className="pr-2" />
          Powrót
        </Link>
        <Link to="/" className={`flex items-center h-full rounded px-2 xl:px-4 ${isGroupsPage ? 'bg-main_lily hover:bg-hover_lily' : 'hover:bg-hover_blue'}`}>Moje grupy</Link>
        <Link to={`/${userState?.id}/assignments`} className={`flex items-center h-full rounded px-2 xl:px-4  ${isAssignmentsPage ? 'bg-main_lily hover:bg-hover_lily' : 'hover:bg-hover_blue'}`}>Moje zadanie domowe</Link>
      </div>
      <div className="absolute flex right-0 items-center h-full">
        <span className="pr-3 xl:pr-5">{userState?.firstname} {userState?.lastname}</span>
        <Link to="/settings">
          <FaCog className="m-2" />
        </Link>
        <div className="flex items-center h-full hover:bg-hover_blue px-2 xl:px-4">
          <LogOut />
        </div>
      </div>
    </section >
  )
}

export default HeaderLoggedInState
