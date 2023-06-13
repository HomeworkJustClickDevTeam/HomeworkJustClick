import React, {useState} from "react";
import {UserToShow} from "../../../types/types";
import {tr} from "date-fns/locale";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {current} from "immer";
import {Axios, AxiosError} from "axios";

export default function GroupUsersSettingsListElement(props:{userToShow:UserToShow, isStudent:boolean, groupId: string | undefined}){
  const [open, setOpen] = useState(false);

  const handleUserDeletion = async () =>{
    props.isStudent ?
      (await postgresqlDatabase
        .post(`/group/deleteStudent/${props.userToShow.id}/${props.groupId}`)
        .catch((error:AxiosError) => console.log(error)))
      : (await postgresqlDatabase
          .post(`/group/deleteTeacher/${props.userToShow.id}/${props.groupId}`)
          .catch((error:AxiosError) => console.log(error)))
  }

  const handlePermissionsChange = async () =>{
    props.isStudent ?
      (await postgresqlDatabase
        .post(`/group/addTeacher/${props.userToShow.id}/${props.groupId}`)
        .catch((error:AxiosError) => console.log(error)))
      : (Promise<void>)
  }
  const threeDotsSettingsButtons = () =>{
    return(
      <ul>
        <li><button onClick={handleUserDeletion}>usuń z grupy</button></li>
        {props.isStudent ? <li><button onClick={handlePermissionsChange}>zmien na nauczyciela</button></li> : ""}
      </ul>
    )
  }
  return(
    <li>{props.userToShow.firstname} {props.userToShow.lastname}
      {!props.isStudent ? (<><button onClick={()=>setOpen(!open)}>trzy kropki</button>{open && threeDotsSettingsButtons}</>) : ("")}
    </li>
  )
}