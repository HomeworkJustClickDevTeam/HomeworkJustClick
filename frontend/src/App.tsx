import React, { useEffect, useState } from "react"

import "./assets/App.css"
import Register from "./components/user/Register"
import Login from "./components/user/logging/Login"
import Home from "./components/home/Home"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import HomeGuest from "./components/home/HomeGuest"
import CreateGroup from "./components/group/CreateGroup"
import Group from "./components/group/Group"
import UserContext from "./UserContext"
import AssignmentsDisplayer from "./components/assigments/assigmentDisplayer/AssignmentsDisplayer"
import AddAssigment from "./components/assigments/AddAssigment"
import AssigmentSpec from "./components/assigments/AssigmentSpec"
import { Action, applicationState } from "./types/types"
import { useImmerReducer } from "use-immer"
import DispatchContext from "./DispatchContext"
import Header from "./components/header/Header"
import Users from "./components/group/users/Users"
import GroupContext from "./GroupContext"
import NotFound from "./components/errors/NotFound"

function App() {
  const initialState: applicationState = {
    loggedIn: Boolean(localStorage.getItem("token")),
    homePageIn: true,
    userState: {
      token: localStorage.getItem("token")!,
      userId: localStorage.getItem("id")!,
    },
  }
  const [role, setRole] = useState<string>("")
  function ourReducer(draft: applicationState, action: Action) {
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
  const [state, dispatch] = useImmerReducer<applicationState, Action>(
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
  }, [state.loggedIn])

  return (
    <UserContext.Provider value={state}>
      <DispatchContext.Provider value={dispatch}>
        <GroupContext.Provider value={{ role, setRole }}>
          <BrowserRouter>
            <Header />
            <Routes>
              <Route
                path="/"
                element={state.loggedIn ? <Home /> : <HomeGuest />}
              />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/create/group" element={<CreateGroup />} />
              <Route path="/group/:id" element={<Group />}>
                <Route path="users" element={<Users />} />
                <Route path="assignments" element={<AssignmentsDisplayer />} />
                <Route path="assignments/add" element={<AddAssigment />} />
                <Route
                  path="assigment/:idAssigment"
                  element={<AssigmentSpec />}
                />
                <Route path="*" element={<NotFound />} />
              </Route>
              <Route path="*" element={<NotFound />} />
            </Routes>
          </BrowserRouter>
        </GroupContext.Provider>
      </DispatchContext.Provider>
    </UserContext.Provider>
  )
}

export default App
