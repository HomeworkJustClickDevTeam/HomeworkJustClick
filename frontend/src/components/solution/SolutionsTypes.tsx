import { useContext, useEffect, useState } from "react"
import { PropsForType, UserWithAssignment } from "../../types/types"
import { Link, useParams } from "react-router-dom"

import groupRoleContext from "../../GroupRoleContext"
import { SolutionFilter } from "./filter/SolutionFilter"
import NotFound from "../errors/NotFound"
import Loading from "../animations/Loading"

function SolutionsTypes({ type }: PropsForType) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [usersWithAssigment, setUserWithAssignment] = useState<
    UserWithAssignment[]
  >([])
  const { id = "" } = useParams()
  const { role } = useContext(groupRoleContext)
  const { checkSolutions, uncheckedSolutions, lateSolutions } = SolutionFilter({
    setUsersWithAssignment: setUserWithAssignment,
    id: id,
  })
  console.log(type)
  useEffect(() => {
    function typeOfSolutions() {
      switch (type) {
        case "check":
          checkSolutions()
            .then(() => setIsLoading(false))
            .catch()
          break
        case "late":
          lateSolutions()
            .then(() => setIsLoading(false))
            .catch()
          break
        case "uncheck":
          uncheckedSolutions()
            .then(() => setIsLoading(false))
            .then()
          break
        default:
          break
      }
    }
    setUserWithAssignment([])
    typeOfSolutions()
  }, [type])
  if (role !== "Teacher") {
    return <NotFound />
  }
  if (isLoading) {
    return <Loading />
  }

  return (
    <>
      <ul>
        {usersWithAssigment.map((userWithAssignment) => (
          <li
            key={
              userWithAssignment.solution.id +
              userWithAssignment.assignment.id +
              userWithAssignment.user.id
            }
          >
            <Link
              to={`/group/${userWithAssignment.assignment.groupId}/solution/${userWithAssignment.user.id}/${userWithAssignment.assignment.id}`}
              state={{ userWithAssignment: userWithAssignment }}
            >
              {userWithAssignment.user.firstname}
              {userWithAssignment.assignment.title}
              {userWithAssignment.assignment.max_points}
            </Link>
          </li>
        ))}
      </ul>
    </>
  )
}
export default SolutionsTypes
