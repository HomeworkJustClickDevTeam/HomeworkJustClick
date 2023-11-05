import { MutableRefObject, useEffect, useRef, useState } from "react"
import { useLocation } from "react-router-dom"
import { useGetFiles } from "../customHooks/useGetFiles"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextArea } from "./AdvancedEvaluationTextArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationImageArea } from "./AdvancedEvaluationImageArea"
import Loading from "../animations/Loading"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useGetSolutionAreaSizeAvailable } from "../customHooks/useGetSolutionAreaSizeAvailable"
import { useGetCommentsByUser } from "../customHooks/useGetCommentsByUser"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import {
  changeCommentImagePostgresService,
  changeCommentPostgresService, changeCommentTextPostgresService, createCommentTextWithFilePostgresService,
  deleteCommentImagePostgresService,
  deleteCommentPostgresService, deleteCommentTextPostgresService
} from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error
import { retry } from "@reduxjs/toolkit/query"
import { useGetCommentsImageByFile } from "../customHooks/useGetCommentsImageByFile"
import { AdvancedEvaluationTextCommentCreateInterface } from "../../types/AdvancedEvaluationTextCommentCreateInterface"
import { AdvancedEvaluationTextCommentInterface } from "../../types/AdvancedEvaluationTextCommentInterface"
import { useGetCommentsTextByFile } from "../customHooks/useGetCommentsTextByFile"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const userState = useAppSelector(selectUserState)
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const [chosenCommentFrameWidth, setChosenCommentFrameWidth] = useState<number>(5)
  const drawnCommentsRef = useRef<AdvancedEvaluationImageCommentInterface[]|null>(null)
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUser(userState?.id, "")
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()
  const commentsImageState = useGetCommentsImageByFile(file?.postgresId, "")
  const advancedEvaluationImageAreaRef:any = useRef()
  const {comments: commentsTextState, setComments: setCommentsTextState} = useGetCommentsTextByFile(file?.postgresId, "")
  const [highlightedCommentId, setHighlightedCommentId] = useState<number|undefined>(undefined)

  const handleCommentHighlight = (commentId:number) => {
    setHighlightedCommentId(commentId)
    setTimeout(() => {
      setHighlightedCommentId(undefined)
    }, 2000)
  }
  const handleNewCommentTextCreation =  async () =>{
    if(chosenComment === undefined) return
    const checkNewRanges = async (updatedComment:AdvancedEvaluationTextCommentInterface, selectedComment:AdvancedEvaluationTextCommentInterface) => {
      if(updatedComment.highlightStart<=updatedComment.highlightEnd){
        const response = await changeCommentTextPostgresService(updatedComment)
        if(response?.status === 200)
          return [updatedComment, selectedComment]
        else return Promise.reject("something went wrong with changing old comment text")
      }
      else{
        return [selectedComment]
      }
    }
    const selection = window.getSelection()
    try {
      if (selection && selection.rangeCount > 0 && window.getSelection()?.getRangeAt(0).endContainer.parentElement?.id && window.getSelection()?.getRangeAt(0).startContainer.parentElement?.id !== null) {
        const newComment: AdvancedEvaluationTextCommentCreateInterface = {
          commentId: chosenComment.id,
          fileId: file.postgresId,
          color: chosenComment.color,
          highlightStart: Math.min(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number),
          highlightEnd: Math.max(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number)
        }
        const response = await createCommentTextWithFilePostgresService(newComment)
        if(response?.status !== 201) return
        const selectedComment = response.data
        const newComments: AdvancedEvaluationTextCommentInterface[] = []
        if (commentsTextState.length === 0) {
          newComments.push(selectedComment)
        } else {
          await Promise.all(commentsTextState.map( async (oldComment) => {
            if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
              const updatedComment = { ...oldComment, highlightEnd: selectedComment.highlightStart - 1 }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              const updatedComment = {
                ...oldComment,
                highlightStart: selectedComment.highlightEnd + 1
              }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
              newComments.push(selectedComment)
            } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              newComments.push(selectedComment)
            } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
              const updatedComment = { ...oldComment, highlightEnd: oldComment.highlightEnd - 1 }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              const updatedComment = { ...oldComment, highlightStart: oldComment.highlightStart + 1 }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
              const updatedComment = {
                ...oldComment,
                highlightEnd: selectedComment.highlightStart - 1
              }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              const updatedComment = {
                ...oldComment,
                highlightStart: selectedComment.highlightEnd + 1
              }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              const updatedComment = {
                ...oldComment,
                highlightStart: selectedComment.highlightEnd + 1
              }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
              const updatedComment = {
                ...oldComment,
                highlightStart: selectedComment.highlightEnd + 1
              }
              const newRanges = await checkNewRanges(updatedComment, selectedComment)
              newComments.push(...newRanges)
            } else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
              newComments.push(selectedComment)
              console.log(newComments)
              return
            } else {
              newComments.push(selectedComment, oldComment)
            }
          }))
        }
        selection?.removeAllRanges()
        setCommentsTextState(newComments)
      }
    }catch (error){
      console.log(error)
    }
  }
  const updateCommentsLists = async (clickedComment:CommentInterface, clickedCommentWidth?:number) => {
    const updateImageList = async () => {
      if (drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null && clickedCommentWidth !== undefined && advancedEvaluationImageAreaRef.current !== undefined && advancedEvaluationImageAreaRef.current !== null) {
        setChosenCommentFrameWidth(clickedCommentWidth)
        const drawnCommentsTemp = await Promise.all(drawnCommentsRef.current.map(async (commentImage) => {
          if(commentImage.commentId === clickedComment.id){
            try {
              const updatedComment = { ...commentImage, color: clickedComment.color, lineWidth: clickedCommentWidth } as AdvancedEvaluationImageCommentInterface
              const response = await changeCommentImagePostgresService(updatedComment)
              if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
                return updatedComment
              }
              else {
                return commentImage
              }
            }catch (error){
              console.log(error)
              return commentImage
            }
          }
          return commentImage
        }))
        drawnCommentsRef.current = drawnCommentsTemp
        advancedEvaluationImageAreaRef.current.drawOnCanvas()
      }
    }
    const updateTextList = async () => {
      const textCommentsTemp = await Promise.all(commentsTextState.map(async textComment => {
        if(textComment.commentId === clickedComment.id){
          try {
            const updatedComment = {...textComment, color: clickedComment.color} as AdvancedEvaluationTextCommentInterface
            const response = await changeCommentTextPostgresService(updatedComment)
            if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
              return updatedComment
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
      setCommentsTextState(textCommentsTemp)
    }

    try {
      const response = await changeCommentPostgresService(clickedComment)
      if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
        const newComments = rightPanelUserComments.map(comment => {
          if (comment.id === clickedComment?.id) return clickedComment
          else return comment
        })
        setRightPanelUserComments(newComments)
        if(file.format !== "txt")
          await updateImageList()
        else
          await updateTextList()
      }
    }catch (error){
      console.log(error)
    }
  }
  const handleCommentRemoval = async (comment:CommentInterface) => {
    const commentTextRemoval = async () => {
      const textCommentsTemp = await Promise.all(commentsTextState.filter(async (commentText)=> {
        if(commentText.commentId === comment.id){
          try {
            const response = await deleteCommentTextPostgresService(commentText.id.toString())
            return !(response !== null && response !== undefined && response.status === 200) //if request went okay remove comment - filter false
          }catch (error){
            console.log(error)
            return true
          }
        }
        return true
      }))
      setCommentsTextState(textCommentsTemp)
    }
    const commentImageRemoval = async () => {
      if(drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null && file.format !== "txt" && advancedEvaluationImageAreaRef.current !== undefined && advancedEvaluationImageAreaRef.current!==null) {
        const drawnCommentsTemp = await Promise.all(drawnCommentsRef.current.filter(async commentImage => {
          if (commentImage.commentId === comment.id) {
            try {
              const response = await deleteCommentImagePostgresService(commentImage.id.toString())
              return !(response !== null && response !== undefined && response.status === 200) //if request went okay remove comment - filter false
            } catch (error) {
              console.log(error)
              return true
            }
          }
          return true
        }))
        drawnCommentsRef.current = drawnCommentsTemp
        advancedEvaluationImageAreaRef.current.drawOnCanvas()
      }
    }
    try {
      const response = await deleteCommentPostgresService(comment.id.toString())
      if (response.data !== null && response.data !== undefined && response.status === 200) {
        const userNewComments = rightPanelUserComments.filter(oldComment => oldComment.id !== comment.id)
        setRightPanelUserComments(userNewComments)
        if(file.format !== "txt")
          await commentImageRemoval()
        else
          await commentTextRemoval()
      }
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    if(file && file.format !== "jpg" && file.format !== "png"){
      file.data.text()
        .then((text) => {
          setFileText(text)})
    }
    else if(file){
      let imageTemp = document.createElement("img")
      imageTemp.src = URL.createObjectURL(file.data)
      imageTemp.onload = () => {
        setImage(imageTemp)
      }
    }
  }, [file])

  useEffect(() => {
    commentsImageState !== undefined && commentsImageState !== null &&
      (drawnCommentsRef.current = commentsImageState)
    advancedEvaluationImageAreaRef?.current !== undefined && advancedEvaluationImageAreaRef?.current !== null &&
      advancedEvaluationImageAreaRef.current.drawOnCanvas()
  }, [commentsImageState])
  if(file)
    return (
      <div id={"advancedEvaluationPageDiv"}>
        <AdvancedEvaluationCommentPanel
          chosenCommentId={chosenComment?.id}
          height={availableHeight}
          highlightedCommentId={highlightedCommentId}
          setChosenComment={setChosenComment}
          updateCommentsLists={updateCommentsLists}
          handleCommentRemoval={handleCommentRemoval}
          setRightPanelUserComments={setRightPanelUserComments}
          rightPanelUserComments={rightPanelUserComments}
          fileType={file.format === "txt"? "txt" : "img"}></AdvancedEvaluationCommentPanel>
        {(file.format === "txt"
            ? (<AdvancedEvaluationTextArea
              editable={true}
              handleNewCommentTextCreation={handleNewCommentTextCreation}
              setComments={setCommentsTextState}
              comments={commentsTextState}
              width = {availableWidth}
              height = {availableHeight}
              fileText={fileText}/>)
            : (image !== undefined && drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null ?
              <AdvancedEvaluationImageArea
                ref={advancedEvaluationImageAreaRef}
                handleCommentHighlight={handleCommentHighlight}
                width = {availableWidth}
                height = {availableHeight}
                editable={true}
                drawnComments={drawnCommentsRef as MutableRefObject<AdvancedEvaluationImageCommentInterface[]>}
                chosenCommentFrameWidth={chosenCommentFrameWidth}
                image={image}
                fileId={file.postgresId}
                chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))}
      </div>
    )
  else return <Loading></Loading>
}