import {Outlet, useParams} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import userContext from "../../contexts/UserContext"
import {
  getGroupPostgresService,
  getGroupUserCheckWithRolePostgresService,
  postGroupAddStudentPostgresService
} from "../../services/postgresDatabase"

import DispatchContext from "../../contexts/DispatchContext"
import GroupHeader from "./GroupHeader"

import GroupSetRoleContext from "../../contexts/GroupSetRoleContext"
import GroupRoleContext from "../../contexts/GroupRoleContext"
import Loading from "../animations/Loading"
import {GroupInterface} from "../../types/GroupInterface";
import {ActionType} from "../../types/ActionType";

function GroupPage() {
  const { idGroup = "" } = useParams<{ idGroup: string }>()
  const { loggedIn, userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  const { setRole } = useContext(GroupSetRoleContext)
  const { role } = useContext(GroupRoleContext)
  const [group, setGroup] = useState<GroupInterface | undefined>(undefined);


  useEffect(() => {
    const action: ActionType = {
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
    getGroupUserCheckWithRolePostgresService(userState.userId, idGroup)
      .then((r) => setRole(r.data))
      .catch(() => setRole("User not in group"))
  }


  function getNameAndDesc() {
    getGroupPostgresService(idGroup)
        .then((r) => setGroup(r.data))
  }


  async function addToGroup() {
    await postGroupAddStudentPostgresService(userState.userId, idGroup)
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

export default GroupPage
