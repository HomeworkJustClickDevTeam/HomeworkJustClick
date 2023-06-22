import { AssigmentItemProps } from "../../../../types/types"
import { Link } from "react-router-dom"
import { format } from "date-fns"

function AssigmentItem({ assignment, idGroup, optionalUserId }: AssigmentItemProps) {
  const completionDatetime = new Date(assignment.completionDatetime)
  return (
      <>
          <Link to={`/group/${idGroup}/assigment/${assignment.id}`} state={optionalUserId} className="flex ml-[7.5%] mt-4 border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center justify-between">
              <div className="flex-col ml-10 ">
                  <div>{assignment.title}</div>
                  <div className="text-border_gray">{format(completionDatetime, "dd.MM.yyyy, HH:mm")}</div>
              </div>
              <div className="mr-10 font-semibold text-[28px]">{"/" + assignment.max_points}</div>
          </Link>
      </>
  )
}

export default AssigmentItem
