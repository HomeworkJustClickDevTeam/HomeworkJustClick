import { useLocation, useParams } from "react-router-dom"
import React from "react"

import AssignmentModifySettingsPageWrapper from "./AssignmentModifySettingsPageWrapper"

import SolutionAddPage from "../solution/SolutionAddPage"

import SolutionCheckedPage from "../solution/SolutionCheckedPage"
import SolutionUncheckedStudentPage from "../solution/SolutionUncheckedStudentPage"
import { AssignmentModel } from "../../types/Assignment.model"
import { SolutionInterface } from "../../types/SolutionInterface"
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetSolutionByUserAssignmentGroup } from "../customHooks/useGetSolutionByUserAssignmentGroup"
import { useGetAssignment } from "../customHooks/useGetAssignment"
import Loading from "../animation/Loading"

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
        <AssignmentModifySettingsPageWrapper assignment={assignment as AssignmentModel} setAssignment={setAssignment}/>
      ) : (checkedSolution === undefined && uncheckedSolution === undefined && role === "Student") ? (
        <SolutionAddPage assignment={assignment as AssignmentModel}/>
      ) : (uncheckedSolution === undefined) ? (
        <SolutionCheckedPage assignment={assignment as AssignmentModel} solution={checkedSolution as SolutionInterface}/>
      ) : (
        <SolutionUncheckedStudentPage assignment={assignment as AssignmentModel} solution={uncheckedSolution as SolutionInterface}/>
      ))}
    </div>
  )
}

export default AssignmentSpecPage
