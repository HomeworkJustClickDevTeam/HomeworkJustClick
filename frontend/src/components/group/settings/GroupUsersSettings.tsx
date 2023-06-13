import GroupUsersSettingsListElement from "./GroupUsersSettingsListElement";
import Loading from "../../animations/Loading";
import React, {useContext, useEffect, useState} from "react";
import userContext from "../../../UserContext";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {useParams} from "react-router-dom";
import {UserToShow} from "../../../types/types";
import {tr} from "date-fns/locale";

export default function GroupUsersSettings(){
  const {loggedIn, userState} = useContext(userContext);
  const [teachers, setTeachers] = useState<UserToShow[]>()
  const [students, setStudents] = useState<UserToShow[]>([])
  const {id} = useParams<{id: string}>()


  useEffect(() => {
    postgresqlDatabase
      .get(`/user/getTeachersByGroup/${id}`)
      .then((response) => {
        const teachers: UserToShow[] = response.data
        setTeachers(teachers)
      })
      .catch((e) => console.log(e))

    postgresqlDatabase
      .get(`/user/getStudentsByGroup/${id}`)
      .then((response) => {
        const students: UserToShow[] = response.data
        setStudents(students)
      })
      .catch((e) => console.log(e))
  }, [id])

  return(
    <dl>
      <dt>Nauczyciele</dt>
        <dd>
          <ul>{teachers?.map((teacher) =>((teacher.id !== +userState.userId) ? <GroupUsersSettingsListElement groupId={id} isStudent={false} userToShow={teacher} key={teacher.id}/> : ""))}</ul>
        </dd>
      <dt>Studenci</dt>
        <dd>
          <ul>{students?.map((student) =>((student.id !== +userState.userId) ? <GroupUsersSettingsListElement groupId={id} isStudent={true} userToShow={student} key={student.id}/> : ""))}</ul>
        </dd>
    </dl>
  )
}