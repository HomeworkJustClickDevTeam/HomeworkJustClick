import { GroupProp } from "../../types/types"
import GroupTeacherDisplayer from "./GroupTeacherDisplayer"
import GroupStudentsDisplayer from "./GroupStudentsDisplayer"

function GroupUsersDisplayer({ id }: {id: string}) {
  return (
    <div className='flex flex-col ml-[7.5%] gap-5 '>
      <GroupTeacherDisplayer id={id} />
      <GroupStudentsDisplayer id={id} />
    </div>
  )
}
export default GroupUsersDisplayer
