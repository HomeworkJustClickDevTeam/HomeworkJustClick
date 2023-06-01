import { useEffect, useState } from "react"
import LogOut from "../user/logging/LogOut"
import GroupItem from "../group/groupItems/GroupItem"
import { Link } from "react-router-dom"
import { groupFilter } from "../group/filter/GroupFilter"
import { Group } from "../../types/types"
import Loading from "../animations/Loading"

function Home() {
  const [groups, setGroups] = useState<Group[]>()
  const [isLoading, setIsLoading] = useState<boolean>(true)

  const { teacherUserGroups, studentsUserGroups, allUserGroups } = groupFilter({
    setGroups,
  })

  useEffect(() => {
    allUserGroups()
    setIsLoading(false)
  }, [])

  return (
    <>
      <Link to="/create/group">
        <button type="button">Stwórz grupe</button>
      </Link>
      {isLoading ? (
        <Loading />
      ) : (
        <div>
          {groups?.map((group) => (
            <GroupItem group={group} key={group.id} />
          ))}
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
    </>
  )
}
export default Home
