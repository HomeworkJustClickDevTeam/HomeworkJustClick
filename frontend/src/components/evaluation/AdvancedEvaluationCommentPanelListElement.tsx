import React, { ChangeEvent, useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error

export const AdvancedEvaluationCommentPanelListElement = (
  {highlightedCommentId, chosenCommentId, updateCommentsLists, setChosenComment, comment, handleCommentRemoval}:{
  setChosenComment: (comment:CommentInterface|undefined)=>void,
  updateCommentsLists: (comment: CommentInterface) => void,
  comment:CommentInterface,
  fileType:"txt"|"img",
  handleCommentRemoval: (commentToBeRemoved:CommentInterface)=>void
  chosenCommentId:number|undefined
  highlightedCommentId:number|undefined
})=>{
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const [commentReadonly, setCommentReadonly] = useState(true)

  const handleCommentChoice = () => {
    if(chosenCommentId === comment.id) {
      setChosenComment(undefined)
    }
    else {
      setChosenComment(commentState)
    }
  }
  const handleEditButtonBehaviour = async () => {
    if(!commentReadonly){
      changeCommentPostgresService(commentState)
        .then(()=>{
          updateCommentsLists(commentState)
        })
        .catch((error)=> console.log(error))
    }
    setCommentReadonly(prevState => !prevState)
  }

  return(
  <div style={
    {backgroundColor: highlightedCommentId === comment.id ? comment.color+"80" : "white",
      border:chosenCommentId === comment.id ? "4px solid #6D8DFF" : "none"}}
      onClick={()=>handleCommentChoice()}>
    <div>
      <input
            onChange={(event) => {!commentReadonly &&
              setCommentState((prevState) => ({...prevState, description:event.target.value}))}
            }
             id={"commentDescription"}
             key={commentState.id}
             readOnly={commentReadonly}
             defaultValue={commentState.description}></input>
      <button id={"editButton"} onClick={() => handleEditButtonBehaviour()}>
        {commentReadonly ? "Edytuj kometarz":"Ok"}
      </button>
      <form
        onSubmit={(event) => {
          event.preventDefault()
          updateCommentsLists(commentState)}}>
        <input
          id={"colorPicker"}
          value={commentState.color}
          onChange={(event) =>{
            setCommentState((prevState)=>({...prevState, color:event.target.value}))}
          }
          type={"color"}/>
        <button type={"submit"}>Potwierdz zmiane koloru</button>
      </form>

    </div><br/>
    <button onClick={() => handleCommentRemoval(commentState)} type={"button"}>Usuń komentarz</button>
    <hr/>
  </div>)
}