import React from "react"

import "./App.css"
import RegisterPage from "./components/user/RegisterPage"
import LoginPage from "./components/user/LoginPage"
import HomePage from "./components/home/HomePage"
import {Route, Routes} from "react-router-dom"
import HomeGuestPage from "./components/home/HomeGuestPage"
import GroupCreatePage from "./components/group/GroupCreatePage"
import GroupPage from "./components/group/GroupPage"
import AssignmentsGroupTeacherDisplayedPage from "./components/assignment/AssignmentsGroupTeacherDisplayedPage"
import AssignmentAddSettingsPageWrapper from "./components/assignment/AssignmentAddSettingsPageWrapper"
import AssignmentSpecPage from "./components/assignment/AssignmentSpecPage"
import GroupUsersPage from "./components/group/GroupUsersPage"
import NotFoundPage from "./components/errors/NotFoundPage"
import AssignmentsTypesPage from "./components/assignment/AssignmentsTypesPage"
import AssignmentsMainPageWrapper from "./components/assignment/AssignmentsMainPageWrapper"
import SolutionsTypesPage from "./components/solution/SolutionsTypesPage"
import SolutionPage from "./components/solution/SolutionPage"
import UserSettingsPage from "./components/user/UserSettingsPage"
import GroupSettingsPage from "./components/group/GroupSettingsPage"
import GroupUserProfilePage from "./components/group/GroupUserProfilePage"
import AdvancedEvaluationPage from "./components/evaluation/AdvancedEvaluationPage"
import {LoggedInUserRoute} from "./components/route/LoggedInUserRoute"
import {LoggedOutUserRoute} from "./components/route/LoggedOutUserRoute"
import {AssignmentsType} from "./types/AssignmentsType"
import {ExtendedSolutionType} from "./types/ExtendedSolutionType"
import {RoleBasedRoute} from "./components/route/RoleBasedRoute";
import {SolutionCheckedAdvancedPage} from "./components/solution/SolutionCheckedAdvancedPage";
import {EvaluationReportedPage} from "./components/evaluation/EvaluationReportedPage";
import {ReportPage} from "./components/report/ReportPage";
import {EvaluationsStudentPage} from "./components/evaluation/EvaluationsStudentPage";
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {AssignmentsStudentGroupPageWrapper} from "./components/assignment/AssignmentsStudentGroupPageWrapper";

function App() {
    return (
        <>
            <Routes>
                <Route path="/group/:idGroup" element={<GroupPage/>}/>
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
                        element={<AssignmentsMainPageWrapper/>}
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
                            element={<RoleBasedRoute renderForStudent={<AssignmentsStudentGroupPageWrapper/>}
                                                     renderForTeacher={<AssignmentsGroupTeacherDisplayedPage/>}/>}
                        />
                        <Route
                            path="assignments/done"
                            element={<AssignmentsTypesPage type={"done" as AssignmentsType}/>}
                        />
                        <Route
                            path="assignments/expired"
                            element={<AssignmentsTypesPage type={"expiredUndone" as AssignmentsType}/>}
                        />
                        <Route
                            path="assignments/todo"
                            element={<AssignmentsTypesPage type={"undone" as AssignmentsType}/>}
                        />
                        <Route path="assignments/add" element={<AssignmentAddSettingsPageWrapper/>}/>
                        <Route
                            path="assignment/:idAssignment"
                            element={<AssignmentSpecPage/>}
                        />
                        <Route
                            path="solution/:idUser/:idAssignment"
                            element={<SolutionPage/>}/>
                        <Route
                            path="solutions/uncheck"
                            element={<SolutionsTypesPage type={ExtendedSolutionType.unchecked}/>}
                        />
                        <Route
                            path="solutions/late"
                            element={<SolutionsTypesPage type={ExtendedSolutionType.late}/>}
                        />
                        <Route
                            path="solutions/check"
                            element={<SolutionsTypesPage type={ExtendedSolutionType.checked}/>}
                        />
                        <Route path="evaluation/reported"
                               element={<EvaluationReportedPage/>}/>
                        <Route
                            path="report"
                            element={<ReportPage/>}
                        />
                        <Route
                            path="evaluations"
                            element={<EvaluationsStudentPage/>}
                        />

                    </Route>
                    <Route path="/group/:idGroup/advancedAssignment" element={
                        <RoleBasedRoute renderForStudent={<SolutionCheckedAdvancedPage/>}
                                        renderForTeacher={<AdvancedEvaluationPage/>}/>}/>
                </Route>
                <Route path="*" element={<NotFoundPage/>}/>
            </Routes>
            <ToastContainer limit={2} autoClose={2000}></ToastContainer>
        </>
    )
}

export default App
