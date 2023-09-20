import GroupUsersDisplayer from "./GroupUsersDisplayer"
import { useContext } from "react"
import { selectGroup } from "../../redux/groupSlice"
import { useSelector } from "react-redux"
import { useAppSelector } from "../../types/HooksRedux"

function GroupUsersPage() {
  const group= useAppSelector(selectGroup)
  return (
    <>
      <GroupUsersDisplayer groupId={group?.id as unknown as string}/>
    </>
  )
}

export default GroupUsersPage
