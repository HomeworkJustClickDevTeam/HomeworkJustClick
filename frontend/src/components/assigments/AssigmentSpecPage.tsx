import {useLocation, useParams} from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import postgresqlDatabase, {
  getAssignmentPostgresService,
  getCheckedSolutionByUserAssignmentGroupPostgresService,
  getUncheckedSolutionByUserAssignmentGroupPostgresService
} from "../../services/postgresDatabase"
import { parseISO } from "date-fns"

import GroupRoleContext from "../../contexts/GroupRoleContext"
import AssigmentModify from "./AssigmentModify"

import AddSolution from "../solution/AddSolution"
import Loading from "../animations/Loading"
import userContext from "../../contexts/UserContext"
import { AxiosError } from "axios"

import CheckedSolution from "../solution/CheckedSolution"
import UncheckedSolution from "../solution/UncheckedSolution"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {SolutionInterface} from "../../types/SolutionInterface";

function AssigmentSpecPage() {
  const { idAssigment, idGroup } = useParams()
  const location = useLocation()
  const optionalUserId:string|null = location.state
  const { userState } = useContext(userContext)
  const { role } = useContext(GroupRoleContext)
  const [isSolutionChecked, setIsSolutionChecked] = useState<boolean | undefined>(undefined)
  const [solution, setSolution] = useState<SolutionInterface | undefined>(undefined)
  const [assignment, setAssignment] = useState<AssignmentInterface | undefined>(undefined)
  let userId: null|string = null
  if(optionalUserId !== null){
    userId = optionalUserId
  }
  else{
    userId = userState.userId
  }
  useEffect(() => {
    getUncheckedSolutionByUserAssignmentGroupPostgresService(userState.userId, idAssigment, idGroup)
      .then((response) => {
        if (response.data.id !== null) {
          setIsSolutionChecked(false)
          setSolution(response.data)
        }
      })
      .catch((error: AxiosError) => {
        if(error.response?.status === 404){
          getCheckedSolutionByUserAssignmentGroupPostgresService(userState.userId, idAssigment, idGroup)
            .then((response) => {
              if (response.data.id !== null) {
                setIsSolutionChecked(true)
                setSolution(response.data)
              }
            })
            .catch((error: AxiosError) => console.log(error))
        }
        else{console.log(error)}
      })
      .finally(() => {
        getAssignmentPostgresService(idAssigment)
          .then((response) => {
            const responseData = response.data
            const parsedDate = parseISO(responseData.completionDatetime)
            setAssignment({
              ...responseData,
              completionDatetime: parsedDate,
            })
          })
          .catch((error) => {
            console.log("Error fetching assignment:", error)
          })
      })
    }, [])
  if(assignment === undefined){
    return(<Loading/>)
  }
  return (
    <div >
      {((role === "Teacher") && (optionalUserId === null)) ? (
        <AssigmentModify assignment={assignment} setAssigment={setAssignment} />
      ) : ((solution === undefined) && (optionalUserId === null)) ? (
        <AddSolution assignment={assignment} />
      ) : isSolutionChecked ? (

        <CheckedSolution assignment={assignment} solution={solution as SolutionInterface} />
      ) : (
        <UncheckedSolution assignment={assignment} solution={solution as SolutionInterface} />
      )}
    </div>
  )
}
export default AssigmentSpecPage
