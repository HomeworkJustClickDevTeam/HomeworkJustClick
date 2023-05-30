import { Outlet, useParams } from "react-router-dom"
import React, { useContext, useEffect } from "react"
import userContext from "../../UserContext"
import common_request from "../../services/default-request-database"
import { Action } from "../../types/types"

import DispatchContext from "../../DispatchContext"
import GroupHeader from "./GroupHeader"
import GroupContext from "../../GroupContext"

function Group() {
  const { id = "" } = useParams<{ id: string }>()
  const { loggedIn, userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  const { role, setRole } = useContext(GroupContext)

  useEffect(() => {
    const action: Action = {
      type: "homePageOut",
    }
    globalDispatch?.(action)
  }, [])

  useEffect(() => {
    common_request
      .get(`/group/userCheckWithRole/${userState.userId}/${id}`)
      .then((r) => setRole(r.data))
      .catch((e) => console.log(e))
  }, [])

  if (!loggedIn) {
    return <>Not log in</>
  }
  if (role === "User not in group") {
    return <>User not in group</>
  }
  return (
    <div>
      <GroupHeader />
      <Outlet />
    </div>
  )
}

export default Group
