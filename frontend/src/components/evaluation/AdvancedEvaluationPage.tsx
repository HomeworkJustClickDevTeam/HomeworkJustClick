import React, { MutableRefObject, useCallback, useEffect, useLayoutEffect, useRef, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
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
  changeCommentPostgresService,
  changeCommentTextPostgresService,
  createCommentTextWithFilePostgresService,
  deleteCommentImageByCommentFilePostgresService,
  deleteCommentImagePostgresService,
  deleteCommentPostgresService,
  deleteCommentTextByCommentFilePostgresService,
  deleteCommentTextPostgresService
} from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error
import { retry } from "@reduxjs/toolkit/query"
import { useGetCommentsImageByFile } from "../customHooks/useGetCommentsImageByFile"
import { AdvancedEvaluationTextCommentCreateInterface } from "../../types/AdvancedEvaluationTextCommentCreateInterface"
import { AdvancedEvaluationTextCommentInterface } from "../../types/AdvancedEvaluationTextCommentInterface"
import { useGetCommentsTextByFile } from "../customHooks/useGetCommentsTextByFile"
import { selectGroup } from "../../redux/groupSlice"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const group = useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const file = useGetFiles(state.solutionExtended.id, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const drawnCommentsRef = useRef<AdvancedEvaluationImageCommentInterface[]>([])
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUser(userState?.id, "")
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()
  const commentsImageState = useGetCommentsImageByFile(file?.postgresId, "")
  const advancedEvaluationImageAreaRef = useRef<any>()
  const {comments: commentsTextState, setComments: setCommentsTextState} = useGetCommentsTextByFile(file?.postgresId, "?page=0&size=50")
  const [highlightedCommentId, setHighlightedCommentId] = useState<number|undefined>(undefined)
  const navigate = useNavigate()
  const handleCommentHighlighting = (commentId:number) => {
    if(highlightedCommentId === undefined){
      setHighlightedCommentId(commentId)
      setTimeout(() => {
        setHighlightedCommentId(undefined)
      }, 2000)
    }
  }
  const handleNewCommentTextCreation =  async () =>{
    if(chosenComment === undefined) return
    const checkNewRanges = async (updatedComment:AdvancedEvaluationTextCommentInterface) => {
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
          fileId: file.postgresId,
          color: chosenComment.color,
          highlightStart: Math.min(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number),
          highlightEnd: Math.max(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number)
        }
        const response = await createCommentTextWithFilePostgresService(newComment)
        if(response?.status !== 201) return
        let selectedComment:AdvancedEvaluationTextCommentInterface = {
          commentId: response.data.comment.id,
          fileId: response.data.file.id,
          color: response.data.color,
          highlightStart: response.data.highlightStart,
          highlightEnd: response.data.highlightEnd,
          id: response.data.id
        }
        const newComments: AdvancedEvaluationTextCommentInterface[] = []
        if (commentsTextState.length === 0) {
          newComments.push(selectedComment)
        } else {
          for(const oldComment of commentsTextState) {
            if (oldComment.commentId === selectedComment.commentId &&
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
            else if (oldComment.commentId === selectedComment.commentId &&
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
        }
        newComments.push(selectedComment)
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
        const drawnCommentsTemp = await Promise.all(drawnCommentsRef.current.map(async (commentImage) => {
          if(commentImage.commentId === clickedComment.id){
            try {
              const updatedComment = { ...commentImage, color: clickedComment.color } as AdvancedEvaluationImageCommentInterface
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
      try {
        const response = await deleteCommentTextByCommentFilePostgresService(comment.id.toString(), file.postgresId.toString())
        if(response?.status === 200){
          const textCommentsTemp = commentsTextState.filter(commentText=> !(commentText.commentId === comment.id))
          setCommentsTextState(textCommentsTemp)
        }
      }catch (error){
        console.log(error)
      }
    }
    const commentImageRemoval = async () => {
      if(drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null && file.format !== "txt" && advancedEvaluationImageAreaRef?.current !== undefined && advancedEvaluationImageAreaRef?.current!==null) {
        try {
          const response = await deleteCommentImageByCommentFilePostgresService(comment.id.toString(), file.postgresId.toString())
          if(response?.status === 200){
            const drawnCommentsTemp = drawnCommentsRef.current.filter( commentImage => !(commentImage.commentId === comment.id))
            drawnCommentsRef.current = drawnCommentsTemp
            advancedEvaluationImageAreaRef.current.drawOnCanvas()
          }
        }catch (error){
          console.log(error)
        }
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
    if(commentsImageState !== undefined && commentsImageState !== null && advancedEvaluationImageAreaRef?.current !== undefined && advancedEvaluationImageAreaRef?.current !== null){
      drawnCommentsRef.current = commentsImageState
      advancedEvaluationImageAreaRef.current.drawOnCanvas()
    }
  }, [commentsImageState])
  if(file)
    return (
      <div id={"advancedEvaluationPageDiv"}>
        {group?.id !== undefined && userState?.id !== undefined &&
        <div style={{width: "100%"}} id={"backButtonDiv"}>
          <Link to = {`/group/${state.solutionExtended.assignment.groupId}/solution/${state.solutionExtended.user.id}/${state.solutionExtended.assignment.id}`} state={{solution: state.solutionExtended}}>Wróć</Link>
        </div>}
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
              chosenComment={chosenComment}
              handleCommentHighlighting={handleCommentHighlighting}
              handleNewCommentTextCreation={handleNewCommentTextCreation}
              setComments={setCommentsTextState}
              comments={commentsTextState}
              width = {availableWidth}
              height = {availableHeight}
              fileText={fileText}/>)
            : (image !== undefined && drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null ?
              <AdvancedEvaluationImageArea
                ref={advancedEvaluationImageAreaRef}
                handleCommentHighlighting={handleCommentHighlighting}
                width = {availableWidth}
                height = {availableHeight}
                editable={true}
                drawnComments={drawnCommentsRef as MutableRefObject<AdvancedEvaluationImageCommentInterface[]>}
                image={image}
                fileId={file.postgresId}
                chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))}
      </div>
    )
  else return <Loading></Loading>
}