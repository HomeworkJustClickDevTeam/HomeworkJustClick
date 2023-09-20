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
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"

function AssignmentSpecPage() {
  const {idAssignment} = useParams()
  const location = useLocation()
  const navigate = useNavigate()
  const optionalUserId: string | null = location.state
  const [isSolutionChecked, setIsSolutionChecked] = useState<boolean | undefined>(undefined)
  const [solution, setSolution] = useState<SolutionInterface | undefined>(undefined)
  const [assignment, setAssignment] = useState<AssignmentInterface | undefined>(undefined)
  const userState = useAppSelector(selectUserState)
  const group= useAppSelector(selectGroup)
  const dispatch = useAppDispatch()
  const role = useAppSelector(selectRole)

  useEffect(() => {
    let userId = userState?.id.toString()
    if(optionalUserId !== null){
      userId = optionalUserId
    }
    if (userId !== undefined) {
      dispatch(setIsLoading(true))
      let mounted =true
      getUncheckedSolutionByUserAssignmentGroupPostgresService(userId, idAssignment as string, group?.id as unknown as string)
        .then((response) => {
          if(mounted) {
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
                if(mounted) {
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
              if(mounted) {
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
        mounted = true
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
