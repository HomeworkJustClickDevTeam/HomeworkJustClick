import {Link, useParams} from "react-router-dom"
import {useContext, useEffect, useState} from "react"
import {getAssignmentsByGroupPostgresService} from "../../services/postgresDatabaseServices"
import AssigmentListElement from "./AssigmentListElement"
import Loading from "../animations/Loading"
import GroupRoleContext from "../../contexts/GroupRoleContext"
import {AxiosError} from "axios";
import {AssignmentInterface} from "../../types/AssignmentInterface";

function AssignmentsGroupDisplayedPage() {
  const [assignments, setAssignments] = useState<AssignmentInterface[]>([])
  const {idGroup = ""} = useParams<string>()
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const {role} = useContext(GroupRoleContext)

  useEffect(() => {
    getAssignmentsByGroupPostgresService(idGroup).then((response) => {
      const assigmentFromServer = response.data as AssignmentInterface[]
      if (role === "Teacher") {
        setAssignments(assigmentFromServer)
      } else {
        setAssignments(
          assigmentFromServer.filter((assigment) => assigment.visible)
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
        <Link to={`/group/${idGroup}/assignments/add`}>
          <button
            className='absolute right-[7.5%] bottom-0 bg-main_blue text-white px-8 py-2 rounded-md text-lg hover:bg-hover_blue hover:shadow-md active:shadow-none'>Nowe
            zadanie +
          </button>
        </Link>
      )}
      <ul className="flex flex-col ">
        {assignments.map((assigment) => (
          <li key={assigment.id} className='flex inline-block '>
            <AssigmentListElement assignment={assigment} idGroup={idGroup}/>{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}

export default AssignmentsGroupDisplayedPage
