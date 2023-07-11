import GroupUsersDisplayer from "./GroupUsersDisplayer"
import {Outlet, useParams} from "react-router-dom"

function GroupUsersPage() {
  const { idGroup } = useParams()
  return (
    <>
      <GroupUsersDisplayer id={idGroup as string} />
    </>
  )
}
export default GroupUsersPage
