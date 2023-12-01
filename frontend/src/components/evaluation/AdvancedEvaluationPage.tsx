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
import { SortButtonStateType } from "../../types/SortButtonStateType"
import { useUpdateEffect } from "usehooks-ts"
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";



export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const group = useAppSelector(selectGroup)
  const userState = useAppSelector(selectUserState)
  const [refreshRightPanelUserComments, setRefreshRightPanelUserComments] = useState(false)
  const [sortButton, setSortButton] = useState<SortButtonStateType>("lastUsedDate,desc")
  const file = useGetFiles(state.solutionExtended.id, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUserAndAssignment(userState!.id, state.solutionExtended.assignment.id, `size=10&sort=${sortButton}`, refreshRightPanelUserComments)
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()
  const commentsImageState = useGetCommentsImageByFile(file?.postgresId, "")
  const {comments: commentsText, setComments: setCommentsText} = useGetCommentsTextByFile(file?.postgresId, "")
  const [highlightedCommentId, setHighlightedCommentId] = useState<number|undefined>(undefined)
  const [deletedRightPanelCommentId, setDeletedRightPanelCommentId] = useState<number|undefined>(undefined)
  const [updatedComment, setUpdatedComment] = useState<CommentInterface|undefined>(undefined)

  const handleCommentHighlighting = (commentId:number) => {
      setHighlightedCommentId(commentId)
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
  useUpdateEffect(()=>{
    if(highlightedCommentId !== undefined){
      const timeoutId = setTimeout(()=> setHighlightedCommentId(undefined), 2000)
      return () => clearTimeout(timeoutId)
    }
  }, [highlightedCommentId])
  if(file)
    return (
      <div id={"advancedEvaluationPageDiv"}>
        {group?.id !== undefined && userState?.id !== undefined &&
        <div style={{width: "100%"}} id={"backButtonDiv"}>
          <Link to = {`/group/${state.solutionExtended.assignment.groupId}/solution/${state.solutionExtended.user.id}/${state.solutionExtended.assignment.id}`} state={{solution: state.solutionExtended}}>Wróć</Link>
        </div>}
        <AdvancedEvaluationCommentPanel
          setDeletedRightPanelCommentId={setDeletedRightPanelCommentId}
          setUpdatedComment={setUpdatedComment}
          assignmentId={state.solutionExtended.assignment.id}
          setSortButtonState={setSortButton}
          sortButtonState={sortButton}
          chosenCommentId={chosenComment?.id}
          height={availableHeight}
          highlightedCommentId={highlightedCommentId}
          setChosenComment={setChosenComment}
          setRightPanelUserComments={setRightPanelUserComments}
          rightPanelUserComments={rightPanelUserComments}></AdvancedEvaluationCommentPanel>
        {(file.format === "txt"
            ? (<AdvancedEvaluationTextArea
              setUpdatedComment={setUpdatedComment}
              setCommentsText={setCommentsText}
              setDeletedCommentId={setDeletedRightPanelCommentId}
              setRefreshRightPanelUserComments={setRefreshRightPanelUserComments}
              setSortButton={setSortButton}
              commentsText={commentsText}
              fileId={file.postgresId}
              deletedCommentId={deletedRightPanelCommentId}
              updatedComment={updatedComment}
              chosenComment={chosenComment}
              handleCommentHighlighting={handleCommentHighlighting}
              setComments={setCommentsText}
              comments={commentsText}
              width = {availableWidth}
              height = {availableHeight}
              fileText={fileText}/>)
            : (image !== undefined ?
              <AdvancedEvaluationImageArea
                deletedCommentId={deletedRightPanelCommentId}
                setDeletedCommentId={setDeletedRightPanelCommentId}
                setUpdatedComment={setUpdatedComment}
                updatedComment={updatedComment}
                handleCommentHighlighting={handleCommentHighlighting}
                width = {availableWidth}
                height = {availableHeight}
                setRefreshRightPanelUserComments={setRefreshRightPanelUserComments}
                setSortButtonState={setSortButton}
                image={image}
                fileId={file.postgresId}
                chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))}
      </div>
    )
  else return <Loading></Loading>
}