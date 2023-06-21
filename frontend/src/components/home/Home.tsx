import { useEffect, useState } from "react"
import LogOut from "../user/logging/LogOut"
import GroupItem from "../group/groupItems/GroupItem"
import { Link } from "react-router-dom"
import { groupFilter } from "../group/filter/GroupFilter"
import { Group } from "../../types/types"
import Loading from "../animations/Loading"

function Home() {
  const [groups, setGroups] = useState<Group[]>()
  const [isLoading, setIsLoading] = useState<boolean>()

  const { teacherUserGroups, studentsUserGroups, allUserGroups } = groupFilter({
    setGroups,
    setIsLoading,
  })

  useEffect(() => {
    allUserGroups()
  }, [])

  return (
    <div className='pl-[2vw] pr-[3vw]'>
        <div className=' flex inline-block mb-4 mt-4 pl-[1vw]'>
            <p className='pr-8 text-lg'>Moje grupy</p>
          <Link to="/create/group" className=''>
            <button type="button" className='text-light_gray text-xs underline items-center pt-2'>Stwórz grupę +</button>
          </Link>
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
      <button onClick={studentsUserGroups}>
        Grupy uczniowskie użytkownika
      </button>
      <button onClick={teacherUserGroups}>
        {" "}
        Grupy nauczycielskie użytkownika
      </button>
      <button onClick={allUserGroups}>Wszystkie grupy użytkownika</button>
      <LogOut />
    </div>
  )
}
export default Home
