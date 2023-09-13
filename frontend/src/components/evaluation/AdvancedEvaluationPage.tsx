import { MutableRefObject, PropsWithChildren, useEffect, useRef, useState } from "react"
import { useLocation } from "react-router-dom"
import { FileInterface } from "../../types/FileInterface"
import { useGetFiles } from "../customHooks/useGetFiles"
import { te } from "date-fns/locale"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextFileArea } from "./AdvancedEvaluationTextFileArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationCommentInterface } from "../../types/AdvancedEvaluationCommentInterface"

interface advancedEvaluationPageInterface{

}

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [commentsList, setCommentsList] = useState<AdvancedEvaluationCommentInterface[]>([])
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])


  useEffect(() => {
    if(fileText){
      let newArray = Array<undefined|string>(fileText.length)
      newArray.fill(undefined, 0, fileText.length)
      commentsList.forEach((comment)=>{
        newArray.fill(comment.color, comment.highlightStart, comment.highlightEnd)
      })
      setLetterColor(newArray)
    }
  }, [fileText])

  useEffect(() => {
    if(file){
      file.fileData.text()
        .then((text) => {
          setFileText(text)})
    }

  }, [file])

  const handleActiveCommentChange = (comment:CommentInterface) =>{
    let selection = window.getSelection()
    if(selection && selection.rangeCount>0 && window.getSelection()?.getRangeAt(0).endContainer.parentElement?.id && window.getSelection()?.getRangeAt(0).startContainer.parentElement?.id !== null){
      const selectedComment:AdvancedEvaluationCommentInterface = {
        id: comment.id,
        description: comment.description,
        color: comment.color,
        highlightStart: Math.min(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number),
        highlightEnd: Math.max(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number)
      }
      let newComments:AdvancedEvaluationCommentInterface[] = []
      commentsList.forEach((oldComment)=> {
      if(oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightStart){
        let updatedComment = {...oldComment, highlightEnd: oldComment.highlightEnd-selectedComment.highlightStart}
        newComments.push(updatedComment, selectedComment)
      }
      else if (oldComment.highlightStart < selectedComment.highlightEnd && selectedComment.highlightStart < oldComment.highlightStart){
        let updatedComment = {...oldComment, highlightStart: oldComment.highlightStart+selectedComment.highlightEnd}
        newComments.push(updatedComment, selectedComment)
      }
      else if(){

      }
    })
    setCommentsList(
      [...commentsList, {...selectedComment}]
    )
    let newArray = letterColor
    newArray.fill(selectedComment.color, selectedComment.highlightStart, selectedComment.highlightEnd+1)
    setLetterColor(newArray)
    selection.removeAllRanges()
  }

  }







  return (
    <>
      <AdvancedEvaluationCommentPanel handleActiveCommentChange={handleActiveCommentChange}></AdvancedEvaluationCommentPanel>
      <br/>
      <AdvancedEvaluationTextFileArea
        letterColor={letterColor}
        text={fileText}/>
    </>
  )
}