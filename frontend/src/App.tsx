import React, {useState} from 'react';

import './assets/App.css';
import Register from "./components/user/Register";
import Login from "./components/user/logging/Login";
import Home from "./components/Home";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import HomeGuest from "./components/HomeGuest";
import CreateGroup from "./components/group/CreateGroup";
import Group from "./components/group/Group";
import UserContext from './UserContext';
import AssignmentsDisplayer from "./components/assigments/assigmentDisplayer/AssignmentsDisplayer";
import AddAssigment from "./components/assigments/AddAssigment";
import AssigmentSpec from "./components/assigments/AssigmentSpec";


function App() {
    const [loggedIn,setLoggedIn] = useState<boolean>(Boolean(localStorage.getItem("token")))
    return (
    <UserContext.Provider value={{loggedIn, setLoggedIn}}>
    <BrowserRouter>
        <Routes>
            <Route path="/" element={loggedIn ? <Home  /> : <HomeGuest/>} />
            <Route path="/login" element={<Login />} />
            <Route path="/register"  element={<Register/>}/>
            <Route path="/create/group" element={<CreateGroup/>} />
            <Route path="/group/:id" element={<Group/>} />
            <Route path="group/:id/assignments" element={<AssignmentsDisplayer />} />
            <Route path="group/:id/assignments/add" element={<AddAssigment/>} />
            <Route path="group/:id/assigment/:idAssigment" element={<AssigmentSpec/>} />
        </Routes>
    </BrowserRouter>
    </UserContext.Provider>
  );
}

export default App;
