import GroupUsersSettingsListElement from "./GroupUsersSettingsListElement"
import React, { useEffect, useState } from "react"
import {
  getStudentsByGroupPostgresService,
  getTeachersByGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { UserInterface } from "../../types/UserInterface"
import { selectGroup } from "../../redux/groupSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"

export default function GroupUsersSettings() {
  const [teachers, setTeachers] = useState<UserInterface[]>()
  const [students, setStudents] = useState<UserInterface[]>([])
  const group= useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)

  useEffect(() => {
    getTeachersByGroupPostgresService(group?.id as unknown as string)
      .then((response) => {
        const teachers: UserInterface[] = response.data
        setTeachers(teachers)
      })
      .catch((e) => console.log(e))

    getStudentsByGroupPostgresService(group?.id as unknown as string)
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
        <ul>{teachers?.map((teacher) => ((teacher.id !== userState?.id as number) ?
          <GroupUsersSettingsListElement makeTeacher={makeTeacher} deleteUser={deleteTeacherFromList}
                                         groupId={group?.id as unknown as string} isStudent={false} userToShow={teacher}
                                         key={teacher.id}/> : ""))}</ul>
      </dd>
      <dt className='font-semibold'>Studenci</dt>
      <dd>
        <ul>{students?.map((student) => ((student.id !== userState?.id as number) ?
          <GroupUsersSettingsListElement makeTeacher={makeTeacher} deleteUser={deleteStudentFromList}
                                         groupId={group?.id as unknown as string} isStudent={true} userToShow={student}
                                         key={student.id}/> : ""))}</ul>
      </dd>
    </dl>
  )
}