import React, {useState} from 'react';

import './assets/App.css';
import Register from "./components/user/Register";
import Login from "./components/user/Logedd/Login";
import Home from "./components/Home";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import HomeGuest from "./components/HomeGuest";
import CreateGroup from "./components/group/CreateGroup";
import GroupFullDesc from "./components/group/GroupFullDesc";
import UserContext from './UserContext';
import Assignments from "./components/homework/Assignments";

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
            <Route path="/group/fullDescription/:id" element={<GroupFullDesc/>} />
            <Route path="group/assignments/:id" element={<Assignments />} />
        </Routes>
    </BrowserRouter>
    </UserContext.Provider>
  );
}

export default App;
