import {FaSort} from "react-icons/fa";
import {SortButtonStateType} from "../../types/SortButtonStateType";
import {AdvancedEvaluationCommentPanelListElement} from "./AdvancedEvaluationCommentPanelListElement";
import React from "react";
import {CommentInterface} from "../../types/CommentInterface";

interface AdvancedEvaluationCheckedCommentPanelPropsInterface{
  rightPanelUserComments: CommentInterface[]
  highlightedCommentId: number|undefined
  height:number|undefined
}
export const AdvancedEvaluationCheckedCommentPanel = ({rightPanelUserComments, highlightedCommentId, height}:AdvancedEvaluationCheckedCommentPanelPropsInterface) => {
  return (
    <div id={"commentPanelDiv"} style={{float:"right", height:height !== undefined ? height.toString() : "100%"}}>
      <p className='text-center underline underline-offset-4 mb-4'>KOMENTARZE</p>
        <br/>
        <hr className='mr-8 mb-2'/>
        <div className='overflow-y-scroll mx-4'>{rightPanelUserComments.map((comment) => {
          return(
            <div key={comment.id} >
              <AdvancedEvaluationCommentPanelListElement
                comment={comment}
                commentId={comment.id}
                highlightedCommentId={highlightedCommentId}></AdvancedEvaluationCommentPanelListElement>
            </div>)}
        )}</div>
    </div>)
}