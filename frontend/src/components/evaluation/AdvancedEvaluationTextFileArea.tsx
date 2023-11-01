import { AdvancedEvaluationCommentInterface } from "../../types/AdvancedEvaluationCommentInterface"
import { CommentInterface } from "../../types/CommentInterface"
import { useEffect, useState } from "react"


export const AdvancedEvaluationTextFileArea = ({chosenComment, fileText, width, height}:{
  width:number|undefined,
  height:number|undefined,
  chosenComment: CommentInterface|undefined
  fileText: string }) => {
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

  useEffect(() => {
    if(chosenComment !== undefined){
      handleActiveCommentChange(chosenComment)
    }
  }, [chosenComment])
  useEffect(() => {
    setLetterColor(toLetterArray(comments))
  }, [fileText])

  return <div style={{width: width !== undefined ? width : "100%", height: height !== undefined ? height:"100%"}}>{fileText.split('').map((letter:string, index)=>{
    return <span id={index.toString()} style={{backgroundColor: letterColor[index] ? letterColor[index] : 'white'}} key={index}>{letter}</span>
  })}</div>
}