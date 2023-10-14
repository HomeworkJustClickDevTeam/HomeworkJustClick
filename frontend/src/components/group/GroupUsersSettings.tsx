import GroupUsersSettingsListElement from "./GroupUsersSettingsListElement"
import React from "react"
import { UserInterface } from "../../types/UserInterface"
import { selectGroup } from "../../redux/groupSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetUsersByGroup } from "../customHooks/useGetUsersByGroup"

export default function GroupUsersSettings() {
  const group= useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const {users: teachers, setUsers: setTeachers} = useGetUsersByGroup(group?.id, "teachers")
  const {users: students, setUsers: setStudents} = useGetUsersByGroup(group?.id, "students")


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