import {Outlet, useParams} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import {
  getGroupPostgresService,
  getGroupUserCheckWithRolePostgresService,
  postGroupAddStudentPostgresService
} from "../../services/postgresDatabaseServices"
import GroupHeader from "./GroupHeader"
import ApplicationStateContext from "../../contexts/ApplicationStateContext"
import Loading from "../animations/Loading"
import {GroupInterface} from "../../types/GroupInterface";
import {getUser} from "../../services/otherServices";

function GroupPage() {
  const {applicationState, setApplicationState} = useContext(ApplicationStateContext)
  const {idGroup} = useParams()

  useEffect(() => {
    getGroup()
  }, []);


   function getGroup() {
     getGroupPostgresService(idGroup as string)
      .then((r) => {
        setApplicationState({type: "setGroupView", group: r.data})
        getGroupUserCheckWithRolePostgresService(applicationState?.userState?.id as unknown as string, r.data.id)
          .then((r) => setApplicationState({type: "setGroupViewRole", role: r.data}))
          .catch(() => setApplicationState({type: "setGroupViewRole", role:"User not in group"}))
      })
  }

  if (applicationState?.group === undefined || applicationState?.group === null) {
    return <Loading/>
  }



  async function addToGroup() {
    await postGroupAddStudentPostgresService(applicationState?.userState?.id as unknown as string, applicationState?.group?.id as unknown as string)
    getGroupUserCheckWithRolePostgresService(applicationState?.userState?.id as unknown as string, applicationState?.group?.id as unknown as string)
      .then((r) => setApplicationState({type: "setGroupViewRole", role: r.data}))
      .catch(() => setApplicationState({type: "setGroupViewRole", role:"User not in group"}))
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
