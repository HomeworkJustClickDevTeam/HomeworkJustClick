import React, {Dispatch, SetStateAction} from "react";

interface AssignmentCommentPanelFormPropsInterface{
  handleNewCommentCreation: (event: React.MouseEvent<HTMLButtonElement>) => void,
  newCommentDescription: string,
  setNewCommentDescription: Dispatch<SetStateAction<string>>
}
export const AssignmentCommentPanelForm = ({handleNewCommentCreation, setNewCommentDescription, newCommentDescription}:AssignmentCommentPanelFormPropsInterface) =>{
  return(
    <>
      Dodaj komentarz:<br/>
      <input value={newCommentDescription} onChange={(event)=>setNewCommentDescription(event.target.value)}/>
      <button type={"button"} onClick={(event) => handleNewCommentCreation(event)}>Dodaj</button>
    </>
  )
}