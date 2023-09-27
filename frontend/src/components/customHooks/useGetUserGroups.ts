import { useEffect, useState } from "react"
import { GroupInterface } from "../../types/GroupInterface"
import {
  getGroupsByStudentPostgresService,
  getGroupsByTeacherPostgresService,
  getGroupsByUserPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetUserGroups = (userId:number|undefined, filter: 'student'|'teacher'|'all') => {
  const [groups, setGroups] = useState<GroupInterface[]>([])
  const dispatch = useAppDispatch()
  useEffect(() => {
    let mounted = true
    if(userId !== undefined && userId !== null){
      if(filter==="teacher"){
        dispatch(setIsLoading(true))
        getGroupsByTeacherPostgresService(userId.toString())
          .then((response) => {
            if(mounted){
              const groups: GroupInterface[] = response.data
              setGroups(groups)
            }
          })
          .catch(() => setGroups([]))
        dispatch(setIsLoading(false))
      }
      else if (filter === 'student'){
        dispatch(setIsLoading(true))
        getGroupsByStudentPostgresService(userId.toString())
          .then((response) => {
            if(mounted) {
              const groups: GroupInterface[] = response.data
              setGroups(groups)
            }
          })
          .catch(() => setGroups([]))
        dispatch(setIsLoading(false))
      }
      else if(filter==='all'){
        dispatch(setIsLoading(true))
        getGroupsByUserPostgresService(userId.toString())
          .then((response) => {
            if(mounted) {
              const groups: GroupInterface[] = response.data
              setGroups(groups)
            }
          })
          .catch(() => setGroups([]))
        dispatch(setIsLoading(false))
      }
    }

    return () => {mounted = false}
  }, [userId, filter])

  return groups
}