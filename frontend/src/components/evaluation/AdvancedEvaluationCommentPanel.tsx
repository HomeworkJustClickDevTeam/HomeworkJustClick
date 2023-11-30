import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useState } from "react"
import { AdvancedEvaluationCommentPanelListElement } from "./AdvancedEvaluationCommentPanelListElement"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import { CommentCreateInterface } from "../../types/CommentCreateInterface"
import { createCommentWithUserPostgresService } from "../../services/postgresDatabaseServices"
import { ca } from "date-fns/locale"
import { type } from "os"
import { parseISO } from "date-fns"
import { FaSort } from "react-icons/fa";
import {SortButtonStateType} from "../../types/SortButtonStateType";

interface AdvancedEvaluationCommentPanelPropsInterface{
  setChosenComment: (comment:CommentInterface|undefined)=>void,
  updateCommentsLists: (comment: CommentInterface) => void,
  chosenCommentId:number|undefined,
  setSortButtonState:(buttonState:SortButtonStateType) => void,
  highlightedCommentId:number|undefined,
  sortButtonState: SortButtonStateType,
  fileType:"txt"|"img",
  height:number|undefined,
  rightPanelUserComments: CommentInterface[],
  setRightPanelUserComments: (comments:CommentInterface[]) => void,
  handleCommentRemoval:(comment:CommentInterface)=>void,
  assignmentId: number
}
export const AdvancedEvaluationCommentPanel = (
  {assignmentId,
    highlightedCommentId,
    sortButtonState,
    updateCommentsLists,
    setSortButtonState,
    chosenCommentId,
    fileType,
    rightPanelUserComments,
    setRightPanelUserComments,
    handleCommentRemoval,
    height,
    setChosenComment}:AdvancedEvaluationCommentPanelPropsInterface) => {

  const [newCommentDescription, setNewCommentDescription] = useState<string>("")
  const userState = useAppSelector(selectUserState)

  const handleNewCommentCreation = async (event:React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(newCommentDescription.length > 0)
    {
      if(userState?.id !== null && userState?.id !== undefined){
        const newComment:CommentCreateInterface = {
          description: newCommentDescription,
          title:"",
          color: "#" + Math.floor(Math.random()*16777215).toString(16), //random color
          userId:userState.id,
          assignmentId:assignmentId
        }
        createCommentWithUserPostgresService(newComment)
          .then((response) => {
            if(response.data !== undefined && response.data !== null && response.status === 201){
              const tempComments = rightPanelUserComments
              setRightPanelUserComments([{
                ...newComment,
                id:response.data.id,
                counter:response.data.counter,
                lastUsedDate: parseISO(response.data.lastUsedDate)},
                ...tempComments])
              setNewCommentDescription("")
            }
          })
          .catch((error) => console.log(error))
      }
    }
  }
  return (
    <div id={"commentPanelDiv"} style={{float:"right", height:height !== undefined ? height.toString() : "100%"}}>
      <p className='text-center underline underline-offset-4 mb-4'>KOMENTARZE</p>
      Dodaj nowy komentarz:<br/>
      <div className='inline-flex w-full pb-4'>
        <input className='border border-black rounded-sm mr-2 w-full pl-1.5' onChange={(event) => setNewCommentDescription(event.target.value)}/>
        <button className='w-24 ml-auto mr-2 bg-main_blue text-white px-2 py-1 rounded-md' type={"button"} onClick={(event) => handleNewCommentCreation(event)}>Dodaj</button><br/><hr/>
      </div>

    <section >
      <p className='pt-1 pb-1 font-semibold'>Wybierz komentarz: </p>
        <section className='border border-border_gray rounded-md w-fit px-2 flex'>
          <label htmlFor={"sortDropdown"} className='flex'> <FaSort className='mt-1'/>Sortuj:</label>
          <select defaultValue={sortButtonState} onChange={(event) => setSortButtonState(event.target.value as SortButtonStateType)} name={"sortDropdown"} id={"sortDropdown"}>
            <option value={"description,desc"}>Alfabetycznie</option>
            <option value={"counter,desc"}>Najczęściej używane</option>
            <option value={"counter,asc"}> Najrzadziej używane</option>
            <option value={"lastUsedDate,desc"}>Ostatnio używane </option>
          </select>

        </section>
      <br/>
        <hr className='mr-8 mb-2'/>
      <div className='overflow-y-scroll mx-4'>{rightPanelUserComments.map((comment) => {
        return(
          <div key={comment.id} >
            <AdvancedEvaluationCommentPanelListElement
              highlightedCommentId={highlightedCommentId}
              chosenCommentId={chosenCommentId}
              fileType={fileType}
              handleCommentRemoval={handleCommentRemoval}
              comment={comment}
              setChosenComment={setChosenComment}
              updateCommentsLists={updateCommentsLists}></AdvancedEvaluationCommentPanelListElement>
          </div>)}
      )}</div>
    </section>
    </div>)
}
