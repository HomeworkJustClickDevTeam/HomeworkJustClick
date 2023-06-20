import { useContext, useEffect, useState } from "react"
import GroupItem from "../group/groupItems/GroupItem"
import { Link } from "react-router-dom"
import { groupFilter } from "../group/filter/GroupFilter"
import { Group } from "../../types/types"
import Loading from "../animations/Loading"
import userContext from "../../UserContext"

function Home() {
  const [groups, setGroups] = useState<Group[]>()
  const [isLoading, setIsLoading] = useState<boolean>()
  const { userState } = useContext(userContext)

  const { teacherUserGroups, studentsUserGroups, allUserGroups } = groupFilter({
    setGroups,
    setIsLoading,
  })

  useEffect(() => {
    if (userState.userId) {
      allUserGroups()
    }
  }, [userState.userId])

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
    </>
  )
}
export default Home
