import {useLocation, useNavigate, useParams} from "react-router-dom"
import React, {useContext, useEffect, useState} from "react"
import {
  getAssignmentPostgresService,
  getCheckedSolutionByUserAssignmentGroupPostgresService,
  getUncheckedSolutionByUserAssignmentGroupPostgresService
} from "../../services/postgresDatabaseServices"
import {parseISO} from "date-fns"

import GroupRoleContext from "../../contexts/GroupRoleContext"
import AssigmentModify from "./AssigmentModify"

import AddSolution from "../solution/AddSolution"
import Loading from "../animations/Loading"
import {AxiosError} from "axios"

import CheckedSolution from "../solution/CheckedSolution"
import UncheckedSolution from "../solution/UncheckedSolution"
import {AssignmentInterface} from "../../types/AssignmentInterface";
import {SolutionInterface} from "../../types/SolutionInterface";
import {getUser} from "../../services/otherServices";

function AssigmentSpecPage() {
  const {idAssigment, idGroup} = useParams()
  const location = useLocation()
  const navigate = useNavigate()
  const optionalUserId: string | null = location.state
  const userState = getUser()
  const {role} = useContext(GroupRoleContext)
  const [isSolutionChecked, setIsSolutionChecked] = useState<boolean | undefined>(undefined)
  const [solution, setSolution] = useState<SolutionInterface | undefined>(undefined)
  const [assignment, setAssignment] = useState<AssignmentInterface | undefined>(undefined)
  let userId: null | string = null

  useEffect(() => {
    if (userState !== undefined) {
      getUncheckedSolutionByUserAssignmentGroupPostgresService(userState.id.toString(), idAssigment as string, idGroup as string)
        .then((response) => {
          if (response.data.id !== null) {
            setIsSolutionChecked(false)
            setSolution(response.data)
          }
        })
        .catch((error: AxiosError) => {
          if (error.response?.status === 404) {
            getCheckedSolutionByUserAssignmentGroupPostgresService(userState.id.toString(), idAssigment as string, idGroup as string)
              .then((response) => {
                if (response.data.id !== null) {
                  setIsSolutionChecked(true)
                  setSolution(response.data)
                }
              })
              .catch((error: AxiosError) => console.log(error))
          } else {
            console.log(error)
          }
        })
        .finally(() => {
          getAssignmentPostgresService(idAssigment as string)
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
    }
  }, [])

  if (userState === undefined) {
    navigate("/")
  }
  if (assignment === undefined) {
    return (<Loading/>)
  }
  if (optionalUserId !== null) {
    userId = optionalUserId
  } else {
    if (userState !== undefined) {
      userId = userState.id.toString()
    }
  }
  return (
    <div>
      {((role === "Teacher") && (userId === null)) ? (
        <AssigmentModify assignment={assignment} setAssigment={setAssignment}/>
      ) : ((solution === undefined) && (userId === null)) ? (
        <AddSolution assignment={assignment}/>
      ) : isSolutionChecked ? (

        <CheckedSolution assignment={assignment} solution={solution as SolutionInterface}/>
      ) : (
        <UncheckedSolution assignment={assignment} solution={solution as SolutionInterface}/>
      )}
    </div>
  )
}

export default AssigmentSpecPage
