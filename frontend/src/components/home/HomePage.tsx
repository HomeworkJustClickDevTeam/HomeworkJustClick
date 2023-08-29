import React, {useContext, useEffect, useState} from "react"
import GroupListElement from "../group/GroupListElement"
import {Link, useNavigate} from "react-router-dom"
import {groupFilter} from "../group/filter/GroupFilter"
import Loading from "../animations/Loading"
import DispatchContext from "../../contexts/DispatchContext"
import {FaCaretDown} from 'react-icons/fa';
import {GroupInterface} from "../../types/GroupInterface";
import {ActionType} from "../../types/ActionType";
import {getUser} from "../../services/otherServices";

function HomePage() {
  const [groups, setGroups] = useState<GroupInterface[] | undefined>(undefined)
  const [isLoading, setIsLoading] = useState<boolean>()
  const [isOpen, setIsOpen] = useState(false)
  const [btnName, setBtnName] = useState('Wszystkie grupy')
  const navigate = useNavigate()
  const userState = getUser()
  const globalDispatch = useContext(DispatchContext)
  let teacherUserGroups = () => {}
  let studentsUserGroups = () => {}
  let allUserGroups = () => {}

  if(userState !== undefined){
    ({teacherUserGroups, studentsUserGroups, allUserGroups } = groupFilter({
      setGroups,
      setIsLoading,
      userId: userState.id.toString()
    }))
  }
  useEffect(() => {
    const action: ActionType = {
      type: "homePageIn",
    }
    globalDispatch?.dispatch(action)
    allUserGroups()
  }, [])

  if (groups === undefined || groups === null) {
    return <Loading />
  }
  if(userState === undefined){
    return <>User Not Logged in</>
  }


  return (
    <div className='pl-6 pr-8'>
        <div className='flex inline-block mb-4 mt-4 pl-5'>
            <p className='pr-8 text-2xl'>Moje grupy</p>
          <Link to="/create/group" className=''>
            <button type="button" className='text-black text-base underline items-center pt-2'>Stwórz grupę +</button>
          </Link>
          <div className='absolute inline-block text-left right-0 mr-16'>
            <div className='flex inline'>
              <span className='mr-2'>Wyświetlane grupy: </span>
              <button className='flex inline underline' onClick={() => setIsOpen(el => !el)}>{btnName}<FaCaretDown className=' ml-1 h-full w-[9px] items-center'/>
              </button>
            </div>

            {isOpen && (
                <div className=' absolute left-20 z-10 w-56 mt-4 origin-top-right bg-white border border-hover_gray rounded-md shadow-lg'>
                  <div onClick={() => {studentsUserGroups(); setBtnName('Grupy uczniowskie')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">
                    Grupy uczniowskie użytkownika
                  </div>
                  <div onClick={() => {teacherUserGroups(); setBtnName('Grupy nauczycielskie')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">
                    {" "}
                    Grupy nauczycielskie użytkownika
                  </div>
                  <div onClick={() => {allUserGroups(); setBtnName('Wszystkie grupy')}} className="block px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black">Wszystkie grupy użytkownika</div>
                </div>
            )}

          </div>
        </div>
      {isLoading ? (
        <Loading />
      ) : (
        <div className='relative flex flex-wrap gap-x-5 gap-y-2 bg-light_gray px-8 py-6 bg-lilly-bg rounded-md'>
          {groups?.map((group) =>
              groups?.length > 0 ? <GroupListElement group={group} key={group.id} /> : <div>Dodaj nową grupę!</div>
          )}
        </div>
      )}

    </div>
  )
}
export default HomePage
