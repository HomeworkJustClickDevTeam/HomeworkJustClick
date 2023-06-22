import { GroupProp } from "../../../types/types"
import TeacherDisplayer from "./type/TeacherDisplayer"
import StudentsDisplayer from "./type/StudentsDisplayer"

function UsersDisplayer({ id }: GroupProp) {
  return (
    <div className='flex flex-col gap-5 ml-28 mt-4'>
      <TeacherDisplayer id={id} />
      <StudentsDisplayer id={id} />
    </div>
  )
}
export default UsersDisplayer
