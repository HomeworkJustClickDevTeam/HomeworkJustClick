import { Outlet, useParams } from "react-router-dom"
import React, { useEffect } from "react"
import {
  addStudentToGroupPostgresService,
  getGroupPostgresService,
  getUserRoleInGroupPostgresService
} from "../../services/postgresDatabaseServices"
import GroupHeader from "./GroupHeader"
import Loading from "../animations/Loading"
import { selectUserState } from "../../redux/userStateSlice"
import { setHomePageIn } from "../../redux/homePageInSlice"
import { selectGroup, setGroup } from "../../redux/groupSlice"
import { selectRole, setRole } from "../../redux/roleSlice"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"

function GroupPage() {
  const {idGroup} = useParams()
  const userState = useAppSelector(selectUserState)
  const group= useAppSelector(selectGroup)
  const dispatch = useAppDispatch()
  const role = useAppSelector(selectRole)

  useEffect(() => {
    let mounted = true

    if(mounted){
      dispatch(setIsLoading(true))
      getGroup()
    }
    dispatch(setIsLoading(false))
    dispatch(setHomePageIn(false))
    return () => {mounted = false}
  }, []);


   function getGroup() {
     getGroupPostgresService(idGroup as string)
      .then((r) => {
        dispatch(setGroup(r.data))
        getUserRoleInGroupPostgresService(userState?.id as unknown as string, r.data.id)
          .then((r) => dispatch(setRole(r.data)))
          .catch(() => dispatch(setRole("User not in group")))
      })
  }

  if (group === undefined || group === null) {
    return <Loading/>
  }



  async function addToGroup() {
    await addStudentToGroupPostgresService(userState?.id as unknown as string, group?.id as unknown as string)
    getUserRoleInGroupPostgresService(userState?.id as unknown as string, group?.id as unknown as string)
      .then((r) => dispatch(setRole(r.data)))
      .catch(() => dispatch(setRole("User not in group")))
  }


  if (role === "User not in group") {
    return (
      <>
        User not in group
        <button onClick={addToGroup}> Add to group</button>
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
