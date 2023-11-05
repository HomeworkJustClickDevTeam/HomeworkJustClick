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


export const AdvancedEvaluationTextArea = ({fileText, width, height, comments, setComments, handleNewCommentTextCreation, handleCommentHighlighting, editable}:{
  setComments:(comments:AdvancedEvaluationTextCommentInterface[]) => void,
  handleCommentHighlighting:(commentId: number) => void,
  editable:boolean
  handleNewCommentTextCreation: ()=> void,
  width:number|undefined,
  height:number|undefined,
  comments: AdvancedEvaluationTextCommentInterface[]
  fileText: string}) => {
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])
  const toLetterArray = (commentsList:AdvancedEvaluationTextCommentInterface[]):(string|undefined)[] => {
    let newArray = Array<undefined|string>(fileText.length)
    newArray.fill(undefined, 0, fileText.length)
    commentsList.forEach((comment)=>{
      newArray.fill(comment.color, comment.highlightStart, comment.highlightEnd+1)
    })
    return newArray
  }
  const handleCommentDeletion = async (index:number) => {
    try {
      let commentToBeDeleted:undefined|AdvancedEvaluationTextCommentInterface = undefined
      for(const comment of comments){
        if(comment.highlightStart <= index && index <= comment.highlightEnd){
          commentToBeDeleted = {...comment}
          break
        }
      }
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

  useEffect(() => {
    setLetterColor(toLetterArray(comments))
  }, [fileText, comments])

  return <div style={{width: width !== undefined ? width : "100%", height: height !== undefined ? height:"100%"}}>{fileText.split('').map((letter:string, index)=>{
    return <span
      onClick={(event) => {event.button === 2 && handleCommentDeletion(index)}}
      id={index.toString()}
      style={{backgroundColor: letterColor[index] ? letterColor[index]+"80" : 'white'}}
      key={index}
      onContextMenu={(event) => {
        event.preventDefault();
        event.stopPropagation()
        if(editable)
          handleCommentDeletion(index)}}
        onMouseUp={() =>{editable && handleNewCommentTextCreation()}}>
      {letter}</span>
  })}</div>
}