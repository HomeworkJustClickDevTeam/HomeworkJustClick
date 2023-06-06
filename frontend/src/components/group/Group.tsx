import { Outlet, useParams } from "react-router-dom"
import React, { useContext, useEffect, useState } from "react"
import userContext from "../../UserContext"
import postgresqlDatabase from "../../services/default-request-database"
import { Action } from "../../types/types"

import DispatchContext from "../../DispatchContext"
import GroupHeader from "./GroupHeader"

import GroupSetRoleContext from "../../GroupSetRoleContext"
import GroupRoleContext from "../../GroupRoleContext"
import Loading from "../animations/Loading"

function Group() {
  const { id = "" } = useParams<{ id: string }>()
  const { loggedIn, userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  const { setRole } = useContext(GroupSetRoleContext)
  const { role } = useContext(GroupRoleContext)
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])

  function checkRole() {
    postgresqlDatabase
      .get(`/group/userCheckWithRole/${userState.userId}/${id}`)
      .then((r) => setRole(r.data))
      .catch(() => setRole("User not in group"))
  }
  useEffect(() => {
    checkRole()
    setLoading(false)
  }, [])

  async function addToGroup() {
    await postgresqlDatabase.post(`/group/addStudent/${userState.userId}/${id}`)
    checkRole()
  }

  if (loading) {
    return <Loading />
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
      <GroupHeader />
      <Outlet />
    </div>
  )
}

export default Group
