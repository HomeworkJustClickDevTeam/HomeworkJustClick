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
  changeCommentPostgresService,
  deleteCommentImagePostgresService,
  deleteCommentPostgresService
} from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error
import { retry } from "@reduxjs/toolkit/query"
import { useGetCommentsImageByFile } from "../customHooks/useGetCommentsImageByFile"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const userState = useAppSelector(selectUserState)
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const [chosenCommentFrameWidth, setChosenCommentFrameWidth] = useState<number|undefined>(undefined)
  const drawnCommentsRef = useRef<AdvancedEvaluationImageCommentInterface[]|null>(null)
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUser(userState?.id, "")
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()
  const commentsImageState = useGetCommentsImageByFile(file?.postgresId, "")
  const advancedEvaluationImageAreaRef:any = useRef()
  const updateCommentsLists = async (clickedComment:CommentInterface, clickedCommentWidth?:number) => {
    try {
      const response = await changeCommentPostgresService(clickedComment)
      if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
        const newComments = rightPanelUserComments.map(comment => {
          if (comment.id === clickedComment?.id) return clickedComment
          else return comment
        })
        setRightPanelUserComments(newComments)

        if (drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null && clickedCommentWidth !== undefined && advancedEvaluationImageAreaRef.current !== undefined && advancedEvaluationImageAreaRef.current !== null) {
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
    }catch (error){
      console.log(error)
    }
  }
  const handleCommentClick = async (clickedComment:CommentInterface, clickedCommentWidth?:number) => {
    setChosenComment(clickedComment)
    setChosenCommentFrameWidth(clickedCommentWidth)
    await updateCommentsLists(clickedComment, clickedCommentWidth)
  }
  const handleCommentRemoval = async (comment:CommentInterface) => {
    try {
      const response = await deleteCommentPostgresService(comment.id.toString())
      if (response.data !== null && response.data !== undefined && response.status === 200) {
        const userNewComments = rightPanelUserComments.filter(oldComment => oldComment.id !== comment.id)
        setRightPanelUserComments(userNewComments)
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
          height={availableHeight}
          handleCommentClick={handleCommentClick}
          handleCommentRemoval={handleCommentRemoval}
          setRightPanelUserComments={setRightPanelUserComments}
          rightPanelUserComments={rightPanelUserComments}
          fileType={file.format === "txt"? "txt" : "img"}></AdvancedEvaluationCommentPanel>
        {(file.format === "txt"
            ? (<AdvancedEvaluationTextArea
              fileId={file.postgresId}
              chosenComment={chosenComment}
              width = {availableWidth}
              height = {availableHeight}
              fileText={fileText}/>)
            : (image !== undefined && drawnCommentsRef?.current !== undefined && drawnCommentsRef?.current !== null ?
              <AdvancedEvaluationImageArea
                ref={advancedEvaluationImageAreaRef}
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