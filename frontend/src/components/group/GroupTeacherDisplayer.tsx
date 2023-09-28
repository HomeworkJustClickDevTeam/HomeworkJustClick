import GroupUserListElement from "./GroupUserListElement"
import { useGetUsersByGroup } from "../customHooks/useGetUsersByGroup"

function GroupTeacherDisplayer({groupId}: { groupId: string }) {
  const {users:teachers} = useGetUsersByGroup(groupId as unknown as number, "teachers")
  return (
    <div className='flex inline-block mt-4 border-b-2 border-b-light_gray w-fit'>
      <h1 className='font-bold text-2xl align-text-bottom mr-3'>Prowadzący: </h1>
      <ul className='flex inline-block font-semibold text-lg'>
        {teachers?.map((teacher) => (
          <GroupUserListElement isTeacher={true} userToShow={{
            firstname: teacher.firstname as string,
            lastname: teacher.lastname as string,
            id: teacher.id as number
          }} key={teacher.id}/>
        ))}
      </ul>
    </div>
  )
}

export default GroupTeacherDisplayer
