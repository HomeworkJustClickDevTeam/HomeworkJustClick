import React, { useEffect, useState } from "react"

import './App.css'
// import "./assets/App.css"
import Register from "./components/user/Register"
import Login from "./components/user/logging/Login"
import Home from "./components/home/Home"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import HomeGuest from "./components/home/HomeGuest"
import CreateGroup from "./components/group/CreateGroup"
import Group from "./components/group/Group"
import UserContext from "./UserContext"
import AssignmentsGroupDisplayed from "./components/assigments/assigmentDisplayer/AssignmentsGroupDisplayed"
import AddAssigment from "./components/assigments/AddAssigment"
import AssigmentSpec from "./components/assigments/AssigmentSpec"
import { Action, ApplicationState } from "./types/types"
import { useImmerReducer } from "use-immer"
import DispatchContext from "./DispatchContext"
import Header from "./components/header/Header"
import Users from "./components/group/users/Users"
import NotFound from "./components/errors/NotFound"
import GroupRoleContext from "./GroupRoleContext"
import GroupSetRoleContext from "./GroupSetRoleContext"
import AssignmentsTypes from "./components/assigments/AssignmentsTypes"
import AssignmentsStudentDisplayed from "./components/assigments/assigmentDisplayer/AssignmentsStudentDisplayed"
import SolutionsTypes from "./components/solution/SolutionsTypes"
import Solution from "./components/solution/Solution"
import UserSettings from "./components/user/settings/UserSettings"
import UserGeneralSettings from "./components/user/settings/UserGeneralSettings"
import UserSecuritySettings from "./components/user/settings/UserSecuritySettings"
import UserAppearanceSettings from "./components/user/settings/UserAppearanceSettings"
import UserMarkingTablesSettings from "./components/user/settings/UserMarkingTablesSettings"
import GroupSettings from "./components/group/settings/GroupSettings"
import UserProfileInGroup from "./components/group/users/UserProfileInGroup"
import HardCodedExample from "./components/solution/HardCodedExample";

function App() {
  useEffect(() => {
    if (localStorage.getItem("token") && localStorage.getItem("id")) {
      localStorage.removeItem("token")
      localStorage.removeItem("id")
    }
  }, [])
  const initialState: ApplicationState = {
    loggedIn: Boolean(localStorage.getItem("token")),
    homePageIn: true,
    userState: {
      token: localStorage.getItem("token")!,
      userId: localStorage.getItem("id")!,
    },
  }
  const [role, setRole] = useState<string>("")
  function ourReducer(draft: ApplicationState, action: Action) {
    switch (action.type) {
      case "login":
        draft.loggedIn = true
        draft.userState = action.data
        break
      case "logout":
        draft.loggedIn = false
        break
      case "homePageIn":
        draft.homePageIn = true
        break
      case "homePageOut":
        draft.homePageIn = false
        break
    }
  }
  const [state, dispatch] = useImmerReducer<ApplicationState, Action>(
    ourReducer,
    initialState
  )

  useEffect(() => {
    if (state.loggedIn) {
      localStorage.setItem("token", state.userState.token)
      localStorage.setItem("id", state.userState.userId)
    } else {
      localStorage.removeItem("token")
      localStorage.removeItem("id")
    }
  }, [state])

  return (
    <UserContext.Provider value={state}>
      <DispatchContext.Provider value={dispatch}>
        <GroupRoleContext.Provider value={{ role }}>
          <GroupSetRoleContext.Provider value={{ setRole }}>
            <BrowserRouter>
              <Header />
              <Routes>
                <Route
                  path="/"
                  element={state.loggedIn ? <Home /> : <HomeGuest />}
                />
                <Route
                  path="/:id/assignments"
                  element={<AssignmentsStudentDisplayed />}
                />
                <Route path="/settings" element={<UserSettings />} />
                <Route
                  path="/settings/general"
                  element={<UserGeneralSettings />}
                />
                <Route
                  path="/settings/security"
                  element={<UserSecuritySettings />}
                />
                <Route
                  path="/settings/appearance"
                  element={<UserAppearanceSettings />}
                />
                <Route
                  path="/settings/markingTables"
                  element={<UserMarkingTablesSettings />}
                />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/create/group" element={<CreateGroup />} />
                <Route path="/group/:id" element={<Group />}>
                  <Route path="settings" element={<GroupSettings />} />
                  <Route path="users" element={<Users />} />
                  <Route
                    path="userProfileInGroup/:userProfileId"
                    element={<UserProfileInGroup />}
                  />
                  <Route
                    path="assignments"
                    element={<AssignmentsGroupDisplayed />}
                  />
                  <Route
                    path="assignments/done"
                    element={<AssignmentsTypes type={"done"} />}
                  />
                  <Route
                    path="assignments/expired"
                    element={<AssignmentsTypes type={"expired"} />}
                  />
                  <Route
                    path="assignments/todo"
                    element={<AssignmentsTypes type={"todo"} />}
                  />
                  <Route path="assignments/add" element={<AddAssigment />} />
                  <Route
                    path="assigment/:idAssigment"
                    element={<AssigmentSpec />}
                  />
                  <Route
                    path="solution/:idUser/:idAssigment"
                    element={<Solution />}/>
                  <Route path="solution/:idUser/:idAssigment/example" element={<HardCodedExample/>}/>
                  <Route
                    path="solutions/uncheck"
                    element={<SolutionsTypes type={"uncheck"} />}
                  />
                  <Route
                    path="solutions/late"
                    element={<SolutionsTypes type={"late"} />}
                  />
                  <Route
                    path="solutions/check"
                    element={<SolutionsTypes type={"check"} />}
                  />

                  <Route path="*" element={<NotFound />} />
                </Route>

                <Route path="*" element={<NotFound />} />
              </Routes>
            </BrowserRouter>
          </GroupSetRoleContext.Provider>
        </GroupRoleContext.Provider>
      </DispatchContext.Provider>
    </UserContext.Provider>
  )
}

export default App
