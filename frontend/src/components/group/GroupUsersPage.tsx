import UsersDisplayer from "./UsersDisplayer"
import {Outlet, useParams} from "react-router-dom"

function GroupUsersPage() {
  const { id } = useParams()
  return (
    <>
      <UsersDisplayer id={id} />
    </>
  )
}
export default GroupUsersPage
