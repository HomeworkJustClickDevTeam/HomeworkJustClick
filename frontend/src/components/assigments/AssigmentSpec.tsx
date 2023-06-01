import { useParams } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import common_request from "../../services/default-request-database"
import { Assigment } from "../../types/types"
import { parseISO } from "date-fns"

import GroupRoleContext from "../../GroupRoleContext"
import ModifyAssigment from "./ModifyAssigment"

function AssigmentSpec() {
  const { idAssigment } = useParams()
  const { role } = useContext(GroupRoleContext)
  const [assigment, setAssigment] = useState<Assigment>({
    completionDatetime: new Date(),
    id: 0,
    taskDescription: "",
    title: "",
    visible: false,
    points: 0,
  })

  useEffect(() => {
    common_request
      .get(`/assignment/${idAssigment}`)
      .then((response) => {
        const responseData = response.data
        const parsedDate = parseISO(responseData.completionDatetime)
        setAssigment({ ...responseData, completionDatetime: parsedDate })
      })
      .catch((error) => {
        console.log("Error fetching assignment:", error)
      })
  }, [])
  return (
    <>
      {role === "Teacher" ? (
        <ModifyAssigment assignment={assigment} />
      ) : (
        <>User in progress</>
      )}
    </>
  )
}
export default AssigmentSpec

/*
<h1>{assigment.title}</h1>
<p>{assigment.taskDescription}</p>
<h2>{format(assigment.completionDatetime, "dd MMM yyyy, HH:mm")}</h2>
 */
