import GroupUserListElement from "./GroupUserListElement"
import { useGetUsersByGroup } from "../customHooks/useGetUsersByGroup"

function GroupStudentsDisplayer({groupId}: { groupId: string }) {
  const {users:students} = useGetUsersByGroup(groupId as unknown as number, "students")
  return (
    <div className='flex flex-col h-[calc(100vh-410px)] overflow-y-hidden'>
      <h1 className='text-lg font-semibold mb-2'>Studenci:</h1>
      <ul className='flex flex-col gap-1 box-content overflow-y-auto mb-4'>
        {students?.map((student) => (
          <GroupUserListElement isTeacher={false} userToShow={{
            firstname: student.firstname as string,
            lastname: student.lastname as string,
            id: student.id as number
          }} key={student.id}/>
        ))}
      </ul>
    </div>
  )
}

export default GroupStudentsDisplayer
