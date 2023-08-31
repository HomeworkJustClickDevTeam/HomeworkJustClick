import {Outlet, useParams} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import {
  getGroupPostgresService,
  getGroupUserCheckWithRolePostgresService,
  postGroupAddStudentPostgresService
} from "../../services/postgresDatabaseServices"

import HomePageContext from "../../contexts/HomePageContext"
import GroupHeader from "./GroupHeader"
import ApplicationStateContext from "../../contexts/ApplicationStateContext"
import Loading from "../animations/Loading"
import {GroupInterface} from "../../types/GroupInterface";
import {getUser} from "../../services/otherServices";

function GroupPage() {
  const {idGroup = ""} = useParams<{ idGroup: string }>()
  const {setHomePageIn} = useContext(HomePageContext)
  const {applicationState, setApplicationState} = useContext(ApplicationStateContext)

  useEffect(() => {
    setHomePageIn(false)
    checkRole()
    getNameAndDesc();
  }, [])

  if (applicationState?.group === undefined || applicationState?.group === null) {
    return <Loading/>
  }

  function checkRole() {
    getGroupUserCheckWithRolePostgresService(applicationState?.userState?.id as unknown as string, idGroup)
      .then((r) => setApplicationState({type: "setGroupViewRole",role: r.data}))
      .catch(() => setApplicationState({type: "setGroupViewRole", role:"User not in group"}))
  }


  function getNameAndDesc() {
    getGroupPostgresService(idGroup)
      .then((r) => setApplicationState({type: "setGroupView", group: r.data as GroupInterface}))
  }


  async function addToGroup() {
    await postGroupAddStudentPostgresService(applicationState?.userState?.id as unknown as string, idGroup)
    checkRole()
  }


  if (applicationState?.role === "User not in group") {
    return (
      <>
        User not in group
        <button onClick={addToGroup}> Add to group</button>
      </>
    )
  }


  return (
    <div>
      <GroupHeader group={applicationState.group} key={applicationState.group.id}/>
      <Outlet/>
    </div>
  )
}

export default GroupPage
