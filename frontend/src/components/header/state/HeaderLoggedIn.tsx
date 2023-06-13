import { useContext } from "react"
import userContext from "../../../UserContext"
import { Link } from "react-router-dom"

function HeaderLoggedIn() {
  const userState = useContext(userContext)
  return (
    <div className='h-10 bg-main_blue'>
      {!userState.homePageIn && (
        <Link to="#" onClick={() => window.history.back()}>
          Powrót
        </Link>
      )}
      <Link to="/">Moje Grupy</Link>
      <Link to={`/${userState.userState.userId}/assignments`}>
        Moje zadanie domowe
      </Link>
        <Link to={`/settings`}>
            Ustawienia
        </Link>
    </div>
  )
}
export default HeaderLoggedIn
