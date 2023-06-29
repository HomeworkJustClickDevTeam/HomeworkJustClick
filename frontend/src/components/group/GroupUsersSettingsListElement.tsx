import React, {useContext, useState} from "react";
import postgresqlDatabase from "../../services/postgresDatabase";
import {AxiosError} from "axios";
import GroupRoleContext from "../../GroupRoleContext";
import {UserInterface} from "../../types/UserInterface";
interface GroupUsersSettingsListElementProps{
  makeTeacher(arg:UserInterface): void,
  deleteUser(arg: UserInterface): void,
  userToShow:UserInterface,
  isStudent:boolean,
  groupId: string}
export default function GroupUsersSettingsListElement(props:GroupUsersSettingsListElementProps){
  const [open, setOpen] = useState(false)
  const {role} = useContext(GroupRoleContext)
  const handleUserDeletion = async () =>{
    props.isStudent ?
      (await postgresqlDatabase
        .delete(`/group/deleteStudent/${props.userToShow.id}/${props.groupId}`)
        .catch((error:AxiosError) => console.log(error))
        .then(() => props.deleteUser(props.userToShow)))
      : (await postgresqlDatabase
          .delete(`/group/deleteTeacher/${props.userToShow.id}/${props.groupId}`)
          .catch((error:AxiosError) => console.log(error))
          .then(() => props.deleteUser(props.userToShow)))
  }

  const handlePermissionsChange = async () =>{
    await postgresqlDatabase
      .delete(`/group/deleteStudent/${props.userToShow.id}/${props.groupId}`)
      .catch((error:AxiosError) => console.log(error))
      .then(()=>{
        postgresqlDatabase
          .post(`/group/addTeacher/${props.userToShow.id}/${props.groupId}`)
          .catch((error:AxiosError) => console.log(error))
          .then(()=> props.makeTeacher(props.userToShow))
      })

  }
  const ThreeDotsSettingsButtons = () =>{
    return(
      <ul className='ml-2 text-left'>
        <li><button onClick={handleUserDeletion} className='text-scarlet'>Usuń z grupy</button></li>
        {props.isStudent ? <li><button onClick={handlePermissionsChange}>Zmień na nauczyciela</button></li> : ""}
      </ul>
    )
  }
  return(
    <li className='flex inline-block text-center items-center'><>{props.userToShow.firstname} {props.userToShow.lastname}
      {role === "Teacher" ? (<button onClick={()=>setOpen(!open)} className='pb-1 pl-1 font-bold'> ... </button>) : ("")}{open && <ThreeDotsSettingsButtons/>}
    </></li>
  )
}