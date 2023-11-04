import { useEffect, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { useAppDispatch } from "../../types/HooksRedux"
import { setIsLoading } from "../../redux/isLoadingSlice"
import {
  getCommentsByUserPostgresService,
  getCommentsImageByFilePostgresService
} from "../../services/postgresDatabaseServices"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const useGetCommentsImageByFile = (fileId: number|undefined|null, params:string) => {
  const [comments, setComments] = useState<AdvancedEvaluationImageCommentInterface[]>([])
  const dispatch = useAppDispatch()

  useEffect(() => {
    let mounted = true
    if(fileId !== undefined && fileId !== null) {
      dispatch(setIsLoading(true))
      getCommentsImageByFilePostgresService(fileId.toString(), params)
        .then((response) => {
          if (response !== null && response !== undefined){
            let commentsFromServer:AdvancedEvaluationImageCommentInterface[] = []
            for(const commentFromServer of response.data.content){
              commentsFromServer.push({
                id: commentFromServer.id,
                leftTopX: commentFromServer.leftTopX,
                leftTopY: commentFromServer.leftTopY,
                width: commentFromServer.width,
                height: commentFromServer.height,
                lineWidth: commentFromServer.lineWidth,
                imgHeight: commentFromServer.imgHeight,
                imgWidth: commentFromServer.imgWidth,
                color: commentFromServer.color,
                commentId: commentFromServer.comment.id,
                fileId: commentFromServer.file.id
              })
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

  }, [fileId])
  return comments
}