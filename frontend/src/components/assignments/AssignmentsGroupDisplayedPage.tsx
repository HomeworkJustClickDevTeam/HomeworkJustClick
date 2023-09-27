import { Link } from "react-router-dom"
import { useEffect, useState } from "react"
import { getAssignmentsByGroupPostgresService } from "../../services/postgresDatabaseServices"
import AssignmentListElement from "./AssignmentListElement"
import { AxiosError } from "axios"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { selectRole } from "../../redux/roleSlice"
import { selectGroup } from "../../redux/groupSlice"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"

function AssignmentsGroupDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const role = useAppSelector(selectRole)
  const group= useAppSelector(selectGroup)
  const dispatch = useAppDispatch()

  useEffect(() => {
    dispatch(setIsLoading(true))
    getAssignmentsByGroupPostgresService(group?.id as unknown as string).then((response) => {
      const assignmentFromServer = response.data as AssignmentInterface[]
      if (role === "Teacher") {
        setAssignments(assignmentFromServer)
      } else {
        setAssignments(
          assignmentFromServer.filter((assignment) => assignment.visible)
        )
      }
    })
      .catch((error: AxiosError) => console.log(error))
    dispatch(setIsLoading(false))
  }, [])

  return (
    <div className='relative h-[420px]'>
      {role === "Teacher" && (
        <Link to={`/group/${group?.id}/assignments/add`}>
          <button
            className='absolute right-[7.5%] bottom-0 bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none'>Nowe
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
