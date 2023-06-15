import { useParams } from "react-router-dom"
import { useContext, useEffect, useState } from "react"
import postgresqlDatabase from "../../services/postgresDatabase"
import {Assigment, Solution, userState} from "../../types/types"
import { parseISO } from "date-fns"

import GroupRoleContext from "../../GroupRoleContext"
import ModifyAssigment from "./ModifyAssigment"

import AddSolution from "../solution/AddSolution"
import Loading from "../animations/Loading";
import userContext from "../../UserContext";
import {AxiosError} from "axios";
import {tr} from "date-fns/locale";
import CheckedSolution from "../solution/CheckedSolution";
import UncheckedSolution from "../solution/UncheckedSolution";
import {type} from "os";

function AssigmentSpec() {
  const { idAssigment, id } = useParams()
  const {userState} = useContext(userContext)
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
  const [isLoading, setIsLoading] = useState<boolean>(false)
  useEffect(() => {
    postgresqlDatabase
      .get(`/solution/getUncheckedSolutionByUserAssignmentGroup/${userState.userId}/${idAssigment}/${id}`)
      .then((response) => {
        if(response.data.id !== null){
          setIsLoading(false)
          setIsSolutionChecked(false)
          setSolution(response.data)
          return void(0)
        }
      })
      .catch((error:AxiosError) => console.log(error))
      .finally(() => {
        postgresqlDatabase
          .get(`/solution/getCheckedSolutionByUserAssignmentGroup/${userState.userId}/${idAssigment}/${id}`)
          .then((response) => {
            if (response.data.id !== null) {
              setIsLoading(false)
              setIsSolutionChecked(true)
              setSolution(response.data)
              return void (0)
            }
          })
          .catch((error: AxiosError) => console.log(error))
          .finally(() => {
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
          })
      })
  }, [])
  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      {role === "Teacher" ? (
        <ModifyAssigment assignment={assignment} />
        ) : (
        (typeof solution === undefined) ? (
          <AddSolution assignment={assignment}/>
          ) : (
            isSolutionChecked ? (
            <CheckedSolution assignment={assignment} solution={solution}/>
            ) : (
              <UncheckedSolution assignment={assignment} solution={solution}/>
            )
          )
        )
      }
    </>
  )
}
export default AssigmentSpec
