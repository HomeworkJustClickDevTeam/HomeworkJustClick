import { Link, useParams } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import { Assigment } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"
import AssigmentItem from "./assigmentItem/AssigmentItem"
import Loading from "../../animations/Loading"
import GroupRoleContext from "../../../GroupRoleContext"
import {Simulate} from "react-dom/test-utils";
import error = Simulate.error;
import {AxiosError} from "axios";

function AssignmentsGroupDisplayed() {
  const [assignments, setAssignments] = useState<Assigment[]>([])
  const { id = "" } = useParams<string>()
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const { role } = useContext(GroupRoleContext)

  useEffect(() => {
    postgresqlDatabase
      .get("/assignments/byGroupId/" + id).then((response) => {
        const assigmentFromServer = response.data as Assigment[]
        console.log(response.data)
        if (role === "Teacher") {
          setAssignments(assigmentFromServer)
        } else {
          setAssignments(
            assigmentFromServer.filter((assigment) => assigment.visible)
          )
        }
      })
      .catch((error:AxiosError) => console.log(error))
    setIsLoading(false)
  }, [])

  if (isLoading) {
    return <Loading />
  }
  return (
    <div className='relative h-[420px]'>
      {role == "Teacher" && (
        <Link to={`/group/${id}/assignments/add`}>
          <button className='absolute right-0 bottom-0 bg-main_blue text-white mr-14 px-8 py-2 rounded-md text-lg'>Nowe zadanie +</button>
        </Link>
      )}
      <ul className="flex flex-col">
        {assignments.map((assigment) => (
          <li key={assigment.id} className='flex inline-block '>
            <AssigmentItem assignment={assigment} idGroup={id} />{" "}
          </li>
        ))}
      </ul>
    </div>
  )
}
export default AssignmentsGroupDisplayed
