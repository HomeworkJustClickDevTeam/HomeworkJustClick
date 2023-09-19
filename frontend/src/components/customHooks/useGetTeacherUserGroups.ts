import { useEffect, useState } from "react"
import { GroupInterface } from "../../types/GroupInterface"
import { getGroupsByTeacherPostgresService } from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetTeacherUserGroups = (userId:number) => {
  const [groups, setGroups] = useState<GroupInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    dispatch(setIsLoading(true))
    let mounted = true
    getGroupsByTeacherPostgresService(userId.toString())
      .then((response) => {
        if(mounted){
          const groups: GroupInterface[] = response.data
          setGroups(groups)
        }
      })
      .catch(() => setGroups([]))
    dispatch(setIsLoading(false))

    return () => {mounted = false}
  }, [userId])

  return groups
}