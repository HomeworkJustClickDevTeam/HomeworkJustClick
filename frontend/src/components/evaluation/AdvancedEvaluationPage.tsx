import { useEffect, useState } from "react"
import { useLocation } from "react-router-dom"
import { useGetFiles } from "../customHooks/useGetFiles"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextFileArea } from "./AdvancedEvaluationTextFileArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationCommentInterface } from "../../types/AdvancedEvaluationCommentInterface"
import { AdvancedEvaluationImageArea } from "./AdvancedEvaluationImageArea"
import Loading from "../animations/Loading"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [comments, setComments] = useState<AdvancedEvaluationCommentInterface[]>([])
  const [letterColor, setLetterColor] = useState<(string|undefined)[]>([undefined])
  let image = document.createElement("img")

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
    if(file && file.format !== "jpg" && file.format !== "png"){
      file.data.text()
        .then((text) => {
          setFileText(text)})
    }
    else if(file){
      image.src = URL.createObjectURL(file.data)
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
        for(const oldComment of comments) {
          if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            const updatedComment = { ...oldComment, highlightEnd: selectedComment.highlightStart-1}
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            const updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            newComments.push(selectedComment)
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            newComments.push(selectedComment)
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd < selectedComment.highlightEnd) {
            const updatedComment = { ...oldComment, highlightEnd: oldComment.highlightEnd - 1 }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          } else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            const updatedComment = { ...oldComment, highlightStart: oldComment.highlightStart + 1 }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          } else if (oldComment.highlightStart < selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
            const updatedComment = {
              ...oldComment,
              highlightEnd: selectedComment.highlightStart-1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          }
          else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart < selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            const updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            const updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          }
          else if (oldComment.highlightStart > selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd > selectedComment.highlightStart && oldComment.highlightEnd > selectedComment.highlightEnd) {
            const updatedComment = {
              ...oldComment,
              highlightStart: selectedComment.highlightEnd+1
            }
            newComments.push(...checkNewRanges(updatedComment, selectedComment))
          }
          else if (oldComment.highlightStart === selectedComment.highlightStart && oldComment.highlightStart === selectedComment.highlightEnd && oldComment.highlightEnd === selectedComment.highlightStart && oldComment.highlightEnd === selectedComment.highlightEnd) {
            newComments.push(selectedComment)
            return
          }
          else{
            newComments.push(selectedComment, oldComment)
          }
        }
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
      {file
        ? (file.format === "txt"
            ? <AdvancedEvaluationTextFileArea
              letterColor={letterColor}
              text={fileText}/>
            : <AdvancedEvaluationImageArea image={image}></AdvancedEvaluationImageArea>)
        : <Loading></Loading>
        }
    </>
  )
}