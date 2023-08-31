import React, {useReducer, useState} from "react"

import './App.css'
// import "./assets/App.css"
import RegisterPage from "./components/user/RegisterPage"
import LoginPage from "./components/user/LoginPage"
import HomePage from "./components/home/HomePage"
import {BrowserRouter, Route, Routes} from "react-router-dom"
import HomeGuestPage from "./components/home/HomeGuestPage"
import GroupCreatePage from "./components/group/GroupCreatePage"
import GroupPage from "./components/group/GroupPage"
import AssignmentsGroupDisplayedPage from "./components/assigments/AssignmentsGroupDisplayedPage"
import AddAssigmentPage from "./components/assigments/AddAssigmentPage"
import AssigmentSpecPage from "./components/assigments/AssigmentSpecPage"
import HomePageContext from "./contexts/HomePageContext"
import GroupUsersPage from "./components/group/GroupUsersPage"
import NotFoundPage from "./components/errors/NotFoundPage"
import ApplicationStateContext from "./contexts/ApplicationStateContext"
import AssignmentsTypesPage from "./components/assigments/AssignmentsTypesPage"
import AssignmentsStudentDisplayedPage from "./components/assigments/AssignmentsStudentDisplayedPage"
import SolutionsTypesPage from "./components/solution/SolutionsTypesPage"
import SolutionPage from "./components/solution/SolutionPage"
import UserSettingsPage from "./components/user/UserSettingsPage"
import UserGeneralSettingsPage from "./components/user/UserGeneralSettingsPage"
import UserSecuritySettingsPage from "./components/user/UserSecuritySettingsPage"
import UserAppearanceSettingsPage from "./components/user/UserAppearanceSettingsPage"
import UserMarkingTablesSettingsPage from "./components/user/UserMarkingTablesSettingsPage"
import GroupSettingsPage from "./components/group/GroupSettingsPage"
import GroupUserProfilePage from "./components/group/GroupUserProfilePage"
import HardCodedExamplePage from "./components/solution/HardCodedExamplePage";
import {LoggedInUserRoute} from "./components/route/LoggedInUserRoute";
import HeaderLoggedInState from "./components/header/HeaderLoggedInState";
import {LoggedOutUserRoute} from "./components/route/LoggedOutUserRoute";
import {ApplicationStateInterface} from "./types/ApplicationStateInterface";
import {ActionTypes} from "./types/ActionTypes";

function App() {
  const [homePageIn, setHomePageIn] = useState<boolean>(true)
  const initialUserState = {
    userState: undefined,
    role: undefined,
    group: undefined
  }
  function reducer(state: ApplicationStateInterface, action: ActionTypes): ApplicationStateInterface{
    switch (action.type){
      case "logOut":
        return {userState: undefined, role: undefined, group:undefined}
      case "logIn":
        return {userState: action.userState, role: undefined, group: undefined}
      case "setGroupView":
        return {group: action.group}
      case "setGroupViewRole":
        return {role: action.role}
    }
  }

  const [applicationState, setApplicationState] = useReducer(reducer, initialUserState)

  return (
    <HomePageContext.Provider value={{setHomePageIn, homePageIn}}>
      <ApplicationStateContext.Provider value={{applicationState, setApplicationState}}>
          <BrowserRouter>
            <Routes>
              <Route element={<LoggedOutUserRoute/>}>
                <Route
                  path="/home"
                  element={<HomeGuestPage/>}
                />
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
              </Route>
              <Route element={<LoggedInUserRoute/>}>
                <Route
                  path="/"
                  element={<HomePage/>}
                />
                <Route
                  path="/:id/assignments"
                  element={<AssignmentsStudentDisplayedPage/>}
                />
                <Route path="/settings" element={<UserSettingsPage/>}/>
                <Route path="/create/group" element={<GroupCreatePage/>}/>
                <Route path="/group/:idGroup" element={<GroupPage/>}>
                  <Route path="settings" element={<GroupSettingsPage/>}/>
                  <Route path="users" element={<GroupUsersPage/>}/>
                  <Route
                    path="userProfileInGroup/:userProfileId"
                    element={<GroupUserProfilePage/>}
                  />
                  <Route
                    path="assignments"
                    element={<AssignmentsGroupDisplayedPage/>}
                  />
                  <Route
                    path="assignments/done"
                    element={<AssignmentsTypesPage type={"done"}/>}
                  />
                  <Route
                    path="assignments/expired"
                    element={<AssignmentsTypesPage type={"expired"}/>}
                  />
                  <Route
                    path="assignments/todo"
                    element={<AssignmentsTypesPage type={"todo"}/>}
                  />
                  <Route path="assignments/add" element={<AddAssigmentPage/>}/>
                  <Route
                    path="assigment/:idAssigment"
                    element={<AssigmentSpecPage/>}
                  />
                  <Route
                    path="solution/:idUser/:idAssigment"
                    element={<SolutionPage/>}/>
                  <Route
                    path="solutions/uncheck"
                    element={<SolutionsTypesPage type={"uncheck"}/>}
                  />
                  <Route
                    path="solutions/late"
                    element={<SolutionsTypesPage type={"late"}/>}
                  />
                  <Route
                    path="solutions/check"
                    element={<SolutionsTypesPage type={"check"}/>}
                  />
                  <Route path="*" element={<NotFoundPage/>}/>
                </Route>
                <Route path="/group/:id/solution/:idUser/:idAssigment/example" element={<HardCodedExamplePage/>}/>
              </Route>
              <Route path="*" element={<NotFoundPage/>}/>
            </Routes>
          </BrowserRouter>
      </ApplicationStateContext.Provider>
    </HomePageContext.Provider>
  )
}

export default App
