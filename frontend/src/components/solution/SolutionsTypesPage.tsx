import { useContext, useEffect, useState } from "react"
import { Link } from "react-router-dom"
import NotFoundPage from "../errors/NotFoundPage"
import Loading from "../animations/Loading"
import { solutionFilter } from "../../filter/SolutionFilter"
import { SolutionExtendedInterface } from "../../types/SolutionExtendedInterface"
import { useDispatch, useSelector } from "react-redux"
import { selectUserState } from "../../redux/userStateSlice"
import { selectGroup } from "../../redux/groupSlice"
import { GroupInterface } from "../../types/GroupInterface"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppDispatch } from "../../types/HooksRedux"

function SolutionsTypesPage({type}: { type: string }) {
  const [solutionsExtended, setSolutionsExtended] = useState<
    SolutionExtendedInterface[]
  >([])
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const dispatch = useAppDispatch()
  const {checkSolutions, uncheckedSolutions, lateSolutions} = solutionFilter({
    setSolutionsExtended,
    idGroup: group?.id as unknown as string,
  })

  function typeOfSolutions() {
    switch (type) {
      case "check":
        checkSolutions()
        dispatch(setIsLoading(false))
        break
      case "late":
        lateSolutions()
        dispatch(setIsLoading(false))
        break
      case "uncheck":
        uncheckedSolutions()
        dispatch(setIsLoading(false))
        break
      default:
        break
    }
  }
  useEffect(() => {
    dispatch(setIsLoading(true))
    setSolutionsExtended([])
    typeOfSolutions()
  }, [type])
  if (role !== "Teacher") {
    return <NotFoundPage/>
  }

  return (
    <div>
      <ul className='flex flex-col gap-3 mt-4 mx-[7.5%]'>
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
              state={{solution: solutionExtended}}
              className="flex relative border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center text-center gap-2"
            >
              <div className="flex-col pl-10 w-40 text-left">
                <div>{solutionExtended.user.index}</div>
                <div>{solutionExtended.user.firstname}</div>
              </div>
              <div className='font-semibold underline text-left w-[40%]'>{solutionExtended.assignment.title}</div>
              <p
                className="absolute right-0 mr-10 font-semibold text-[28px]">/{solutionExtended.assignment.max_points}</p>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default SolutionsTypesPage
