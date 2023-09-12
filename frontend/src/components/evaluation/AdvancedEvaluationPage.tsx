import { MutableRefObject, PropsWithChildren, useEffect, useRef, useState } from "react"
import { useLocation } from "react-router-dom"
import { FileInterface } from "../../types/FileInterface"
import { useGetFiles } from "../customHooks/useGetFiles"
import { te } from "date-fns/locale"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextFileArea } from "./AdvancedEvaluationTextFileArea"
import { CommentInterface } from "../../types/CommentInterface"

interface advancedEvaluationPageInterface{

}

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [activeComment, setActiveComment] = useState<CommentInterface| undefined>(undefined)

  useEffect(() => {
    if(file){
      file.fileData.text()
        .then((text) => {
          setFileText(" " + text + " ")})
    }

  }, [file, activeComment])

  const handleActiveCommentChange = (comment:CommentInterface) =>{
    setActiveComment(comment)
  }




  return (
    <>
      <AdvancedEvaluationCommentPanel handleActiveCommentChange={handleActiveCommentChange}></AdvancedEvaluationCommentPanel>
      <br/>
      <AdvancedEvaluationTextFileArea color={activeComment?.color} text={fileText}></AdvancedEvaluationTextFileArea>
    </>
  )
}