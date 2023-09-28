import { useEffect, useState } from "react"
import { GroupInterface } from "../../types/GroupInterface"
import {
  getGroupsByStudentPostgresService,
  getGroupsByTeacherPostgresService,
  getGroupsByUserPostgresService
} from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetUserGroups = (userId:number|undefined|null, filter: 'student'|'teacher'|'all') => {
  const [groups, setGroups] = useState<GroupInterface[]>([])
  const dispatch = useAppDispatch()
  useEffect(() => {
    const fetchData = async () => {
      if(userId !== undefined && userId !== null){
        dispatch(setIsLoading(true))
        let response = null
        try{
          if(filter==="teacher") {
            response = await getGroupsByTeacherPostgresService(userId.toString())
          }
          else if (filter === 'student') {
            response = await getGroupsByStudentPostgresService(userId.toString())
          }
          else if(filter==='all') {
            response = await getGroupsByUserPostgresService(userId.toString())
          }
          if(response !== null && response !== undefined) {
            if(mounted){setGroups(response.data)}
          }
        }catch(e){console.log(e)}
        dispatch(setIsLoading(false))
      }

    }

    let mounted = true
    fetchData()
    return () => {mounted = false}
  }, [userId, filter])

  return groups
}