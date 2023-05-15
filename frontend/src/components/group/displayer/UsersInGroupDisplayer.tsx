import {useEffect, useState} from "react";
import common_request from "../../../services/default-request-database";
import {GroupProp, UserToShow} from "../../../types/types";

function UsersInGroupDisplayer({id}:GroupProp){

    const [students,setStudents] = useState<UserToShow []>()
    const [teachers,setTeachers] = useState<UserToShow []>()
    useEffect(() => {
        common_request.get("/user/getTeachersByGroup/"+id).then(response =>{
            const teacher: UserToShow[] = response.data
            setTeachers(teacher)
        }).catch(e => console.log(e))
        common_request.get("/user/getStudentsByGroup/"+id).then(response => {
            const student : UserToShow [] = response.data
            setStudents(student)
        })
    },[])
    return(
        <>
            <div>
                <h1>Teachers:</h1>
                {
                    teachers?.map(teacher =>
                        <div key={teacher.id}>
                            <p>{teacher.firstname}</p>
                            <p>{teacher.lastname}</p>
                        </div>
                    )
                }
            </div>
            <div>
                <h1>Students:</h1>
                {
                    students?.map(student =>
                        <div key={student.id}>
                            <p>{student.firstname}</p>
                            <p>{student.lastname}</p>

                        </div>
                    )
                }
            </div>
        </>
    )
}
export default UsersInGroupDisplayer