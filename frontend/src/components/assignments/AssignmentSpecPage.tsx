import { useLocation, useNavigate, useParams } from "react-router-dom"
import React, { useEffect, useState } from "react"
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
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { useGetSolutionByUserAssignmentGroup } from "../customHooks/useGetSolutionByUserAssignmentGroup"
import { useGetAssignment } from "../customHooks/useGetAssignment"

function AssignmentSpecPage() {
  const {idAssignment} = useParams()
  const location = useLocation()
  let userId: number | undefined = location.state
  const {assignment, setAssignment}= useGetAssignment(idAssignment as unknown as number)
  const userState = useAppSelector(selectUserState)
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const checkedSolution = useGetSolutionByUserAssignmentGroup(userId !== undefined? userId : userState?.id, idAssignment as unknown as number, group?.id, "checked")
  const uncheckedSolution = useGetSolutionByUserAssignmentGroup(userId !== undefined? userId : userState?.id, idAssignment as unknown as number, group?.id, "unchecked")

  return (
    <div>
      {((role === "Teacher") ? (
        <AssignmentModify assignment={assignment as AssignmentInterface} setAssignment={setAssignment}/>
      ) : (checkedSolution === undefined && uncheckedSolution === undefined && role === "Student") ? (
        <SolutionAdd assignment={assignment as AssignmentInterface}/>
      ) : (uncheckedSolution === undefined) ? (
        <SolutionChecked assignment={assignment as AssignmentInterface} solution={checkedSolution as SolutionInterface}/>
      ) : (
        <SolutionUnchecked assignment={assignment as AssignmentInterface} solution={uncheckedSolution as SolutionInterface}/>
      ))}
    </div>
  )
}

export default AssignmentSpecPage
