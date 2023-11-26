import { AdvancedEvaluationTextCommentInterface } from "../../types/AdvancedEvaluationTextCommentInterface"
import { CommentInterface } from "../../types/CommentInterface"
import React, { useEffect, useState } from "react"
import { AdvancedEvaluationTextCommentCreateInterface } from "../../types/AdvancedEvaluationTextCommentCreateInterface"
import {
  changeCommentTextPostgresService,
  createCommentTextWithFilePostgresService,
  deleteCommentTextPostgresService
} from "../../services/postgresDatabaseServices"
import { useGetCommentsTextByFile } from "../customHooks/useGetCommentsTextByFile"


export const AdvancedEvaluationTextArea = ({fileText, width, height, comments, setComments, handleNewCommentTextCreation, handleCommentHighlighting, editable, chosenComment}:{
  chosenComment:CommentInterface|undefined,
  setComments:(comments:AdvancedEvaluationTextCommentInterface[]) => void,
  handleCommentHighlighting:(commentId: number) => void,
  editable:boolean,
  handleNewCommentTextCreation: ()=> void,
  width:number|undefined,
  height:number|undefined,
  comments: AdvancedEvaluationTextCommentInterface[]
  fileText: string}) => {
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])
  const [mouseDownTimestamp, setMouseDownTimestamp] = useState<undefined|number>(undefined)
  const toLetterArray = (commentsList:AdvancedEvaluationTextCommentInterface[]):(string|undefined)[] => {
    let newArray = Array<undefined|string>(fileText.length)
    newArray.fill(undefined, 0, fileText.length)
    commentsList.forEach((comment)=>{
      if(comment.highlightStart <= comment.highlightEnd) newArray.fill(comment.color, comment.highlightStart, comment.highlightEnd+1)
    })
    return newArray
  }
  const calculateCommentBasedOnLetterIndex = (index:number):AdvancedEvaluationTextCommentInterface|undefined => {
    let resultComment:undefined|AdvancedEvaluationTextCommentInterface = undefined
    for(const comment of comments){
      if(comment.highlightStart <= index && index <= comment.highlightEnd){
        resultComment = {...comment}
        break
      }
    }
    return resultComment
  }
  const handleCommentDeletion = async (index:number) => {
    try {
      const commentToBeDeleted = calculateCommentBasedOnLetterIndex(index)
      if(commentToBeDeleted!==undefined){
        const response = await deleteCommentTextPostgresService(commentToBeDeleted.id.toString())
        if(response?.status === 200){
          const tempComments = comments.filter((comment)=>comment.id !== commentToBeDeleted?.id)
          setComments(tempComments)
        }
      }
    }catch (error){
      console.log(error)
    }
  }
  const handleMouseUp = async (event:React.MouseEvent, index:number) => {
    event.preventDefault()
    event.stopPropagation()
    if(event.button === 2) await handleCommentDeletion(index)
    else if(event.button === 0) {
      if(mouseDownTimestamp !== undefined && (event.timeStamp - mouseDownTimestamp) < 150){
        const clickedComment = calculateCommentBasedOnLetterIndex(index)
        if(clickedComment !== undefined){
          handleCommentHighlighting(clickedComment.commentId)
          setMouseDownTimestamp(undefined)
        }
      }
      else{
        handleNewCommentTextCreation()
      }
    }
  }

  useEffect(() => {
    setLetterColor(toLetterArray(comments))
  }, [fileText, comments])

  return <div style={{width: width !== undefined ? width : "100%", height: height !== undefined ? height:"100%"}}>{fileText.split('').map((letter:string, index)=>{
    return <span
      onMouseDown={(event) => {event.button===0 && setMouseDownTimestamp(event.timeStamp)}}
      id={index.toString()}
      style={{backgroundColor: letterColor[index] ? letterColor[index]+"80" : 'white', userSelect:chosenComment === undefined ? "none" : "auto"}}
      key={index}
      onContextMenu={(event) => {event.preventDefault();event.stopPropagation()}}
      onMouseUp={(event) =>handleMouseUp(event, index)}>
      {letter}</span>
  })}</div>
}