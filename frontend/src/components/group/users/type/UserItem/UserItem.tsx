import {UserItemToDisplay} from "../../../../../types/types";

function UserItem({userToShow}:UserItemToDisplay) {

    return(
        <li>
          <a href={"userProfileInGroup/" + userToShow.id}>
            {userToShow.firstname} {userToShow.lastname}
          </a>
        </li>
    )

}
export default UserItem