import { useParams } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../services/default-request-database"
import { Assigment } from "../../types/types"
import { parseISO } from "date-fns"

import GroupRoleContext from "../../GroupRoleContext"
import ModifyAssigment from "./ModifyAssigment"

import AddSolution from "../solution/AddSolution"
import Loading from "../animations/Loading"

function AssigmentSpec() {
  const { idAssigment } = useParams()
  const { role } = useContext(GroupRoleContext)
  const [assignment, setAssignment] = useState<Assigment>({
    completionDatetime: new Date(),
    id: 0,
    taskDescription: "",
    title: "",
    visible: false,
    max_points: 0,
    groupId: 0,
  })
  const [isLoading, setIsLoading] = useState<boolean>(false)
  useEffect(() => {
    postgresqlDatabase
      .get(`/assignment/${idAssigment}`)
      .then((response) => {
        const responseData = response.data
        const parsedDate = parseISO(responseData.completionDatetime)
        setAssignment({ ...responseData, completionDatetime: parsedDate })
      })
      .catch((error) => {
        console.log("Error fetching assignment:", error)
      })
    setIsLoading(false)
  }, [])
  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      {role === "Teacher" ? (
        <ModifyAssigment assignment={assignment} />
      ) : (
        <AddSolution assignment={assignment} />
      )}
    </>
  )
}
export default AssigmentSpec
