import {UserItemToDisplay} from "../../../../../types/types";
import {useContext, useEffect, useState} from "react";
import postgresqlDatabase from "../../../../../services/postgresDatabase";
import userContext from "../../../../../UserContext";
import {useParams} from "react-router-dom";
import Loading from "../../../../animations/Loading";
import groupRoleContext from "../../../../../GroupRoleContext";

function UserItem({userToShow, isTeacher}:UserItemToDisplay) {
  const {userProfileId,id} = useParams()
  const { role } = useContext(groupRoleContext)

  if(!isTeacher && role==="Teacher") {
    return (
      <li>
        <a href={"userProfileInGroup/" + userToShow.id}>
          {userToShow.firstname} {userToShow.lastname}
        </a>
      </li>
    )
  }
  else {
    return (
      <li>
        {userToShow.firstname} {userToShow.lastname}
      </li>
    )
  }
}
export default UserItem