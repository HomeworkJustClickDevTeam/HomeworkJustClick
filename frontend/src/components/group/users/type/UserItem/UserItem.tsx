import {UserItemToDisplay} from "../../../../../types/types";


function UserItem({userToShow, isTeacher}:UserItemToDisplay) {

  if(!isTeacher && true) {//TO CHANGE
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