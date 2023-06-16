import {useLocation, useParams} from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import { Assigment, Solution } from "../../types/types"
import { parseISO } from "date-fns"

import GroupRoleContext from "../../GroupRoleContext"
import ModifyAssigment from "./ModifyAssigment"

import AddSolution from "../solution/AddSolution"
import Loading from "../animations/Loading"
import userContext from "../../UserContext"
import { AxiosError } from "axios"

import CheckedSolution from "../solution/CheckedSolution"
import UncheckedSolution from "../solution/UncheckedSolution"

function AssigmentSpec() {
  const { idAssigment, id } = useParams()
  const location = useLocation()
  const optionalUserId:string|null = location.state
  const { userState } = useContext(userContext)
  const { role } = useContext(GroupRoleContext)
  const [isSolutionChecked, setIsSolutionChecked] = useState<boolean | undefined>(undefined)
  const [solution, setSolution] = useState<Solution | undefined>(undefined)
  const [assignment, setAssignment] = useState<Assigment>({
    completionDatetime: new Date(),
    id: 0,
    taskDescription: "",
    title: "",
    visible: false,
    max_points: 0,
    groupId: 0,
  })
  useEffect(() => {
    let userId = userState.userId
    if(optionalUserId !== null){
      userId = optionalUserId
    }
    postgresqlDatabase
      .get(
        `/solution/getUncheckedSolutionByUserAssignmentGroup/${userId}/${idAssigment}/${id}`
      )
      .then((response) => {
        if (response.data.id !== null) {
          setIsSolutionChecked(false)
          setSolution(response.data)
        }
      })
      .catch((error: AxiosError) => {
        if(error.response?.status === 404){
          postgresqlDatabase
            .get(
              `/solution/getCheckedSolutionByUserAssignmentGroup/${userId}/${idAssigment}/${id}`
            )
            .then((response) => {
              if (response.data.id !== null) {
                setIsSolutionChecked(true)
                setSolution(response.data)
                return void 0
              }
            })
            .catch((error: AxiosError) => console.log(error))
        }
        else{console.log(error)}
      })
      .finally(() => {
        postgresqlDatabase
          .get(`/assignment/${idAssigment}`)
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
  if (assignment.id === 0) {
    return <Loading />
  }
  console.log(solution)
  return (
    <>
      {role === "Teacher" ? (
        <ModifyAssigment assignment={assignment} />
      ) : typeof solution === undefined ? (
        <AddSolution assignment={assignment} />
      ) : isSolutionChecked ? (
        <CheckedSolution assignment={assignment} solution={solution} />
      ) : (
        <UncheckedSolution assignment={assignment} solution={solution} />
      )}
    </>
  )
}
export default AssigmentSpec
