import React, { ChangeEvent, useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import { useTimeout } from "../customHooks/useTimeout"
import { FaEdit } from "react-icons/fa";
import { MdDelete } from "react-icons/md";


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
      <div className="pb-1 select-none" onClick={(event)=>handleCommentChoice(event)}>
  <div style={{backgroundColor: highlightedCommentId === comment.id ? comment.color+"80" : "white",
      border:chosenCommentId === comment.id ? "4px solid #6D8DFF" : "none",  margin: "4px 0px 0px 0px", padding: "5px 4px"}
  }>
    <div>
        <div className='mb-3 flex'>
          <textarea className='italic underline underline-offset-2 max-h-[75px] min-h-[36px] pr-2'
                onChange={(event) => {!commentReadonly &&
                  setCommentState((prevState) => ({...prevState, description:event.target.value}))}
                }
                 id={"commentDescription"}
                 key={commentState.id}
                 readOnly={commentReadonly}
                 defaultValue={commentState.description}></textarea>
          <button className=' text-main_blue underline text-sm flex' id={"editButton"} onClick={() => handleEditButtonBehaviour()}>
              <FaEdit className='mt-1 mr-[2px] ml-2'/>
            {commentReadonly ? "Edytuj":"Ok"}

          </button>
        </div>
      <form
        onSubmit={(event) => {
          event.preventDefault()
          updateCommentsLists(commentState)}} className='flex pt-1'>
          <p className='mb-2 h-full'>Kolor:</p>
        <input
          id={"colorPicker"}
          value={commentState.color}
          onChange={(event) =>{
            setCommentState((prevState)=>({...prevState, color:event.target.value}))}
          }
          type={"color"}/>
        <button type={"submit"} className='bg-main_blue text-white rounded-md text-sm p-1 ml-2'>Kolor</button>
      </form>
    </div><br/>
    <button className='text-berry_red ml-auto text-center flex mr-4' onClick={() => handleCommentRemoval(commentState)} type={"button"}>
        <MdDelete className='text-berry_red mt-[4.5px]' />Usuń</button>

  </div>
        <hr className='mt-2'/>
      </div>

)
}