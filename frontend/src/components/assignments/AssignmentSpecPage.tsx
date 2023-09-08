import { useLocation, useNavigate, useParams } from "react-router-dom"
import React, { useContext, useEffect, useState } from "react"
import {
  getAssignmentPostgresService,
  getCheckedSolutionByUserAssignmentGroupPostgresService,
  getUncheckedSolutionByUserAssignmentGroupPostgresService
} from "../../services/postgresDatabaseServices"
import { parseISO } from "date-fns"

import AssignmentModify from "./AssignmentModify"

import SolutionAdd from "../solution/SolutionAdd"
import { AxiosError } from "axios"

import SolutionChecked from "../solution/SolutionChecked"
import SolutionUnchecked from "../solution/SolutionUnchecked"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { SolutionInterface } from "../../types/SolutionInterface"
import Loading from "../animations/Loading"
import { useDispatch, useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { selectRole } from "../../redux/roleSlice"

function AssignmentSpecPage() {
  const {idAssignment} = useParams()
  const location = useLocation()
  const navigate = useNavigate()
  const optionalUserId: string | null = location.state
  const [isSolutionChecked, setIsSolutionChecked] = useState<boolean | undefined>(undefined)
  const [solution, setSolution] = useState<SolutionInterface | undefined>(undefined)
  const [assignment, setAssignment] = useState<AssignmentInterface | undefined>(undefined)
  const userState = useSelector(selectUserState)
  const group= useSelector(selectGroup)
  const dispatch = useDispatch()
  const role = useSelector(selectRole)

  useEffect(() => {
    let userId = userState?.id.toString()
    if(optionalUserId !== null){
      userId = optionalUserId
    }
    if (userId !== undefined) {
      let ignore = false
      getUncheckedSolutionByUserAssignmentGroupPostgresService(userId, idAssignment as string, group?.id as unknown as string)
        .then((response) => {
          if(!ignore) {
            if (response.data.id !== null) {
              setIsSolutionChecked(false)
              setSolution(response.data)
            }
          }
        })
        .catch((error: AxiosError) => {
          if (error.response?.status === 404) {
            getCheckedSolutionByUserAssignmentGroupPostgresService(userId as string, idAssignment as string, group?.id as unknown as string)
              .then((response) => {
                if(!ignore) {
                  if (response.data.id !== null) {
                    setIsSolutionChecked(true)
                    setSolution(response.data)
                  }
                }
              })
              .catch((error: AxiosError) => console.log(error))
          } else {
            console.log(error)
          }
        })
        .finally(() => {
          getAssignmentPostgresService(idAssignment as string)
            .then((response) => {
              if(!ignore) {
                const responseData = response.data
                const parsedDate = parseISO(responseData.completionDatetime)
                setAssignment({
                  ...responseData,
                  completionDatetime: parsedDate,
                })
                dispatch(setIsLoading(false))
              }
            })
            .catch((error) => {
              console.log("Error fetching assignment:", error)
            })
        })
      return () => {
        ignore = true
      }
    }

  }, [userState, idAssignment, optionalUserId])

  if(assignment === undefined){
    return <Loading></Loading>
  }
  return (
    <div>
      {((role === "Teacher") ? (
        <AssignmentModify assignment={assignment as AssignmentInterface} setAssignment={setAssignment}/>
      ) : ((solution === undefined) && (role === "Student")) ? (
        <SolutionAdd assignment={assignment as AssignmentInterface}/>
      ) : isSolutionChecked ? (
        <SolutionChecked assignment={assignment as AssignmentInterface} solution={solution as SolutionInterface}/>
      ) : (
        <SolutionUnchecked assignment={assignment as AssignmentInterface} solution={solution as SolutionInterface}/>
      ))}
    </div>
  )
}

export default AssignmentSpecPage
