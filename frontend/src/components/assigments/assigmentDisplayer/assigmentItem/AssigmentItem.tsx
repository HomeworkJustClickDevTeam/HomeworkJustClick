import { AssigmentItemProps } from "../../../../types/types"
import { Link } from "react-router-dom"

function AssigmentItem({ assignment, idGroup }: AssigmentItemProps) {
  return (
    <>
      <Link to={`/group/${idGroup}/assigment/${assignment.id}`}>
        {assignment.title} {assignment.completionDatetime.toLocaleString()}
      </Link>
    </>
  )
}
export default AssigmentItem
