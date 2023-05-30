import { GroupProp } from "../../../types/types"
import TeacherDisplayer from "./type/TeacherDisplayer"
import StudentsDisplayer from "./type/StudentsDisplayer"

function UsersDisplayer({ id }: GroupProp) {
  return (
    <>
      <TeacherDisplayer id={id} />
      <StudentsDisplayer id={id} />
    </>
  )
}
export default UsersDisplayer
