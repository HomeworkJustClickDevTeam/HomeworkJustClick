import { GroupProp } from "../../types/types"
import TeacherDisplayer from "./users/type/TeacherDisplayer"
import StudentsDisplayer from "./users/type/StudentsDisplayer"

function UsersDisplayer({ id }: GroupProp) {
  return (
    <div className='flex flex-col ml-[7.5%] gap-5 '>
      <TeacherDisplayer id={id} />
      <StudentsDisplayer id={id} />
    </div>
  )
}
export default UsersDisplayer
