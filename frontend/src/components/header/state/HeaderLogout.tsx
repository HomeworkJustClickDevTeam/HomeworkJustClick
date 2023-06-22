import { Link, useNavigate } from "react-router-dom"
import { useContext } from "react"
import userContext from "../../../UserContext"

function HeaderLogout() {
  const userState = useContext(userContext)
  const navigate = useNavigate()
  return (
    <>
      {!userState.homePageIn && (
        <Link to="#" onClick={() => navigate(-1)}>
          Powrót
        </Link>
      )}
    </>
  )
}
export default HeaderLogout
