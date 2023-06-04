import UsersDisplayer from "./UsersDisplayer"
import { useParams } from "react-router-dom"

function Users() {
  const { id } = useParams()
  return (
    <>
      <UsersDisplayer id={id} />
    </>
  )
}
export default Users
