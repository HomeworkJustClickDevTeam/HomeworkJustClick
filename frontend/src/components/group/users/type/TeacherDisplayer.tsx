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
    <div>
      <h1>Teachers:</h1>
      <ul>
        {teachers?.map((teacher) => (
          <li key={teacher.id}>
            <UserItem userToShow={teacher} />
          </li>
        ))}
      </ul>
    </div>
  )
}
export default TeacherDisplayer
