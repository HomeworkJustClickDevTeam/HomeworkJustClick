import { AssigmentItemProps } from "../../../../types/types"
import { Link } from "react-router-dom"
import { format } from "date-fns"

function AssigmentItem({ assignment, idGroup, optionalUserId }: AssigmentItemProps) {
  const completionDatetime = new Date(assignment.completionDatetime)
  return (
    <>
      <Link to={`/group/${idGroup}/assigment/${assignment.id}`} state={optionalUserId}>
        {assignment.title}
        {format(completionDatetime, "dd MM yyyy, HH:mm")}
      </Link>
    </>
  )
}

export default AssigmentItem
