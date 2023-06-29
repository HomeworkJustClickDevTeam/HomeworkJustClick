import GroupUsersDisplayer from "./GroupUsersDisplayer"
import {Outlet, useParams} from "react-router-dom"

function GroupUsersPage() {
  const { id } = useParams()
  return (
    <>
      <GroupUsersDisplayer id={id} />
    </>
  )
}
export default GroupUsersPage
