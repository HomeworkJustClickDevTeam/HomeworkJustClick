import { Link } from "react-router-dom"
import NotFoundPage from "../errors/NotFoundPage"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import { useGetExtendedSolutionsByGroup } from "../customHooks/useGetExtendedSolutionsByGroup"
import { ExtendedSolutionType } from "../../types/ExtendedSolutionType"

function SolutionsTypesPage({ type }: { type: ExtendedSolutionType }) {
  const group = useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const solutionsExtended = useGetExtendedSolutionsByGroup(group?.id, type)

  if (role !== "Teacher") {
    return <NotFoundPage />
  }

  return (
    <div className='flex flex-col h-[calc(100vh-325px)] overflow-y-hidden'>
      <ul className="flex flex-col gap-3 mt-4 ml-[7.5%] box-content overflow-y-scroll mb-4">
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
              className="flex relative border-border_gray border w-[42.5%] h-16 rounded-lg font-lato text-xl items-center text-center gap-2"
            >
              <div className="flex-col pl-10 w-40 text-left">
                <div>{solutionExtended.user.index}</div>
                <div>{solutionExtended.user.name}</div>
              </div>
              <div className="font-semibold underline text-left w-[40%]">
                {solutionExtended.assignment.title}
              </div>
              <p className="absolute right-0 mr-10 font-semibold text-[28px]">
                /{solutionExtended.assignment.max_points}
              </p>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  )
}

export default SolutionsTypesPage
