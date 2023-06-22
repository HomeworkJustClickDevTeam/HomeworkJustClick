import { useEffect, useState } from "react"
import { GroupProp, UserToShow } from "../../../../types/types"
import postgresqlDatabase from "../../../../services/postgresDatabase"
import UserItem from "./UserItem/UserItem"

function StudentsDisplayer({ id }: GroupProp) {
  const [students, setStudents] = useState<UserToShow[]>()
  useEffect(() => {
    postgresqlDatabase
      .get("/user/getStudentsByGroup/" + id)
      .then((response) => {
        const student: UserToShow[] = response.data
        setStudents(student)
      })
  }, [])
  return (
    <div className='flex flex-col'>
      <h1 className='text-lg font-semibold'>Students:</h1>
      <ul>
        {students?.map((student) => (
            <UserItem isTeacher={false} userToShow={student} key={student.id}/>
        ))}
      </ul>
    </div>
  )
}
export default StudentsDisplayer
