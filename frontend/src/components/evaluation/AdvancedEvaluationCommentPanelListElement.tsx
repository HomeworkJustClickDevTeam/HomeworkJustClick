import React, { useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { FaEdit } from "react-icons/fa"
import { MdDelete } from "react-icons/md"
import { useTimeout, useUpdateEffect } from "usehooks-ts"
import { delay } from "@reduxjs/toolkit/dist/utils"

interface AdvancedEvaluationCommentPanelListElementPropsInterface{
  setChosenComment?: React.Dispatch<React.SetStateAction<CommentInterface|undefined>>,
  updateCommentsList: (comment: CommentInterface) => void,
  comment: CommentInterface,
  handleCommentRemoval: (commentToBeRemoved: CommentInterface) => void
  chosenCommentId?: number | undefined
  highlightedCommentId?: number | undefined
}
export const AdvancedEvaluationCommentPanelListElement = ({ highlightedCommentId,
                                                            chosenCommentId,
                                                            updateCommentsList,
                                                            setChosenComment,
                                                            comment,
                                                            handleCommentRemoval }:AdvancedEvaluationCommentPanelListElementPropsInterface ) => {
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const [commentReadonly, setCommentReadonly] = useState(true)

  useUpdateEffect(()=>{
    const updateComment = () => {
      changeCommentPostgresService(commentState)
        .then(() => {
          updateCommentsList(commentState)
        })
        .catch((error) => console.log(error))
    }
    const timeoutId = setTimeout(()=> updateComment(), 250)
    return () => clearTimeout(timeoutId)
  }, [commentState])
  const handleColorPickerBehaviour = (event: React.ChangeEvent<HTMLInputElement>) => {
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
      <div onClick={(event) => handleCommentChoice(event)}
        id={"commentListElementDiv"}
        style={{
        backgroundColor: highlightedCommentId === comment.id ? comment.color + "80" : "white",
        border: chosenCommentId === comment.id ? "4px solid #6D8DFF" : "none",
        margin: "4px 0px 0px 0px",
        padding: "5px 4px"}
      }>
        <div>
          <div className="mb-3 flex">
          <textarea className="italic underline underline-offset-2 max-h-[75px] min-h-[36px] pr-2"
                    disabled={commentReadonly}
                    onChange={(event) => {
                      !commentReadonly &&
                      setCommentState((prevState) => ({ ...prevState, description: event.target.value }))
                    }
                    }
                    id={"commentDescription"}
                    key={commentState.id}
                    readOnly={commentReadonly}
                    defaultValue={commentState.description}></textarea>
            <button className=" text-main_blue underline text-sm flex" id={"editButton"}
                    onClick={() => setCommentReadonly(prevState => !prevState)}>
              <FaEdit className="mt-1 mr-[2px] ml-2" />
              {commentReadonly ? "Edytuj" : "Ok"}
            </button>
          </div>
            <p className="mb-2 h-full">Kolor:</p>
            <input
              id={"colorPicker"}
              value={commentState.color}
              onChange={(event) => handleColorPickerBehaviour(event)}
              type={"color"} />
        </div>
        <br />
        <button className="text-berry_red ml-auto text-center flex mr-4"
                onClick={() => handleCommentRemoval(commentState)} type={"button"}>
          <MdDelete className="text-berry_red mt-[4.5px]" />Usuń
        </button>
      </div>
      <hr className="mt-2" />
    </div>
  )
}