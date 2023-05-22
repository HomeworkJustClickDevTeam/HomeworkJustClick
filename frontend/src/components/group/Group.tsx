import {Link, useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import userContext from "../../UserContext";
import common_request from "../../services/default-request-database";
import {InGroup} from "../../types/types";

import Users from "./users/Users";

function Group() {
    const {id=""} = useParams<{id:string}>()
    const {loggedIn} = useContext(userContext)
    const [inGroup, setInGroup] = useState<InGroup>({
        student: false,
        teacher: false,
    })


    function checkTeacher() {
        common_request.get(`/group/teacherCheck/${localStorage.getItem("id")}/${id}`).then((response) => {
            const teacher = response.data
            setInGroup((prevState) => ({
                ...prevState,
                teacher,
            }))
        }).catch((e) => console.log(e))
    }


    function checkStudent() {
        common_request.get(`/group/studentCheck/${localStorage.getItem("id")}/${id}`).then((response) => {
            const student = response.data
            setInGroup((prevState) => ({
                ...prevState,
                student,
            }))
        }).catch((e) => console.log(e))
    }

    function checkIsInGroup() {
        checkTeacher()
        checkStudent()

    }

    useEffect(() => {
            checkIsInGroup()
        }
        , [])

    async function addToGroup() {
        await common_request.post(`/group/addStudent/${localStorage.getItem("id")}/${id}`)
        checkIsInGroup()
    }

    if (loggedIn) {
        if (inGroup.student || inGroup.teacher) {
            return (
                <Users id={id} role={inGroup} />
            )
        } else {
            return (<h1>
                Nie jestes w grupie
                <button onClick={addToGroup}> Add to group</button>
            </h1>)
        }
    } else {
        return (
            <>
                <Link to={"/login"}>
                    <button>
                        Zaloguj się
                    </button>
                </Link>
            </>
        )
    }
}

export default Group