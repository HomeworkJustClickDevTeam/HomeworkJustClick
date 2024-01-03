import { Link } from "react-router-dom"
import Logo from "./Logo.svg"

function HeaderLoggedOutState() {
  return (
    <div id={"headerLoggedOut"} className='relative flex h-10 xl:h-16 bg-main_blue items-center justify-center select-none'>
      <Link to="/" className="scale-50 xl:scale-100">
        <img src={Logo} alt="Logo"></img>
      </Link>
    </div>
  )
}

export default HeaderLoggedOutState