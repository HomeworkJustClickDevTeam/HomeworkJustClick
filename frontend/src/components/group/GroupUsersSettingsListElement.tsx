import React, { useState } from "react"
import {
  addTeacherToGroupPostgresService,
  deleteStudentInGroupPostgresService,
  deleteTeacherInGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { UserInterface } from "../../types/UserInterface"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"

interface GroupUsersSettingsListElementProps {
  makeTeacher(arg: UserInterface): void,

  deleteUser(arg: UserInterface): void,

  userToShow: UserInterface,
  isStudent: boolean,
  groupId: string
}

export default function GroupUsersSettingsListElement(props: GroupUsersSettingsListElementProps) {
  const [open, setOpen] = useState(false)
  const role = useAppSelector(selectRole)
  const handleUserDeletion = async () => {
    props.isStudent ?
      (await deleteStudentInGroupPostgresService(props.userToShow.id.toString(), props.groupId)
        .catch((error: AxiosError) => console.log(error))
        .then(() => props.deleteUser(props.userToShow)))
      : (await deleteTeacherInGroupPostgresService(props.userToShow.id.toString(), props.groupId)
        .catch((error: AxiosError) => console.log(error))
        .then(() => props.deleteUser(props.userToShow)))
  }

  const handlePermissionsChange = async () => {
    await deleteStudentInGroupPostgresService(props.userToShow.id.toString(), props.groupId)
      .catch((error: AxiosError) => console.log(error))
      .then(() => {
        addTeacherToGroupPostgresService(props.userToShow.id.toString(), props.groupId)
          .catch((error: AxiosError) => console.log(error))
          .then(() => props.makeTeacher(props.userToShow))
      })

  }
  const ThreeDotsSettingsButtons = () => {
    return (
      <ul className='ml-2 text-left border border-border_gray rounded-md px-3'>
        <li>
          <button onClick={handleUserDeletion} className='text-scarlet'>Usuń z grupy</button>
        </li>
        {props.isStudent ? <li>
          <button onClick={handlePermissionsChange}>Zmień na nauczyciela</button>
        </li> : ""}
      </ul>
    )
  }
  return (
    <li
      className='flex inline-block text-center items-center'><>{props.userToShow.firstname} {props.userToShow.lastname}
      {role === "Teacher" ? (
        <button onClick={() => setOpen(!open)} className='pb-1 pl-1 font-bold'> ... </button>) : ("")}{open &&
            <ThreeDotsSettingsButtons/>}
    </>
    </li>
  )
}