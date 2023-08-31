import {useEffect, useState} from "react"
import {getStudentsByGroupPostgresService} from "../../services/postgresDatabaseServices"
import GroupUserListElement from "./GroupUserListElement"
import {UserInterface} from "../../types/UserInterface";

function GroupStudentsDisplayer({groupId}: { groupId: string }) {
  const [students, setStudents] = useState<UserInterface[]>()
  useEffect(() => {
    getStudentsByGroupPostgresService(groupId)
      .then((response) => {
        const student: UserInterface[] = response.data
        setStudents(student)
      })
  }, [])
  return (
    <div className='flex flex-col'>
      <h1 className='text-lg font-semibold mb-2'>Studenci:</h1>
      <ul className='flex flex-col gap-1'>
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
