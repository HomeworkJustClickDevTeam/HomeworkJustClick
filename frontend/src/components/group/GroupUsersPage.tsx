import GroupUsersDisplayer from "./GroupUsersDisplayer"
import {Outlet, useParams} from "react-router-dom"

function GroupUsersPage() {
  const { id } = useParams()
  return (
    <>
      <GroupUsersDisplayer id={id as string} />
    </>
  )
}
export default GroupUsersPage
