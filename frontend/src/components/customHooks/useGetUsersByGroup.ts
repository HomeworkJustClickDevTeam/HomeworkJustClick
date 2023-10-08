import { useEffect, useState } from "react"
import { UserInterface } from "../../types/UserInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getStudentsByGroupPostgresService,
  getTeachersByGroupPostgresService
} from "../../services/postgresDatabaseServices"

export const useGetUsersByGroup = (groupId:number|undefined|null, filter:"students"|"teachers") => {
  const [users, setUsers] = useState<UserInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchdata = async () => {
      if(groupId !== undefined && groupId !== null){
        let response = null

        try {
          if(filter === "students"){
            response = await getStudentsByGroupPostgresService(groupId.toString())
          }
          else if(filter === "teachers"){
            response = await getTeachersByGroupPostgresService(groupId.toString())
          }
          if(response !== null && response !== undefined) {
            if(mounted){setUsers(response.data)}
          }
          }catch (error:any){
          if(error !== undefined && error !== null && error.response.status === 404){
            if(mounted){setUsers([])}
          }
          else {console.log(error)}
        }

      }
    }
    let mounted = true
    dispatch(setIsLoading(true))
    fetchdata()
    dispatch(setIsLoading(false))
    return () => {mounted = false}
  }, [groupId, filter])
  return {users, setUsers}
}