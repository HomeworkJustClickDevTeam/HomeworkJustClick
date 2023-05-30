import { AssigmentItemProps } from "../../../../types/types"
import { Link } from "react-router-dom"

function AssigmentItem({ assigment, idGroup }: AssigmentItemProps) {
  return (
    <>
      <Link to={`/group/${idGroup}/assigment/${assigment.id}`}>
        {assigment.title} {assigment.completionDatetime.toLocaleString()}
      </Link>
    </>
  )
}
export default AssigmentItem
