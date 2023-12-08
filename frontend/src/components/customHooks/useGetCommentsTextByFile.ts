import { useEffect, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getCommentsByUserPostgresService,
  getCommentsImageByFilePostgresService, getCommentsTextByFilePostgresService
} from "../../services/postgresDatabaseServices"
import { AdvancedEvaluationImageCommentModel } from "../../types/AdvancedEvaluationImageComment.model"
import { AdvancedEvaluationTextCommentModel } from "../../types/AdvancedEvaluationTextComment.model"

export const useGetCommentsTextByFile = (fileId: number|undefined|null, params:string) => {
  const [comments, setComments] = useState<AdvancedEvaluationTextCommentModel[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(fileId !== undefined && fileId !== null) {
      dispatch(setIsLoading(true))
      getCommentsTextByFilePostgresService(fileId.toString(), "")
        .then(async (response) => {
          if (response !== null && response !== undefined) {
            const commentsFromServer: AdvancedEvaluationTextCommentModel[] = []
            for (let pageNumber = 0; pageNumber < response.data.totalPages; pageNumber++) {
              const responsePaged = await getCommentsTextByFilePostgresService(fileId.toString(), `?page=${pageNumber}&${params}`)
              if (responsePaged?.status === 200) {
                for (const commentFromServer of responsePaged.data.content) {
                  commentsFromServer.push({
                    id: commentFromServer.id,
                    file: commentFromServer.file,
                    highlightEnd: commentFromServer.highlightEnd,
                    highlightStart: commentFromServer.highlightStart,
                    color: commentFromServer.color,
                    comment: commentFromServer.comment
                  })
                }
              }
            }
            if (mounted) {
              setComments(commentsFromServer)
            }
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

  }, [fileId])
  return {comments, setComments}
}