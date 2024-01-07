import React, { useState } from "react"
import {
  addStudentToGroupPostgresService,
  addTeacherToGroupPostgresService,
  deleteStudentInGroupPostgresService,
  deleteTeacherInGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios"
import { UserInterface } from "../../types/UserInterface"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import {toast} from "react-toastify";

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
        .catch((error: AxiosError) => {
          toast.error("Coś poszło nie tak przy usuwaniu studenta z grupy")
          console.log(error)
        })
        .then(() => {
          toast.success("Udało się usunąć studenta z grupy")
          props.deleteUser(props.userToShow)
        }))
      : (await deleteTeacherInGroupPostgresService(props.userToShow.id.toString(), props.groupId)
        .catch((error: AxiosError) => {
          toast.error("Coś poszło nie tak przy usuwaniu prowadzącego z grupy")
          console.log(error)
        })
        .then(() => {
          toast.success("Udało się usunąć prowadzącego z grupy")
          props.deleteUser(props.userToShow)
        }))
  }

  const handlePermissionsChange = async () => {
    await deleteStudentInGroupPostgresService(props.userToShow.id.toString(), props.groupId)
      .catch((error: AxiosError) => {
        toast.error("Coś poszło nie tak przy zmianie użytkownika na nauczyciela")
        console.log(error)
      })
      .then(() => {
        addTeacherToGroupPostgresService(props.userToShow.id.toString(), props.groupId)
          .catch((error: AxiosError) => {
            toast.error("Coś poszło nie tak przy zmianie użytkownika na nauczyciela")
            addStudentToGroupPostgresService(props.userToShow.id.toString(), props.groupId)
              .catch(e=>console.error(e))
            console.log(error)
          })
          .then(() => {
            toast.success("Udało się zmienić studenta na prowadzącego")
            props.makeTeacher(props.userToShow)
          })
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