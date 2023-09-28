import { useEffect, useState } from "react"
import { UserInterface } from "../../types/UserInterface"
import { getUserPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios/index"
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
        .catch((error: AxiosError) => console.log(error))
      dispatch(setIsLoading(false))
    }
    return () => {mounted = false}

  }, [userId])

  return user
}