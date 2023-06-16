import { useContext, useEffect, useState } from "react"
import { PropsForType, SolutionExtended } from "../../types/types"
import { Link, useParams } from "react-router-dom"

import groupRoleContext from "../../GroupRoleContext"
import NotFound from "../errors/NotFound"
import Loading from "../animations/Loading"
import { solutionFilter } from "./filter/SolutionFilter"

function SolutionsTypes({ type }: PropsForType) {
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [solutionsExtended, setSolutionsExtended] = useState<
    SolutionExtended[]
  >([])
  const { id = "" } = useParams()
  const { role } = useContext(groupRoleContext)
  const { checkSolutions, uncheckedSolutions, lateSolutions } = solutionFilter({
    setSolutionsExtended,
    id: id,
  })
  function typeOfSolutions() {
    switch (type) {
      case "check":
        checkSolutions()
        setIsLoading(false)
        break
      case "late":
        lateSolutions()
        setIsLoading(false)
        break
      case "uncheck":
        uncheckedSolutions()
        setIsLoading(false)
        break
      default:
        break
    }
  }
  console.log(type)
  useEffect(() => {
    setIsLoading(true)
    setSolutionsExtended([])
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
        {solutionsExtended.map((solutionExtended) => (
          <li
            key={
              solutionExtended.id +
              solutionExtended.assignment.id +
              solutionExtended.user.id
            }
          >
            <Link
              to={`/group/${solutionExtended.assignment.groupId}/solution/${solutionExtended.user.id}/${solutionExtended.assignment.id}`}
              state={{ solution: solutionExtended }}
            >
              {solutionExtended.user.firstname}
              {solutionExtended.assignment.title}
              {solutionExtended.assignment.max_points}
            </Link>
          </li>
        ))}
      </ul>
    </>
  )
}
export default SolutionsTypes
