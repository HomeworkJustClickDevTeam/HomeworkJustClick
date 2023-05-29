import {useEffect, useState} from "react";
import {GroupProp, UserToShow} from "../../../../types/types";
import common_request from "../../../../services/default-request-database";
import UserItem from "./UserItem/UserItem";

function StudentsDisplayer({id}:GroupProp) {
    const [students,setStudents] = useState<UserToShow []>()
    useEffect(() => {

        common_request.get("/user/getStudentsByGroup/"+id).then(response => {
            const student : UserToShow [] = response.data
            setStudents(student)
        })
    },[])
    return(
        <div>
            <h1>Students:</h1>
            <ul>
            {
                students?.map(student =>
                    <li key={student.id}>
                     <UserItem userToShow={student} />
                    </li>
                )
            }
            </ul>
        </div>
    )

}
export default StudentsDisplayer