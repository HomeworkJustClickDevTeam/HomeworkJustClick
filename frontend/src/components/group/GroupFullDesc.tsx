import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import common_request from "../../services/default-request-database";

function GroupFullDesc() {
    const {id} = useParams()
    const [students,setStudents] = useState<UserToShow []>()
    const [teachers,setTeachers] = useState<UserToShow []>()
    const config = {headers:{
            Authorization:`Bearer ${localStorage.getItem("token")}`}}

    useEffect(() => {
        common_request.get("/user/getTeachersByGroup/"+id,config).then(response =>{
            const teacher: UserToShow[] = response.data
            setTeachers(teacher)
        }).catch(e => console.log(e))
        common_request.get("/user/getStudentsByGroup/"+id,config).then(response => {
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
                        <div>
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
                        <div>
                            <p>{student.firstname}</p>
                            <p>{student.lastname}</p>

                        </div>
                    )
                }
            </div>
            <Link to="/">
                <button type="button">
                    Strona główna
                </button>
            </Link>
        </>
    )

}
export default GroupFullDesc