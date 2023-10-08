import { CommentInterface } from "../../types/CommentInterface"

export const AdvancedEvaluationCommentPanel = (
  {handleActiveCommentChange}:{handleActiveCommentChange:(comment:CommentInterface) => void}) => {
  const comments: CommentInterface[] = [
    {color:'red', id:0, description: "tutaj komentarz"},
    {color:'CornflowerBlue', id:1, description: "tutaj inny komentraz"},
    {color:'yellow', id:2, description: "ostatni komentarz"}]
  return <>
    Panel komentarzy <br/>
    Komentarze: <br/>
    {comments.map((comment) => {
      return <div key={comment.id}><button type={'button'} onClick={() => handleActiveCommentChange(comment)} style={{backgroundColor: comment.color}}>{comment.description}</button><br/></div>
    })}
    </>
}