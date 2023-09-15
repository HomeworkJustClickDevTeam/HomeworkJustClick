import { Link } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import { getAssignmentsByGroupPostgresService } from "../../services/postgresDatabaseServices"
import AssignmentListElement from "./AssignmentListElement"
import Loading from "../animations/Loading"
import { AxiosError } from "axios"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { selectRole } from "../../redux/roleSlice"
import { useSelector } from "react-redux"
import { selectGroup } from "../../redux/groupSlice"

function AssignmentsGroupDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const role = useSelector(selectRole)
  const group= useSelector(selectGroup)

  useEffect(() => {
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
    setIsLoading(false)
  }, [])

  if (isLoading) {
    return <Loading/>
  }
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