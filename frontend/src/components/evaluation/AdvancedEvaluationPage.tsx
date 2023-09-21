import { MutableRefObject, PropsWithChildren, useEffect, useRef, useState } from "react"
import { useLocation } from "react-router-dom"
import { FileInterface } from "../../types/FileInterface"
import { useGetFiles } from "../customHooks/useGetFiles"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextFileArea } from "./AdvancedEvaluationTextFileArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationCommentInterface } from "../../types/AdvancedEvaluationCommentInterface"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [comments, setComments] = useState<AdvancedEvaluationCommentInterface[]>([])
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])

  const checkNewRanges = (updatedComment:AdvancedEvaluationCommentInterface, selectedComment:AdvancedEvaluationCommentInterface):AdvancedEvaluationCommentInterface[] => {
    if(updatedComment.highlightStart<=updatedComment.highlightEnd){
      return [updatedComment, selectedComment]
    }
    else{
      return [selectedComment]
    }
  }

  const toLetterArray = (commentsList:AdvancedEvaluationCommentInterface[]):(string|undefined)[] => {
    let newArray = Array<undefined|string>(fileText.length)
    newArray.fill(undefined, 0, fileText.length)
    commentsList.forEach((comment)=>{
      newArray.fill(comment.color, comment.highlightStart, comment.highlightEnd+1)
    })
    return newArray
  }

  useEffect(() => {
    setLetterColor(toLetterArray(comments))
  }, [fileText])

  useEffect(() => {
    if(file){
      file.fileData.text()
        .then((text) => {
          setFileText(text)})
    }

  }, [file])

  const handleActiveCommentChange = (comment:CommentInterface) =>{
    const selection = window.getSelection()
    if(selection && selection.rangeCount>0 && window.getSelection()?.getRangeAt(0).endContainer.parentElement?.id && window.getSelection()?.getRangeAt(0).startContainer.parentElement?.id !== null){
      const selectedComment:AdvancedEvaluationCommentInterface = {
        id: comment.id,
        description: comment.description,
        color: comment.color,
        highlightStart: Math.min(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number),
        highlightEnd: Math.max(selection.getRangeAt(0).endContainer.parentElement?.id as unknown as number, selection.getRangeAt(0).startContainer.parentElement?.id as unknown as number)
      }
      const newComments:AdvancedEvaluationCommentInterface[] = []
      if(comments.length === 0){
        newComments.push(selectedComment)
      }
      else{
        comments.forEach((oldComment)=> {
          console.log("COMMENTS:", newComments, "COLOR", toLetterArray(newComments))
          console.log("SELECTED START:", selectedComment.highlightStart, "SELECTED END:", selectedComment.highlightEnd, "OLD START:", oldComment.highlightStart, "OLD END:", oldComment.highlightEnd)
          if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            let updatedComment = { ...oldComment, highlightEnd: selectedComment.highlightStart-1}
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(1)
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            let updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(2)
          } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            newComments.push(selectedComment)
            console.log(3)
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            newComments.push(selectedComment)
            console.log(4)
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            let updatedComment = { ...oldComment, highlightEnd: oldComment.highlightEnd - 1 }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(5)
          } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            let updatedComment = { ...oldComment, highlightStart: oldComment.highlightStart + 1 }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(6)
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
            let updatedComment = {
              ...oldComment,
              highlightEnd: selectedComment.highlightStart-1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(7)
          }
          else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            let updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(8)
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            let updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(9)
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            let updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
            console.log(10)
          }
          else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
            console.log(11)
            return
          }
          else{
            console.log(newComments, "przed")
            newComments.push(selectedComment)
            console.log(0)
          }
        })
        console.log("NEW COMMENTS:", newComments, "NEW COLOR", toLetterArray(newComments))
      }
      setComments(newComments)
      setLetterColor(toLetterArray(newComments))
      selection?.removeAllRanges()
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