import { GroupProp } from "../../../types/types"
import TeacherDisplayer from "./type/TeacherDisplayer"
import StudentsDisplayer from "./type/StudentsDisplayer"

function UsersDisplayer({ id }: GroupProp) {
  return (
    <div className='flex flex-col ml-[7.5%] gap-5 '>
      <TeacherDisplayer id={id} />
      <StudentsDisplayer id={id} />
    </div>
  )
}
export default UsersDisplayer
