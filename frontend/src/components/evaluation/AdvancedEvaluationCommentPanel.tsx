import { CommentInterface } from "../../types/CommentInterface"
import React from "react"

export const AdvancedEvaluationCommentPanel = (
  {setChosenComment, chosenComment}:{setChosenComment:(comment:CommentInterface|undefined) => void, chosenComment:CommentInterface|undefined}) => {

  const handleCommentClick = (clickedComment:CommentInterface) => {
    if(chosenComment?.id === clickedComment.id){
      setChosenComment(undefined)
    }
    else{
      setChosenComment(clickedComment)
    }
  }
  const comments: CommentInterface[] = [
    {color:'red', id:0, description: "tutaj komentarz"},
    {color:'CornflowerBlue', id:1, description: "tutaj inny komentraz"},
    {color:'yellow', id:2, description: "ostatni komentarz"}]
  return <>
    Panel komentarzy <br/>
    Komentarze: <br/>
    {comments.map((comment) => {
      return <div key={comment.id}><button type={'button'} onClick={() => handleCommentClick(comment)} style={{backgroundColor: comment.color}}>{comment.description}</button><br/></div>
    })}
    </>
}