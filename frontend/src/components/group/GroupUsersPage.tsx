import GroupUsersDisplayer from "./GroupUsersDisplayer"
import {useParams} from "react-router-dom"

function GroupUsersPage() {
  const { idGroup } = useParams()
  return (
    <>
      <GroupUsersDisplayer groupId={idGroup as string} />
    </>
  )
}
export default GroupUsersPage
