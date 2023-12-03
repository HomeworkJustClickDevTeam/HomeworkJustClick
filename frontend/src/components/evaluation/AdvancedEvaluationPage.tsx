import React, { MutableRefObject, useCallback, useEffect, useLayoutEffect, useRef, useState } from "react"
import { Link, useLocation, useNavigate } from "react-router-dom"
import { useGetFile } from "../customHooks/useGetFile"
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
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface";
import {AdvancedEvaluationCheckedCommentPanel} from "./AdvancedEvaluationCheckedCommentPanel";



export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const [solutionExtended, setSolutionExtended] = useState<SolutionExtendedInterface>(state.solutionExtended)
  const userState = useAppSelector(selectUserState)
  const [refreshRightPanelUserComments, setRefreshRightPanelUserComments] = useState(false)
  const [sortButton, setSortButton] = useState<SortButtonStateType>("lastUsedDate,desc")
  const file = useGetFile(solutionExtended.id, 'solution')
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUserAndAssignment(userState!.id, solutionExtended.assignment.id, `size=10&sort=${sortButton}`, refreshRightPanelUserComments)
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()
  const {commentsImage, setCommentsImage} = useGetCommentsImageByFile(file?.postgresId, "")
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
      return () => URL.revokeObjectURL(imageTemp.src)
    }
  }, [file])
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
      <div id={"advancedEvaluationPageDiv"}>
        {solutionExtended.assignment.groupId !== undefined && userState?.id !== undefined &&
        <div style={{width: "100%"}} id={"backButtonDiv"}>
          <Link to = {`/group/${solutionExtended.assignment.groupId}/solution/${solutionExtended.user.id}/${solutionExtended.assignment.id}`} state={{solution: solutionExtended}}>Wróć</Link>
        </div>}
        {userState!.role === "Teacher" ?
          <AdvancedEvaluationCommentPanel
            setDeletedRightPanelCommentId={setDeletedRightPanelCommentId}
            setUpdatedComment={setUpdatedComment}
            assignmentId={solutionExtended.assignment.id}
            setSortButtonState={setSortButton}
            sortButtonState={sortButton}
            chosenCommentId={chosenComment?.id}
            height={availableHeight}
            highlightedCommentId={highlightedCommentId}
            setChosenComment={setChosenComment}
            setRightPanelUserComments={setRightPanelUserComments}
            rightPanelUserComments={rightPanelUserComments}></AdvancedEvaluationCommentPanel>
        :
          <AdvancedEvaluationCheckedCommentPanel
            rightPanelUserComments={rightPanelUserComments}
            highlightedCommentId={highlightedCommentId}
            height={availableHeight}></AdvancedEvaluationCheckedCommentPanel>}
        {(file.format === "txt"
            ? (<AdvancedEvaluationTextArea
              setUpdatedComment={setUpdatedComment}
              checked={userState!.role === "Student"}
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
                checked={userState!.role === "Student"}
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
                fileId={file.postgresId}
                chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))}
      </div>
    )
  else return <Loading></Loading>
}