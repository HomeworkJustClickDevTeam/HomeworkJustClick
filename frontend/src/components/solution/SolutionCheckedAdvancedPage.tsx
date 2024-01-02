import {Link, useLocation} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {SolutionExtendedInterface} from "../../types/SolutionExtendedInterface";
import {useGetFile} from "../customHooks/useGetFile";
import {useDetermineFileType} from "../customHooks/useDetermineFileType";
import {useGetCommentsImageByFile} from "../customHooks/useGetCommentsImageByFile";
import {useGetCommentsTextByFile} from "../customHooks/useGetCommentsTextByFile";
import {useGetSolutionAreaSizeAvailable} from "../customHooks/useGetSolutionAreaSizeAvailable";
import {AdvancedEvaluationTextCommentModel} from "../../types/AdvancedEvaluationTextComment.model";
import {AdvancedEvaluationImageCommentModel} from "../../types/AdvancedEvaluationImageComment.model";
import {AdvancedEvaluationCommentPanelListElement} from "../evaluation/AdvancedEvaluationCommentPanelListElement";
import {AdvancedEvaluationImageArea} from "../evaluation/AdvancedEvaluationImageArea";
import {useUpdateEffect} from "usehooks-ts";
import {AdvancedEvaluationTextArea} from "../evaluation/AdvancedEvaluationTextArea";



export const SolutionCheckedAdvancedPage = ()=>{
  const {state} = useLocation()
  const [solutionExtended, setSolutionExtended] = useState<SolutionExtendedInterface>(state.solutionExtended)
  const solutionFile = useGetFile(solutionExtended.id, "solution")
  const {image, fileText} = useDetermineFileType(solutionFile)
  const {commentsImage, setCommentsImage} = useGetCommentsImageByFile(solutionFile?.id, "")
  const {comments: commentsText, setComments: setCommentsText} = useGetCommentsTextByFile(solutionFile?.id, "")
  const [highlightedCommentId, setHighlightedCommentId] = useState<number|undefined>(undefined)
  const {availableHeight, availableWidth, onCommentPanelListRefChange, onBackButtonRefChange} = useGetSolutionAreaSizeAvailable()

  const handleCommentHighlighting = (commentId:number) => {
    setHighlightedCommentId(commentId)
  }
  useUpdateEffect(()=>{
    if(highlightedCommentId !== undefined){
      const timeoutId = setTimeout(()=> setHighlightedCommentId(undefined), 2000)
      return () => clearTimeout(timeoutId)
    }
  }, [highlightedCommentId])
  const showRightPanelComments = (commentsList:AdvancedEvaluationTextCommentModel[]|AdvancedEvaluationImageCommentModel[]) => {
    const commentListWithoutDuplicates = [...new Map(commentsList.map(commentAdv =>
      [commentAdv.comment['id'], commentAdv])).values()]

    return (<div >{commentListWithoutDuplicates.map((drawnComment)=> {

      return <div ref={onCommentPanelListRefChange} key={drawnComment.id}>
        <AdvancedEvaluationCommentPanelListElement highlightedCommentId={highlightedCommentId} comment={drawnComment.comment} commentId={drawnComment.comment.id}></AdvancedEvaluationCommentPanelListElement>
      </div>})}
    </div>)
  }

  useUpdateEffect(()=>{
    if(highlightedCommentId !== undefined){
      const timeoutId = setTimeout(()=> setHighlightedCommentId(undefined), 2000)
      return () => clearTimeout(timeoutId)
    }
  }, [highlightedCommentId])
  useEffect(() => {
    setSolutionExtended(state.solutionExtended)
  }, [state]);
  return(<div>
      <div style={{width: "100%"}} ref={onBackButtonRefChange}>
        <Link to = {`/group/${solutionExtended.assignment.groupId}/assignment/${solutionExtended.assignment.id}`}>Wróć</Link>
      </div>
      <div style={{float:"right", height:"100vh"}}>
        Komentarze:<br/>
        {image !== undefined ?  showRightPanelComments(commentsImage)
        :
        fileText !== undefined && showRightPanelComments(commentsText)}
      </div>
    <div>
      {image !== undefined ?  <AdvancedEvaluationImageArea width={availableWidth} height={availableHeight} image={image} commentsImage={commentsImage} handleCommentHighlighting={handleCommentHighlighting}></AdvancedEvaluationImageArea>
        :
        fileText !== undefined && <AdvancedEvaluationTextArea handleCommentHighlighting={handleCommentHighlighting} width={availableWidth} height={availableHeight} comments={commentsText} fileText={fileText} commentsText={commentsText}/>}
    </div>
    </div>)
}