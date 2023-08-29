import {Outlet, useParams} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import {
  getGroupPostgresService,
  getGroupUserCheckWithRolePostgresService,
  postGroupAddStudentPostgresService
} from "../../services/postgresDatabaseServices"

import HomePageContext from "../../contexts/HomePageContext"
import GroupHeader from "./GroupHeader"

import GroupSetRoleContext from "../../contexts/GroupSetRoleContext"
import GroupRoleContext from "../../contexts/GroupRoleContext"
import Loading from "../animations/Loading"
import {GroupInterface} from "../../types/GroupInterface";
import {getUser} from "../../services/otherServices";

function GroupPage() {
  const { idGroup = "" } = useParams<{ idGroup: string }>()
  const userState = getUser()
  const {setHomePageIn} = useContext(HomePageContext)
  const { setRole } = useContext(GroupSetRoleContext)
  const { role } = useContext(GroupRoleContext)
  const [group, setGroup] = useState<GroupInterface | undefined>(undefined);

  useEffect(() => {
    setHomePageIn(false)
    checkRole()
    getNameAndDesc();
  }, [])

  if(group === undefined || group === null){
    return <Loading/>
  }

  function checkRole() {
    getGroupUserCheckWithRolePostgresService(userState?.id as unknown as string, idGroup)
      .then((r) => setRole(r.data))
      .catch(() => setRole("User not in group"))
  }


  function getNameAndDesc() {
    getGroupPostgresService(idGroup)
        .then((r) => setGroup(r.data))
  }


  async function addToGroup() {
    await postGroupAddStudentPostgresService(userState?.id as unknown as string, idGroup)
    checkRole()
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
