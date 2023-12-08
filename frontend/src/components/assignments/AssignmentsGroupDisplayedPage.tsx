import { Link } from "react-router-dom"
import AssignmentListElement from "./AssignmentListElement"
import { selectRole } from "../../redux/roleSlice"
import { selectGroup } from "../../redux/groupSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetAssignmentsByGroup } from "../customHooks/useGetAssignmentsByGroup"

function AssignmentsGroupDisplayedPage() {
  const role = useAppSelector(selectRole)
  const group= useAppSelector(selectGroup)
  const assignments = useGetAssignmentsByGroup(group?.id)

  return (
    <div className='relative h-[420px]'>
      {role === "Teacher" && (
        <Link to={`/group/${group?.id}/assignments/add`}>
          <button
            className='absolute mb-4 right-[7.5%] bottom-0 bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none'>Nowe
            zadanie +
          </button>
        </Link>
      )}
      <ul className="flex flex-col ">
        {assignments.map((assignment) => (
          <li key={assignment.id} className='flex inline-block '>
            <AssignmentListElement assignment={assignment} idGroup={group?.id as unknown as string}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsGroupDisplayedPage
