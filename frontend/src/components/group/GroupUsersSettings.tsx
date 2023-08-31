import GroupUsersSettingsListElement from "./GroupUsersSettingsListElement";
import React, {useContext, useEffect, useState} from "react";
import {
  getUserGetStudentsByGroupPostgresService,
  getUserGetTeachersByGroupPostgresService
} from "../../services/postgresDatabaseServices";
import {useParams} from "react-router-dom";
import {UserInterface} from "../../types/UserInterface";
import {getUser} from "../../services/otherServices";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

export default function GroupUsersSettings() {
  const [teachers, setTeachers] = useState<UserInterface[]>()
  const [students, setStudents] = useState<UserInterface[]>([])
  const {idGroup} = useParams<{ idGroup: string }>()
  const {applicationState} = useContext(ApplicationStateContext)


  useEffect(() => {
    getUserGetTeachersByGroupPostgresService(idGroup as string)
      .then((response) => {
        const teachers: UserInterface[] = response.data
        setTeachers(teachers)
      })
      .catch((e) => console.log(e))

    getUserGetStudentsByGroupPostgresService(idGroup as string)
      .then((response) => {
        const students: UserInterface[] = response.data
        setStudents(students)
      })
      .catch((e) => console.log(e))
  }, [])

  const deleteTeacherFromList = (teacher: UserInterface) => {
    setTeachers((prevState) => (
      prevState?.filter(teacherFromList => {
        return teacherFromList.id !== teacher.id
      })
    ))
  }

  const deleteStudentFromList = (student: UserInterface) => {
    setStudents((prevState) => (
      prevState?.filter(studentFromList => {
        return studentFromList.id !== student.id
      })
    ))
  }

  const makeTeacher = (student: UserInterface) => {
    deleteStudentFromList(student)
    setTeachers((prevState) => {
      return ([...(prevState || []), student]);
    })
  }

  return (
    <dl>
      <dt className='font-semibold'>Nauczyciele</dt>
      <dd>
        <ul>{teachers?.map((teacher) => ((teacher.id !== applicationState?.userState?.id as number) ?
          <GroupUsersSettingsListElement makeTeacher={makeTeacher} deleteUser={deleteTeacherFromList}
                                         groupId={idGroup as string} isStudent={false} userToShow={teacher}
                                         key={teacher.id}/> : ""))}</ul>
      </dd>
      <dt className='font-semibold'>Studenci</dt>
      <dd>
        <ul>{students?.map((student) => ((student.id !== applicationState?.userState?.id as number) ?
          <GroupUsersSettingsListElement makeTeacher={makeTeacher} deleteUser={deleteStudentFromList}
                                         groupId={idGroup as string} isStudent={true} userToShow={student}
                                         key={student.id}/> : ""))}</ul>
      </dd>
    </dl>
  )
}