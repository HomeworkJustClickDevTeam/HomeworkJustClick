import { Outlet, useParams } from "react-router-dom"
import React, {useContext, useEffect, useRef, useState} from "react"
import userContext from "../../UserContext"
import postgresqlDatabase from "../../services/postgresDatabase"
import { Action } from "../../types/types"

import DispatchContext from "../../DispatchContext"
import GroupHeader from "./GroupHeader"

import GroupSetRoleContext from "../../GroupSetRoleContext"
import GroupRoleContext from "../../GroupRoleContext"
import Loading from "../animations/Loading"
import { Group as GroupType} from "../../types/types"

function Group() {
  const { id = "" } = useParams<{ id: string }>()
  const { loggedIn, userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  const { setRole } = useContext(GroupSetRoleContext)
  const { role } = useContext(GroupRoleContext)
  const [group, setGroup] = useState<GroupType | undefined>(undefined);


  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
    checkRole()
    getNameAndDesc();
  }, [])

  if(group === undefined || group === null){
    return <Loading/>
  }

  function checkRole() {
    postgresqlDatabase
      .get(`/group/userCheckWithRole/${userState.userId}/${id}`)
      .then((r) => setRole(r.data))
      .catch(() => setRole("User not in group"))
  }


  function getNameAndDesc() {
    postgresqlDatabase
        .get(`/group/${id}`)
        .then((r) => setGroup(r.data))
  }


  async function addToGroup() {
    await postgresqlDatabase.post(`/group/addStudent/${userState.userId}/${id}`)
    checkRole()
  }


  if (!loggedIn) {
    return <>Not log in</>
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
      <GroupHeader group = {group} key={group.id}/>
      <Outlet />
    </div>
  )
}

export default Group
