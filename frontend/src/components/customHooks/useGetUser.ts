import { useEffect, useState } from "react"
import { UserInterface } from "../../types/UserInterface"
import { getUserPostgresService } from "../../services/postgresDatabaseServices"
import { AxiosError } from "axios/index"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetUser = (id: number|undefined) => {
  const [user, setUser] = useState<UserInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(id!== undefined){
      dispatch(setIsLoading(true))
      getUserPostgresService(id.toString())
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

  }, [id])

  return user
}