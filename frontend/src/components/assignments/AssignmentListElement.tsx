import {Link} from "react-router-dom"
import {format} from "date-fns"
import {AssignmentPropsInterface} from "../../types/AssignmentPropsInterface";

interface AssignmentItemPropsInterface extends AssignmentPropsInterface {
  idGroup: string
  optionalUserId?: string
}

function AssignmentListElement({assignment, idGroup, optionalUserId}: AssignmentItemPropsInterface) {
  const completionDatetime = new Date(assignment.completionDatetime)
  return (
    <>
      <Link to={`/group/${idGroup}/assignment/${assignment.id}`} state={optionalUserId}
            className="flex ml-[7.5%] mt-4 border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center justify-between">
        <div className="flex-col ml-10 ">
          <div>{assignment.title}</div>
          <div className="text-border_gray">{format(completionDatetime, "dd.MM.yyyy, HH:mm")}</div>
        </div>
        <div className="mr-10 font-semibold text-[28px]">{"/" + assignment.max_points}</div>
      </Link>
    </>
  )
}

export default AssignmentListElement
