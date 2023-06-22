import { useEffect, useState } from "react"
import { GroupProp, UserToShow } from "../../../../types/types"
import postgresqlDatabase from "../../../../services/postgresDatabase"
import UserItem from "./UserItem/UserItem"

function TeacherDisplayer({ id }: GroupProp) {
  const [teachers, setTeachers] = useState<UserToShow[]>()

  useEffect(() => {
    postgresqlDatabase
      .get("/user/getTeachersByGroup/" + id)
      .then((response) => {
        const teacher: UserToShow[] = response.data
        setTeachers(teacher)
      })
      .catch((e) => console.log(e))
  }, [])
  return (
    <div className='flex inline-block mt-3'>
      <h1 className='font-bold text-2xl align-text-bottom mr-3'>Teachers: </h1>
      <ul className='flex inline-block gap-2 font-semibold text-lg'>
        {teachers?.map((teacher) => (
            <UserItem isTeacher={true} userToShow={teacher} key={teacher.id}/>
        ))}
      </ul>
    </div>
  )
}
export default TeacherDisplayer
