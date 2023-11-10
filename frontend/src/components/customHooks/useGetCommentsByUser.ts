import { useEffect, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { getCommentsByUserPostgresService } from "../../services/postgresDatabaseServices"
import { parseISO } from "date-fns"

export const useGetCommentsByUser = (userId: number|undefined|null, params:string) => {
  const [comments, setComments] = useState<CommentInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(userId !== undefined && userId !== null) {
      dispatch(setIsLoading(true))
      getCommentsByUserPostgresService(userId.toString(), params)
        .then(async (response) => {
          if (response !== null && response !== undefined){
            const commentsFromServer:CommentInterface[] = []
            for(const commentFromServer of response.data.content){
              commentsFromServer.push({
                title:commentFromServer.title,
                description: commentFromServer.description,
                color:commentFromServer.color,
                userId:commentFromServer.user.id,
                lastUsedDate: parseISO(commentFromServer.lastUsedDate),
                counter: commentFromServer.counter,
                id: commentFromServer.id
              })
            }
            for(let pageNumber = 1; pageNumber < response.data.totalPages; pageNumber++) {
              const response = await getCommentsByUserPostgresService(userId.toString(), `?page=${pageNumber}&${params}`)
              if (response?.status === 200) {
                for (const commentFromServer of response.data.content) {
                  commentsFromServer.push({
                    title: commentFromServer.title,
                    description: commentFromServer.description,
                    color: commentFromServer.color,
                    userId: commentFromServer.user.id,
                    lastUsedDate: parseISO(commentFromServer.lastUsedDate),
                    counter: commentFromServer.counter,
                    id: commentFromServer.id
                  })
                }
              }
            }
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

  }, [userId, params])

  return {comments, setComments}
}