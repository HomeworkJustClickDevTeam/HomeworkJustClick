import {UserItemToDisplay} from "../../../../../types/types";
import {useContext} from "react";
import GroupRoleContext from "../../../../../GroupRoleContext";


function UserItem({userToShow, isTeacher}:UserItemToDisplay) {
  const {role} = useContext(GroupRoleContext)

  if(!isTeacher && role==="teacher") {//TO CHANGE
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