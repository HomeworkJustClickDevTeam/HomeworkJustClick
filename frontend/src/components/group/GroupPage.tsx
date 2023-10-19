import { Outlet, useNavigate, useParams } from "react-router-dom"
import React from "react"
import { addStudentToGroupPostgresService } from "../../services/postgresDatabaseServices"
import GroupHeader from "./GroupHeader"
import Loading from "../animations/Loading"
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetGroupAndRole } from "../customHooks/useGetGroupAndRole"

function GroupPage() {
  const {idGroup} = useParams()
  const userState = useAppSelector(selectUserState)
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const navigate = useNavigate()
  useGetGroupAndRole(idGroup as unknown as number, userState?.id)

  if (group === undefined || group === null) {
    return <Loading/>
  }
  async function addToGroup() {
    await addStudentToGroupPostgresService(userState?.id as unknown as string, group?.id as unknown as string)
      .then(() => navigate("/"))
  }
  if (role === "User not in group") {
    return (
      <>
        User not in group
        <button onClick={() => addToGroup()}> Add to group</button>
      </>
    )
  }


  return (
    <div>
      <GroupHeader key={group.id}/>
      <Outlet/>
    </div>
  )
}

export default GroupPage
