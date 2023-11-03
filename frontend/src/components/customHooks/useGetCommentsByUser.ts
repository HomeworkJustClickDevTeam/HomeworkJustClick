import { useEffect, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getCommentsByUserPostgresService } from "../../services/postgresDatabaseServices"

export const useGetCommentsByUser = (userId: number|undefined|null, params:string) => {
  const [comments, setComments] = useState<CommentInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    dispatch(setIsLoading(true))
    let mounted = true
    if(userId !== undefined && userId !== null) {
      getCommentsByUserPostgresService(userId.toString(), params)
        .then((response) => {
          const commentsFromServer = response.data as CommentInterface[]
          if (response !== null && response !== undefined){
            if(mounted){setComments(commentsFromServer)}
          }
        })
        .catch((error)=> {
          if(error !== null && error !== undefined && error.response.status === 404) {
            if(mounted){setComments([])}
          }
          else {console.log(error)}
        })
      dispatch(setIsLoading(false))
    }
    return () => {mounted = false}

  }, [userId])

  return {comments, setComments}
}