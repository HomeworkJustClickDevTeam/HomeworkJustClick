import { useEffect, useState } from "react"
import { UserInterface } from "../../types/UserInterface"
import { getUserPostgresService } from "../../services/postgresDatabaseServices"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetUser = (userId: number|undefined|null) => {
  const [user, setUser] = useState<UserInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(userId!== undefined && userId!== null){
      dispatch(setIsLoading(true))
      getUserPostgresService(userId.toString())
        .then((response) =>
        {
          if(response !== null && response !== undefined){
            if(mounted){
              setUser(response.data)
            }
          }
        })
        .catch((error) => {
          if(error !== null && error !== undefined && error.response.status === 404){
            if(mounted){setUser(undefined)}
          }
          else{
            console.log("Error fetching user:", error)
          }
        })
      dispatch(setIsLoading(false))
    }
    return () => {mounted = false}

  }, [userId])

  return user
}