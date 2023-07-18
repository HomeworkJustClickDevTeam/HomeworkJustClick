import GroupTeacherDisplayer from "./GroupTeacherDisplayer"
import GroupStudentsDisplayer from "./GroupStudentsDisplayer"

function GroupUsersDisplayer({ groupId }: {groupId: string}) {
  return (
    <div className='flex flex-col ml-[7.5%] gap-5 '>
      <GroupTeacherDisplayer groupId={groupId} />
      <GroupStudentsDisplayer groupId={groupId} />
    </div>
  )
}
export default GroupUsersDisplayer
