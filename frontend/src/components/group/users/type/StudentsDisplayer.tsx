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
      <h1 className='text-lg font-semibold mb-2'>Studenci:</h1>
      <ul className='flex flex-col gap-1'>
        {students?.map((student) => (
            <UserItem isTeacher={false} userToShow={student} key={student.id}/>
        ))}
      </ul>
    </div>
  )
}
export default StudentsDisplayer
