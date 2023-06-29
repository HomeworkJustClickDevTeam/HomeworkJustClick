import { useEffect, useState } from "react"
import { GroupProp} from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import GroupUserListElement from "./GroupUserListElement"
import {UserInterface} from "../../types/UserInterface";

function GroupTeacherDisplayer({ id }: GroupProp) {
  const [teachers, setTeachers] = useState<UserInterface[]>()

  useEffect(() => {
    postgresqlDatabase
      .get("/user/getTeachersByGroup/" + id)
      .then((response) => {
        const teacher: UserInterface[] = response.data
        setTeachers(teacher)
      })
      .catch((e) => console.log(e))
  }, [])
  return (
    <div className='flex inline-block mt-4 border-b-2 border-b-light_gray w-fit'>
      <h1 className='font-bold text-2xl align-text-bottom mr-3'>Prowadzący: </h1>
      <ul className='flex inline-block font-semibold text-lg'>
        {teachers?.map((teacher) => (
            <GroupUserListElement isTeacher={true} userToShow={{firstname: teacher.firstname as string, lastname: teacher.lastname as string, id: teacher.id as number}} key={teacher.id}/>
        ))}
      </ul>
    </div>
  )
}
export default GroupTeacherDisplayer
