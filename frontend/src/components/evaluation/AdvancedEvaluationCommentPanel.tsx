import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, SetStateAction, useCallback, useLayoutEffect, useRef, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { CommentCreateInterface } from "../../types/CommentCreateInterface"
import {
  changeCommentPostgresService,
  createCommentWithUserPostgresService,
  deleteCommentPostgresService
} from "../../services/postgresDatabaseServices"
import { ca } from "date-fns/locale"
import { type } from "os"
import { parseISO } from "date-fns"
import { FaSort } from "react-icons/fa";
import { SortCommentsButtonStateType } from "../../types/SortCommentsButtonStateType";
import { IoMdArrowDropright, IoMdArrowDropleft } from "react-icons/io";

interface AdvancedEvaluationCommentPanelPropsInterface {
  setChosenComment: React.Dispatch<React.SetStateAction<CommentInterface | undefined>>,
  setUpdatedComment: React.Dispatch<React.SetStateAction<CommentInterface | undefined>>,
  chosenCommentId: number | undefined,
  setSortButtonState: (buttonState: SortCommentsButtonStateType) => void,
  highlightedCommentId: number | undefined,
  sortButtonState: SortCommentsButtonStateType,
  rightPanelUserComments: CommentInterface[],
  setRightPanelUserComments: (comments: CommentInterface[]) => void,
  assignmentId: number,
  setDeletedRightPanelCommentId: React.Dispatch<React.SetStateAction<number | undefined>>
  onCommentPanelListRefChange: (node: HTMLDivElement) => void
}
export const AdvancedEvaluationCommentPanel = (
  { assignmentId,
    setUpdatedComment,
    setDeletedRightPanelCommentId,
    highlightedCommentId,
    sortButtonState,
    setSortButtonState,
    chosenCommentId,
    rightPanelUserComments,
    setRightPanelUserComments,
    setChosenComment,
    onCommentPanelListRefChange }: AdvancedEvaluationCommentPanelPropsInterface) => {

  const [isHidden, setIsHidden] = useState<boolean>(false)
  const [newCommentDescription, setNewCommentDescription] = useState<string>("")
  const userState = useAppSelector(selectUserState)
  const updateCommentsList = async (changedComment: CommentInterface) => {
    try {
      const response = await changeCommentPostgresService(changedComment)
      if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
        const newComments = rightPanelUserComments.map(comment => {
          if (comment.id === changedComment?.id) return changedComment
          else return comment
        })
        setRightPanelUserComments(newComments)
        setUpdatedComment(changedComment)
      }
    } catch (error) {
      console.log(error)
    }
  }
  const handleCommentRemoval = async (commentId: number) => {
    try {
      const response = await deleteCommentPostgresService(commentId.toString())
      if (response.data !== null && response.data !== undefined && response.status === 200) {
        const userNewComments = rightPanelUserComments.filter(oldComment => oldComment.id !== commentId)
        setRightPanelUserComments(userNewComments)
        setDeletedRightPanelCommentId(commentId)
      }
    } catch (error) {
      console.log(error)
    }
  }
  const handleNewCommentCreation = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (newCommentDescription.length > 0) {
      if (userState?.id !== null && userState?.id !== undefined) {
        const newComment: CommentCreateInterface = {
          description: newCommentDescription,
          title: "",
          color: "#" + Math.floor(Math.random() * 16777215).toString(16), //random color
          userId: userState.id,
          assignmentId: assignmentId
        }
        createCommentWithUserPostgresService(newComment)
          .then((response) => {
            if (response.data !== undefined && response.data !== null && response.status === (201 || 200)) {
              const tempComments = rightPanelUserComments
              setRightPanelUserComments([{
                ...newComment,
                id: response.data.id,
                counter: response.data.counter,
                lastUsedDate: parseISO(response.data.lastUsedDate)
              },
              ...tempComments])
              setNewCommentDescription("")
            }
          })
          .catch((error) => console.log(error))
      }
    }
  }


  return (
    <div style={{ float: "right", height: "100vh" }} className='flex'>
      <button className={'bg-light_gray h-full w-[20px] pl-[2px] rounded-bl-md rounded-tl-md'} onClick={() => setIsHidden(!isHidden)}>{isHidden ? <IoMdArrowDropleft /> : <IoMdArrowDropright />}</button>
      {!isHidden && <div className='pl-1 w-[310px]'>
        <p className='text-center underline underline-offset-4 mb-4'>KOMENTARZE</p>

        <form onSubmit={(event) => handleNewCommentCreation(event)}>
          <div className='inline-flex w-full pb-2'>
            <p className='mr-2'>Dodaj komentarz: </p>
            <input value={newCommentDescription} className='border border-black rounded-sm mr-2 w-full pl-1.5 h-10 mt-2' onChange={(event) => setNewCommentDescription(event.target.value)} />
            <button className='w-24 ml-auto mr-2 bg-main_blue text-white px-2 h-10 mt-2 rounded-md' type={"submit"}>Dodaj</button><br /><hr />
          </div>
        </form>
        <section >
          <p className='pt-1 pb-1 font-semibold' ref={onCommentPanelListRefChange}>Wybierz komentarz: </p>
          <section className='border border-border_gray rounded-md w-fit px-2 flex'>
            <label htmlFor={"sortDropdown"} className='flex'> <FaSort className='mt-1' />Sortuj:</label>
            <select defaultValue={sortButtonState} onChange={(event) => setSortButtonState(event.target.value as SortCommentsButtonStateType)} name={"sortDropdown"} id={"sortDropdown"}>
              <option value={"description,asc"}>Alfabetycznie</option>
              <option value={"counter,desc"}>Najczęściej używane</option>
              <option value={"counter,asc"}> Najrzadziej używane</option>
              <option value={"lastUsedDate,desc"}>Ostatnio używane </option>
            </select>

          </section>
          <br />
          <hr className='mr-8 mb-2' />
          <div className='overflow-y-auto mx-4'>{rightPanelUserComments.map((comment) => {
            return (
              <div key={comment.id} >
                <AdvancedEvaluationCommentPanelListElement
                  commentId={comment.id}
                  highlightedCommentId={highlightedCommentId}
                  chosenCommentId={chosenCommentId}
                  handleCommentRemoval={handleCommentRemoval}
                  comment={comment}
                  setChosenComment={setChosenComment}
                  updateCommentsList={updateCommentsList}></AdvancedEvaluationCommentPanelListElement>
              </div>)
          }
          )}</div>
        </section>
      </div>}
    </div>)
}
