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
    <div >
      <ul className='flex flex-col gap-3 mt-4 ml-28'>
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
              state={{ solution: solutionExtended }} className="flex relative border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center text-center justify-between gap-2"
            >
              <div className="flex-col pl-10 ">
                <div>{solutionExtended.user.index}</div>
                <div>{solutionExtended.user.firstname}</div>
              </div>
              <div className='font-semibold underline'>{solutionExtended.assignment.title}</div>
              <p className="mr-10 font-semibold text-[28px]">/{solutionExtended.assignment.max_points}</p>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
export default SolutionsTypes
