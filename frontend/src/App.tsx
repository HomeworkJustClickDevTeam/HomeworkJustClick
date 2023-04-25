import React, {useState} from 'react';

import './assets/App.css';
import Register from "./components/user/Register";
import Login from "./components/user/Logedd/Login";
import Home from "./components/Home";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import HomeGuest from "./components/HomeGuest";
import CreateGroup from "./components/group/CreateGroup";
import GroupFullDesc from "./components/group/GroupFullDesc";

function App() {
    const [loggedIn,setLoggedIn] = useState<Boolean>(Boolean(localStorage.getItem("token")))
    return (
    <BrowserRouter>
        <Routes>
            <Route path="/" element={loggedIn ? <Home setLoggedIn={setLoggedIn} /> : <HomeGuest/>} />
            <Route path="/login" element={<Login setLoggedIn={setLoggedIn}/>} />
            <Route path="/register"  element={<Register/>}/>
            <Route path="/create/group" element={<CreateGroup/>} />
            <Route path="/group/fullDescription/:id" element={<GroupFullDesc/>} />
        </Routes>
    </BrowserRouter>
  );
}

export default App;
