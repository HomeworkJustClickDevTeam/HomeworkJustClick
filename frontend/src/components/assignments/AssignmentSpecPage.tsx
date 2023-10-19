import { useLocation, useParams } from "react-router-dom"
import React from "react"

import AssignmentModify from "./AssignmentModify"

import SolutionAdd from "../solution/SolutionAdd"

import SolutionChecked from "../solution/SolutionChecked"
import SolutionUnchecked from "../solution/SolutionUnchecked"
import { AssignmentInterface } from "../../types/AssignmentInterface"
import { SolutionInterface } from "../../types/SolutionInterface"
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetSolutionByUserAssignmentGroup } from "../customHooks/useGetSolutionByUserAssignmentGroup"
import { useGetAssignment } from "../customHooks/useGetAssignment"
import Loading from "../animations/Loading"

function AssignmentSpecPage() {
  const {idAssignment} = useParams()
  const location = useLocation()
  let userId: number | null = location.state
  const {assignment, setAssignment}= useGetAssignment(idAssignment as unknown as number)
  const userState = useAppSelector(selectUserState)
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const checkedSolution = useGetSolutionByUserAssignmentGroup(userId !== null ? userId : userState?.id, idAssignment as unknown as number, group?.id, "checked")
  const uncheckedSolution = useGetSolutionByUserAssignmentGroup(userId !== null ? userId : userState?.id, idAssignment as unknown as number, group?.id, "unchecked")

  if(assignment === undefined){
    return <Loading></Loading>
  }
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
