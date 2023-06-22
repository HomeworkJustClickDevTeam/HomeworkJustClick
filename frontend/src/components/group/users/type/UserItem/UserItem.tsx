import {UserItemToDisplay} from "../../../../../types/types";
import {useContext} from "react";
import GroupRoleContext from "../../../../../GroupRoleContext";


function UserItem({userToShow, isTeacher}:UserItemToDisplay) {
  const {role} = useContext(GroupRoleContext)

  if(!isTeacher && role==="Teacher") {
    return (
      <li className="px-1 after:content-[','] last:after:content-['']">
        <a href={"userProfileInGroup/" + userToShow.id}>
          {userToShow.firstname} {userToShow.lastname}
        </a>
      </li>
    )
  }
  else {
    return (
      <li className="align-text-bottom mt-1 px-1 after:content-[','] last:after:content-['']">
        {userToShow.firstname} {userToShow.lastname}
      </li>
    )
  }
}
export default UserItem