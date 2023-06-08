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
  console.log(type)
  useEffect(() => {
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
          <li key={userWithAssignment.solution.id}>
            <Link
              to="/solution"
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
