import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"

interface GroupUserListElementProps {
  userToShow: { firstname: string, lastname: string, id: number }
  isTeacher?: boolean
}

function GroupUserListElement({userToShow, isTeacher}: GroupUserListElementProps) {
  const role = useAppSelector(selectRole)

  if (!isTeacher && role === "Teacher") {
    return (
      <li className="px-1 after:content-[','] last:after:content-['']">
        <a href={"userProfileInGroup/" + userToShow.id}>
          {userToShow.firstname} {userToShow.lastname}
        </a>
      </li>
    )
  } else {
    return (
      <li className="align-text-bottom mt-1 px-1 after:content-[','] last:after:content-['']">
        {userToShow.firstname} {userToShow.lastname}
      </li>
    )
  }
}

export default GroupUserListElement