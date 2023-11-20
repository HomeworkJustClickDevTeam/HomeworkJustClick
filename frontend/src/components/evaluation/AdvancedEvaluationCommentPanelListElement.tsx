import React, { ChangeEvent, useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error
import { useTimeout } from "../customHooks/useTimeout"

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
  const {reset: resetColorPickerTimer} = useTimeout(() => updateComment(), 250)

  const updateComment = () => {
    changeCommentPostgresService(commentState)
      .then(()=>{
        updateCommentsLists(commentState)
      })
      .catch((error)=> console.log(error))
  }
  const handleColorPickerBehaviour = (event:React.ChangeEvent<HTMLInputElement>) => {
    setCommentState((prevState) => ({...prevState, description:event.target.value}))
    resetColorPickerTimer()
  }
  const handleCommentChoice = (event:React.MouseEvent<HTMLDivElement>) => {
    if((event.target as Element).id !== "commentListElementDiv")
      return
    event.stopPropagation()
    event.preventDefault()
    if(chosenCommentId === comment.id) {
      setChosenComment(undefined)
    }
    else {
      setChosenComment(commentState)
    }
  }
  const handleEditButtonBehaviour = () => {
    if(!commentReadonly){
      updateComment()
    }
    setCommentReadonly(prevState => !prevState)
  }

  return(
  <div style={
    {backgroundColor: highlightedCommentId === comment.id ? comment.color+"80" : "white",
      border:chosenCommentId === comment.id ? "4px solid #6D8DFF" : "none"}}
      onClick={(event)=>handleCommentChoice(event)}
    id={"commentListElementDiv"}>
    <div>
      <input
             onChange={(event) => handleColorPickerBehaviour(event)}
             id={"commentDescription"}
             key={commentState.id}
             readOnly={commentReadonly}
             defaultValue={commentState.description}></input>
      <button id={"editButton"} onClick={() => handleEditButtonBehaviour()}>
        {commentReadonly ? "Edytuj kometarz":"Ok"}
      </button>
      <input
        id={"colorPicker"}
        value={commentState.color}
        onChange={(event) =>{
          setCommentState((prevState)=>({...prevState, color:event.target.value}))}
        }
        type={"color"}/>
    </div><br/>
    <button onClick={() => handleCommentRemoval(commentState)} type={"button"}>Usuń komentarz</button>
    <hr/>
  </div>)
}