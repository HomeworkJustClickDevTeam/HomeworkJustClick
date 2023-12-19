import { Link } from "react-router-dom"
import Logo from "./Logo.svg"

function HeaderLoggedOutState() {
  return (
    <div id={"headerLoggedOut"} className='relative flex h-[32px] xl:h-[64px] bg-main_blue items-center justify-center select-none'>
      <Link to="/"> 
        <img className="scale-50 relative top-[6%] xl:scale-100 xl:transform-none" src={Logo} alt="Logo"></img>
      </Link>
    </div>
  )
}

export default HeaderLoggedOutState