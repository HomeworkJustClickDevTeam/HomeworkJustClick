import { useContext, useEffect, useState } from "react"
import GroupItem from "../group/groupItems/GroupItem"
import { Link } from "react-router-dom"
import { groupFilter } from "../group/filter/GroupFilter"
import { Action, Group } from "../../types/types"
import Loading from "../animations/Loading"
import userContext from "../../UserContext"
import DispatchContext from "../../DispatchContext"

function Home() {
  const [groups, setGroups] = useState<Group[]>()
  const [isLoading, setIsLoading] = useState<boolean>()
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
