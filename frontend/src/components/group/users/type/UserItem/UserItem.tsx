import {UserItemToDisplay} from "../../../../../types/types";

function UserItem({userToShow}:UserItemToDisplay) {

    return(
        <>
            <p>{userToShow.firstname} {userToShow.lastname}</p>
        </>
    )

}
export default UserItem