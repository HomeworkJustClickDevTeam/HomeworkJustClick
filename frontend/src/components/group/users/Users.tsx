import UsersDisplayer from "./UsersDisplayer"
import { Link, useParams } from "react-router-dom"

function Users() {
  const { id } = useParams()
  return (
    <>
      <UsersDisplayer id={id} />
      <Link to={`assignments`}>
        <button>Zadania domowe</button>
      </Link>
      <Link to="/">
        <button type="button">Strona główna</button>
      </Link>
    </>
  )
}
export default Users
