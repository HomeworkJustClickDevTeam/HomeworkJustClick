import { useContext, useEffect, useState } from "react"
import GroupItem from "../group/groupItems/GroupItem"
import { Link } from "react-router-dom"
import { groupFilter } from "../group/filter/GroupFilter"
import { Action, Group } from "../../types/types"
import Loading from "../animations/Loading"
import userContext from "../../UserContext"
import DispatchContext from "../../DispatchContext"
import {render} from "@testing-library/react";
import {FaCaretDown} from 'react-icons/fa';

function Home() {
  const [groups, setGroups] = useState<Group[] | undefined>(undefined)
  const [isLoading, setIsLoading] = useState<boolean>()
  const [isOpen, setIsOpen] = useState(false)
  const [btnName, setBtnName] = useState('Wszystkie grupy')
  const { userState } = useContext(userContext)
  const globalDispatch = useContext(DispatchContext)
  useEffect(() => {
    const action: Action = {
      type: "homePageIn",
    }
    globalDispatch?.(action)
  }, [])
  const { teacherUserGroups, studentsUserGroups, allUserGroups } = groupFilter({
    setGroups,
    setIsLoading,
  })

  useEffect(() => {
    if (userState.userId) {
      allUserGroups()
    }
  }, [userState])

  if (groups === undefined || groups === null) {
    return <Loading />
  }


  return (
    <div className='pl-5 pr-6'>
        <div className='flex inline-block mb-4 mt-4 pl-3'>
            <p className='pr-8 text-lg'>Moje grupy</p>
          <Link to="/create/group" className=''>
            <button type="button" className='text-black text-xs underline items-center pt-2'>Stwórz grupę +</button>
          </Link>
          <div className='absolute inline-block text-right right-0 mr-16'>
            <div className='flex inline'>
              <span className='mr-2'>Wyświetlane grupy: </span>
              <button className='group flex inline underline' onClick={() => setIsOpen(el => !el)}>{btnName}<FaCaretDown className=' ml-1 h-full w-[9px] items-center'/>
              </button>
            </div>

            {isOpen && (
                <div className=' absolute left-20 z-10 w-56 mt-4 origin-top-right bg-white border border-hover_gray rounded-md shadow-lg'>
                  <div onClick={() => {studentsUserGroups(); setBtnName('Grupy uczniowskie')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">
                    Grupy uczniowskie użytkownika
                  </div>
                  <div onClick={() => {teacherUserGroups(); setBtnName('Grupy uczniowskie')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">
                    {" "}
                    Grupy nauczycielskie użytkownika
                  </div>
                  <div onClick={() => {allUserGroups(); setBtnName('Grupy uczniowskie')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">Wszystkie grupy użytkownika</div>
                </div>
            )}

          </div>
        </div>
      {isLoading ? (
        <Loading />
      ) : (
        <div className='relative flex flex-wrap gap-x-3 gap-y-2 bg-light_gray px-8 py-6 bg-lilly-bg rounded-md'>
          {groups?.map((group) =>
              groups?.length > 0 ? <GroupItem group={group} key={group.id} /> : <div>Dodaj nową grupę!</div>
          )}
        </div>
      )}

    </div>
  )
}
export default Home
