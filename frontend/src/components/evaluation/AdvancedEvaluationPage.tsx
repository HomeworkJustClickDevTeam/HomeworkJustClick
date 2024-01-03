import React, { MutableRefObject, useCallback, useEffect, useLayoutEffect, useRef, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
import { useGetFile } from "../customHooks/useGetFile"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextArea } from "./AdvancedEvaluationTextArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationImageArea } from "./AdvancedEvaluationImageArea"
import Loading from "../animations/Loading"
import { AdvancedEvaluationImageCommentModel } from "../../types/AdvancedEvaluationImageComment.model"
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
import { AdvancedEvaluationTextCommentModel } from "../../types/AdvancedEvaluationTextComment.model"
import { useGetCommentsTextByFile } from "../customHooks/useGetCommentsTextByFile"
import { selectGroup } from "../../redux/groupSlice"
import { SortCommentsButtonStateType } from "../../types/SortCommentsButtonStateType"
import { useUpdateEffect } from "usehooks-ts"
import {useGetCommentsByUserAndAssignment} from "../customHooks/useGetCommentsByUserAndAssignment";
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface";
import {useDetermineFileType} from "../customHooks/useDetermineFileType";



export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const [solutionExtended, setSolutionExtended] = useState<SolutionExtendedInterface>(state.solutionExtended)
  const userState = useAppSelector(selectUserState)
  const [refreshRightPanelUserComments, setRefreshRightPanelUserComments] = useState(false)
  const [sortButton, setSortButton] = useState<SortCommentsButtonStateType>("lastUsedDate,desc")
  const file = useGetFile(solutionExtended.id, 'solution')
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUserAndAssignment(userState!.id, solutionExtended.assignment.id, `size=10&sort=${sortButton}`, refreshRightPanelUserComments)
  const {availableHeight, availableWidth, onBackButtonRefChange, onCommentPanelListRefChange} = useGetSolutionAreaSizeAvailable()
  const {commentsImage, setCommentsImage} = useGetCommentsImageByFile(file?.id, "")
  const {comments: commentsText, setComments: setCommentsText} = useGetCommentsTextByFile(file?.id, "")
  const [highlightedCommentId, setHighlightedCommentId] = useState<number|undefined>(undefined)
  const [deletedRightPanelCommentId, setDeletedRightPanelCommentId] = useState<number|undefined>(undefined)
  const [updatedComment, setUpdatedComment] = useState<CommentInterface|undefined>(undefined)
  const {image, fileText} = useDetermineFileType(file)

  const handleCommentHighlighting = (commentId:number) => {
      setHighlightedCommentId(commentId)
  }

  useEffect(() => {
    setSolutionExtended(state.solutionExtended)
  }, [state]);
  useUpdateEffect(()=>{
    if(highlightedCommentId !== undefined){
      const timeoutId = setTimeout(()=> setHighlightedCommentId(undefined), 2000)
      return () => clearTimeout(timeoutId)
    }
  }, [highlightedCommentId])

  if(file)
    return (
      <div>
        {solutionExtended.assignment.groupId !== undefined && userState?.id !== undefined &&
        <div style={{width: "100%"}}  ref={onBackButtonRefChange}>
          <Link to = {`/group/${solutionExtended.assignment.groupId}/solution/${solutionExtended.user.id}/${solutionExtended.assignment.id}`} state={{solution: solutionExtended}}>Wróć</Link>
        </div>}
          <AdvancedEvaluationCommentPanel
            onCommentPanelListRefChange={onCommentPanelListRefChange}
            setDeletedRightPanelCommentId={setDeletedRightPanelCommentId}
            setUpdatedComment={setUpdatedComment}
            assignmentId={solutionExtended.assignment.id}
            setSortButtonState={setSortButton}
            sortButtonState={sortButton}
            chosenCommentId={chosenComment?.id}
            highlightedCommentId={highlightedCommentId}
            setChosenComment={setChosenComment}
            setRightPanelUserComments={setRightPanelUserComments}
            rightPanelUserComments={rightPanelUserComments}></AdvancedEvaluationCommentPanel>
        {(fileText !== undefined
            ? (<AdvancedEvaluationTextArea
              setUpdatedComment={setUpdatedComment}
              setCommentsText={setCommentsText}
              setDeletedCommentId={setDeletedRightPanelCommentId}
              setRefreshRightPanelUserComments={setRefreshRightPanelUserComments}
              setSortButton={setSortButton}
              commentsText={commentsText}
              fileId={file.id}
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
                commentsImage={commentsImage}
                setCommentsImage={setCommentsImage}
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
                file={file}
                chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))}
      </div>
    )
  else return <Loading></Loading>
}