import { AdvancedEvaluationTextCommentModel } from "../../types/AdvancedEvaluationTextComment.model"
import { CommentInterface } from "../../types/CommentInterface"
import React, { useEffect, useState } from "react"
import { AdvancedEvaluationTextCommentCreateInterface } from "../../types/AdvancedEvaluationTextCommentCreateInterface"
import {
  changeCommentTextPostgresService,
  createCommentTextWithFilePostgresService, deleteCommentTextByCommentFilePostgresService,
  deleteCommentTextPostgresService
} from "../../services/postgresDatabaseServices"
import { useGetCommentsTextByFile } from "../customHooks/useGetCommentsTextByFile"
import {SortCommentsButtonStateType} from "../../types/SortCommentsButtonStateType";
import {useUpdateEffect} from "usehooks-ts";



interface AdvancedEvaluationTextAreaPropsInterface{
  chosenComment?:CommentInterface|undefined,
  setComments?:(comments:AdvancedEvaluationTextCommentModel[]) => void,
  handleCommentHighlighting:(commentId: number) => void,
  width:number|undefined,
  height:number|undefined,
  comments: AdvancedEvaluationTextCommentModel[]
  fileText: string,
  fileId?: number,
  commentsText: AdvancedEvaluationTextCommentModel[],
  setCommentsText?: React.Dispatch<React.SetStateAction<AdvancedEvaluationTextCommentModel[]>>,
  setSortButton?: React.Dispatch<React.SetStateAction<SortCommentsButtonStateType>>,
  setRefreshRightPanelUserComments?: React.Dispatch<React.SetStateAction<boolean>>,
  deletedCommentId?: number|undefined,
  setDeletedCommentId?:React.Dispatch<React.SetStateAction<number|undefined>>,
  updatedComment?:CommentInterface|undefined,
  setUpdatedComment?:React.Dispatch<React.SetStateAction<CommentInterface|undefined>>,
}
export const AdvancedEvaluationTextArea = ({fileText,
                                             setUpdatedComment,
                                             updatedComment,
                                             setDeletedCommentId,
                                             deletedCommentId,
                                             setSortButton,
                                             setRefreshRightPanelUserComments,
                                             setCommentsText,
                                             commentsText,
                                             fileId,
                                             width,
                                             height,
                                             comments,
                                             setComments,
                                             handleCommentHighlighting,
                                             chosenComment}:AdvancedEvaluationTextAreaPropsInterface) => {
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])
  const [mouseDownTimestamp, setMouseDownTimestamp] = useState<undefined|number>(undefined)
  const toLetterArray = (commentsList:AdvancedEvaluationTextCommentModel[]):(string|undefined)[] => {
    let newArray = Array<undefined|string>(fileText.length)
    newArray.fill(undefined, 0, fileText.length)
    commentsList.forEach((comment)=>{
      if(comment.highlightStart <= comment.highlightEnd) newArray.fill(comment.color, comment.highlightStart, comment.highlightEnd+1)
    })
    return newArray
  }
  const calculateCommentBasedOnLetterIndex = (index:number):AdvancedEvaluationTextCommentModel|undefined => {
    let resultComment:undefined|AdvancedEvaluationTextCommentModel = undefined
    for(const comment of comments){
      if(comment.highlightStart <= index && index <= comment.highlightEnd){
        resultComment = {...comment}
        break
      }
    }
    return resultComment
  }
  const handleDrawnCommentDeletion = async (index:number) => {
    if(setComments===undefined) return
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
  const handleMouseUp = async (event:React.MouseEvent) => {
    event.preventDefault()
    event.stopPropagation()
    const index = +(event.target as HTMLSpanElement).id
    if(setComments !== undefined && event.button === 2) await handleDrawnCommentDeletion(index)
    else if(event.button === 0) {
      if(mouseDownTimestamp !== undefined && (event.timeStamp - mouseDownTimestamp) < 150){
        const clickedComment = calculateCommentBasedOnLetterIndex(index)
        if(clickedComment !== undefined){
          handleCommentHighlighting(clickedComment.comment.id)
          setMouseDownTimestamp(undefined)
        }
      }
      else{
        setComments !== undefined && await handleNewCommentTextCreation()
      }
    }
  }

  const printFormattedText = () =>{
    const formattedText:JSX.Element[] = []
    let index = 0
    fileText.split(/\r?\n/).forEach(line=>{
      line.split("").forEach((letter:string)=>{
        formattedText.push(<span
          onMouseDown={(event) => {
            event.button===0 && setMouseDownTimestamp(event.timeStamp)}}
          id={index.toString()}
          style={{backgroundColor: letterColor[index] ? letterColor[index]+"80" : 'white', userSelect:chosenComment === undefined ? "none" : "auto"}}
          key={index}
          onContextMenu={(event) => {
            event.preventDefault();
            event.stopPropagation()}}
          onMouseUp={(event) =>handleMouseUp(event)}>
          {letter}</span>)
        index++
      })
      formattedText.push(<br key={index}/>)
      index++
    })
    return formattedText
  }
  const handleNewCommentTextCreation =  async () =>{
    if(chosenComment === undefined || fileId === undefined || setCommentsText === undefined || setSortButton === undefined || setRefreshRightPanelUserComments === undefined) return
    const checkNewRanges = async (updatedComment:AdvancedEvaluationTextCommentModel) => {
      if(updatedComment.highlightStart<=updatedComment.highlightEnd){
        const response = await changeCommentTextPostgresService(updatedComment)
        return response?.status === 200;
      }
      else{
        await deleteCommentTextPostgresService(updatedComment.id.toString())
        return true
      }
    }
    const selection = window.getSelection()
    try {
      if (selection && selection.rangeCount > 0 && window.getSelection()?.getRangeAt(0).endContainer.parentElement?.id && window.getSelection()?.getRangeAt(0).startContainer.parentElement?.id !== null) {
        const newComment: AdvancedEvaluationTextCommentCreateInterface = {
          commentId: chosenComment.id,
          fileId: fileId,
          color: chosenComment.color,
          highlightStart: Math.min(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number),
          highlightEnd: Math.max(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number)
        }
        const response = await createCommentTextWithFilePostgresService(newComment)
        if(response?.status !== 201) return
        let selectedComment:AdvancedEvaluationTextCommentModel = {
          comment: response.data.comment,
          file: response.data.file,
          color: response.data.color,
          highlightStart: response.data.highlightStart,
          highlightEnd: response.data.highlightEnd,
          id: response.data.id
        }
        const newComments: AdvancedEvaluationTextCommentModel[] = []
        if (commentsText.length === 0) {
          newComments.push(selectedComment)
        } else {
          for (const oldComment of commentsText) {
            if (oldComment.comment.id === selectedComment.comment.id &&
              selectedComment.highlightStart < oldComment.highlightStart &&
              selectedComment.highlightEnd >= oldComment.highlightStart &&
              selectedComment.highlightStart < oldComment.highlightEnd &&
              selectedComment.highlightEnd < oldComment.highlightEnd){//--SS--OS--SE--OE--
              const updatedComment = {...selectedComment, highlightEnd: oldComment.highlightEnd}
              const deleteResponse = await deleteCommentTextPostgresService(oldComment.id.toString())
              if(deleteResponse?.status === 200){
                const updateResponse = await changeCommentTextPostgresService(updatedComment)
                if(updateResponse?.status === 200) selectedComment = updatedComment
              }
              else{
                newComments.push(oldComment)
              }
            }
            else if (oldComment.comment.id === selectedComment.comment.id &&
              selectedComment.highlightStart > oldComment.highlightStart &&
              selectedComment.highlightEnd > oldComment.highlightStart &&
              selectedComment.highlightStart <= oldComment.highlightEnd &&
              selectedComment.highlightEnd > oldComment.highlightEnd){//--OS--SS--OE--SE--
              const updatedComment = {...selectedComment, highlightStart: oldComment.highlightStart}
              const deleteResponse = await deleteCommentTextPostgresService(oldComment.id.toString())
              if(deleteResponse?.status === 200){
                const updateResponse = await changeCommentTextPostgresService(updatedComment)
                if(updateResponse?.status === 200) selectedComment = updatedComment
              }
              else{
                newComments.push(oldComment)
              }
            }
            else if((selectedComment.highlightStart < oldComment.highlightStart &&
                selectedComment.highlightEnd < oldComment.highlightStart) //--SS--SE--OS--OE--
              ||
              (selectedComment.highlightStart > oldComment.highlightStart &&
                selectedComment.highlightEnd > oldComment.highlightEnd)){//--OS--OE--SS--SE--
              newComments.push(oldComment)
            }
            else if (selectedComment.highlightStart < oldComment.highlightStart &&
              selectedComment.highlightEnd >= oldComment.highlightStart &&
              selectedComment.highlightStart < oldComment.highlightEnd &&
              selectedComment.highlightEnd < oldComment.highlightEnd){//--SS--OS--SE--OE--
              const updatedComment = {...oldComment, highlightStart: selectedComment.highlightEnd+1}
              if(await checkNewRanges(updatedComment)) newComments.push(updatedComment)
              else newComments.push(oldComment)
            }
            else if ((selectedComment.highlightStart === oldComment.highlightStart &&
                selectedComment.highlightEnd === oldComment.highlightEnd)//EQUAL
              ||
              (selectedComment.highlightStart > oldComment.highlightStart &&
                selectedComment.highlightStart < oldComment.highlightEnd &&
                selectedComment.highlightEnd > oldComment.highlightStart &&
                selectedComment.highlightEnd < oldComment.highlightEnd)//--OS--SS--SE--OE--
              ||
              (selectedComment.highlightStart < oldComment.highlightStart &&
                selectedComment.highlightStart < selectedComment.highlightEnd &&
                selectedComment.highlightEnd > oldComment.highlightStart &&
                selectedComment.highlightEnd > oldComment.highlightStart)){//--SS--OS--OE--SE--
              const response = await deleteCommentTextPostgresService(oldComment.id.toString())
              if(response?.status !== 200) newComments.push(oldComment)
            }
            else if (selectedComment.highlightStart>oldComment.highlightStart &&
              selectedComment.highlightEnd > oldComment.highlightStart &&
              selectedComment.highlightStart <= oldComment.highlightEnd &&
              selectedComment.highlightEnd > oldComment.highlightEnd){//--OS--SS--OE--SE--
              const updatedComment = {...oldComment, highlightEnd: selectedComment.highlightStart -1}
              if(await checkNewRanges(updatedComment)) newComments.push(updatedComment)
              else newComments.push(oldComment)
            }
            else newComments.push(oldComment)
          }
          newComments.push(selectedComment)
        }
        selection?.removeAllRanges()
        setCommentsText(newComments)
        setSortButton("lastUsedDate,desc")
        setRefreshRightPanelUserComments(prevState => !prevState)
      }
    }catch (error){
      console.log(error)
    }
  }
  useEffect(() => {
    setLetterColor(toLetterArray(comments))
  }, [fileText, comments])
  useUpdateEffect(()=>{
    const handleCommentTextDeletion = async (deletedCommentId:number) => {
      if(setCommentsText === undefined || fileId === undefined) return
      try {
        const response = await deleteCommentTextByCommentFilePostgresService(deletedCommentId.toString(), fileId.toString())
        if(response?.status === 200){
          const textCommentsTemp = commentsText.filter(commentText=> !(commentText.comment.id === deletedCommentId))
          setCommentsText(textCommentsTemp)
        }
      }catch (error){
        console.log(error)
      }
    }
    if(deletedCommentId !== undefined && setDeletedCommentId !== undefined)
      handleCommentTextDeletion(deletedCommentId)
        .then(() => setDeletedCommentId(undefined))
  },[deletedCommentId])
  useUpdateEffect(()=>{
    const updateTextList = async (updatedComment:CommentInterface) => {
      if(setCommentsText === undefined) return
      const textCommentsTemp = await Promise.all(commentsText.map(async textComment => {
        if(textComment.comment.id === updatedComment.id){
          try {
            const updatedCommentExtended = {...textComment, color: updatedComment.color} as AdvancedEvaluationTextCommentModel
            const response = await changeCommentTextPostgresService(updatedCommentExtended)
            if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
              return updatedCommentExtended
            }
            else {
              return textComment
            }
          }catch (error){
            console.log(error)
            return textComment
          }
        }
        return textComment
      }))
      setCommentsText(textCommentsTemp)
    }
    if(updatedComment !== undefined && setUpdatedComment !== undefined)
      updateTextList(updatedComment)
        .then(() => setUpdatedComment(undefined))
  },[updatedComment])

  return <div style={{width: width !== undefined ? width : "100%", height: height !== undefined ? height:"100%"}}>
    {printFormattedText()}
  </div>
}