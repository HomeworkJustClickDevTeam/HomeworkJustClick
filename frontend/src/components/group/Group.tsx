import { Outlet, useParams } from "react-router-dom"
import React, { useContext, useEffect } from "react"
import userContext from "../../UserContext"
import common_request from "../../services/default-request-database"
import { Action } from "../../types/types"

import DispatchContext from "../../DispatchContext"
import GroupHeader from "./GroupHeader"

import GroupSetRoleContext from "../../GroupSetRoleContext"
import GroupRoleContext from "../../GroupRoleContext"

function Group() {
  const { id = "" } = useParams<{ id: string }>()
  const { loggedIn, userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  const { setRole } = useContext(GroupSetRoleContext)
  const { role } = useContext(GroupRoleContext)

  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])

  function checkRole() {
    common_request
      .get(`/group/userCheckWithRole/${userState.userId}/${id}`)
      .then((r) => setRole(r.data))
      .catch((e) => console.log(e))
  }
  useEffect(() => {
    checkRole()
  }, [])

  async function addToGroup() {
    await common_request.post(
      `/group/addStudent/${localStorage.getItem("id")}/${id}`
    )
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
      <GroupHeader />
      <Outlet />
    </div>
  )
}

export default Group
