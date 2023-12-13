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
      <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden '>
    <div className='relative h-[420px]'>
      {role === "Teacher" && (
        <Link to={`/group/${group?.id}/assignments/add`}>
          <button
            className='absolute mb-4 right-[7.5%] top-[30px] bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none'>Nowe
            zadanie +
          </button>
        </Link>
      )}
      <ul className="flex flex-col box-content overflow-y-scroll mb-4">
        {assignments.map((assignment) => (
          <li key={assignment.id} className='flex inline-block '>
            <AssignmentListElement assignment={assignment} idGroup={group?.id as unknown as string}/>{" "}
          </li>
        ))}
      </ul>
    </div>
      </div>
  )
}

export default AssignmentsGroupDisplayedPage
