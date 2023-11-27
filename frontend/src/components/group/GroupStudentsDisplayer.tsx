import GroupUserListElement from "./GroupUserListElement"
import { useGetUsersByGroup } from "../customHooks/useGetUsersByGroup"

function GroupStudentsDisplayer({groupId}: { groupId: string }) {
  const {users:students} = useGetUsersByGroup(groupId as unknown as number, "students")
  return (
    <div className='flex flex-col'>
      <h1 className='text-lg font-semibold mb-2'>Studenci:</h1>
      <ul className='flex flex-col gap-1'>
        {students?.map((student) => (
          <GroupUserListElement isTeacher={false} userToShow={{
            firstname: student.name as string,
            lastname: student.lastname as string,
            id: student.id as number
          }} key={student.id}/>
        ))}
      </ul>
    </div>
  )
}

export default GroupStudentsDisplayer
