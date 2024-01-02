import React, { useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { FaEdit } from "react-icons/fa"
import { MdDelete } from "react-icons/md"
import { useTimeout, useUpdateEffect } from "usehooks-ts"
import { delay } from "@reduxjs/toolkit/dist/utils"

interface AdvancedEvaluationCommentPanelListElementPropsInterface{
  setChosenComment?: React.Dispatch<React.SetStateAction<CommentInterface|undefined>>,
  updateCommentsList?: (comment: CommentInterface) => void,
  comment: CommentInterface,
  handleCommentRemoval?: (commentToBeRemovedId: number) => void
  chosenCommentId?: number | undefined
  highlightedCommentId?: number | undefined
  commentId: number
  commentAlreadyInDb?: boolean
}
export const AdvancedEvaluationCommentPanelListElement = ({ highlightedCommentId,
                                                            commentAlreadyInDb,
                                                            chosenCommentId,
                                                            updateCommentsList,
                                                            setChosenComment,
                                                            comment,
                                                            commentId,
                                                            handleCommentRemoval }:AdvancedEvaluationCommentPanelListElementPropsInterface ) => {
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const [commentReadonly, setCommentReadonly] = useState(true)

  useUpdateEffect(()=>{
    const updateComment = () => {
      changeCommentPostgresService(commentState)
        .then(() => {
          if(updateCommentsList!==undefined)
            updateCommentsList(commentState)
        })
        .catch((error) => console.log(error))
    }
    if (commentAlreadyInDb === false)
    {
      if(updateCommentsList!==undefined)
        updateCommentsList(commentState)
      return
    }
    else if(handleCommentRemoval !==undefined){
      const timeoutId = setTimeout(()=> updateComment(), 300)
      return () => clearTimeout(timeoutId)
    }
  }, [commentState])
  const handleColorPickerBehaviour = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault()
    setCommentState((prevState) => ({ ...prevState, color: event.target.value }))
  }
  const handleCommentChoice = (event: React.MouseEvent<HTMLDivElement>) => {
    if ((event.target as Element).id !== "commentListElementDiv")
      return
    event.stopPropagation()
    event.preventDefault()
    if (chosenCommentId === comment.id) {
        if(setChosenComment !== undefined)
            setChosenComment(undefined)
    } else {
      if(setChosenComment !== undefined)
        setChosenComment(commentState)
    }
  }

  return (
    <div className="pb-1 select-none">
      <div onClick={(event) =>{
        handleCommentRemoval !==undefined && handleCommentChoice(event)}}
        id={"commentListElementDiv"}
        style={{
        backgroundColor: highlightedCommentId === comment.id ? comment.color + "80" : "white",
        border: chosenCommentId === comment.id ? "4px solid #6D8DFF" : "none",
        margin: "4px 0px 0px 0px",
        padding: "5px 4px"}
      }>

          <div className="mb-3 flex">
          <textarea className="italic underline underline-offset-2 max-h-[75px] min-h-[36px] pr-2"
                    disabled={commentReadonly}
                    onChange={(event) => {
                      handleCommentRemoval !==undefined && setCommentState((prevState) => ({ ...prevState, description: event.target.value }))}}
                    id={"commentDescription"}
                    key={commentState.id}
                    readOnly={commentReadonly}
                    defaultValue={commentState.description}></textarea>
            {handleCommentRemoval !==undefined && <button className=" text-main_blue underline text-sm flex" id={"editButton"}
                    type={"button"}
                    onClick={() => setCommentReadonly(prevState => !prevState)}>
              <div className='flex inline-block pt-3'><FaEdit className="mt-1 mr-[2px] ml-2" />
                  {commentReadonly ? "Edytuj" : "Zatwierdź"}</div>
            </button>}
          </div>
          <div className='flex inline-block w-fit'>
            <p className="mb-2 h-full mr-2">Kolor:</p>
            <input
              id={"colorPicker"}
              value={commentState.color}
              disabled={handleCommentRemoval ===undefined}
              onChange={(event) => handleColorPickerBehaviour(event)}
              type={"color"} />
          </div>

        <br />
        {handleCommentRemoval !==undefined && <button className="text-berry_red ml-auto text-center flex mr-4"
                onClick={() => {
                  handleCommentRemoval!==undefined && handleCommentRemoval(commentId)}}
                type={"button"}>
          <MdDelete className="text-berry_red mt-[4.5px]" />Usuń
        </button>}
      </div>
      <hr className="mt-2" />
    </div>
  )
}