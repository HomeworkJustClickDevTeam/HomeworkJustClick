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
  let image = document.createElement("img")
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)

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



  return (
    <>
      <AdvancedEvaluationCommentPanel setChosenComment={setChosenComment}></AdvancedEvaluationCommentPanel>
      <br/>
      {file
        ? (file.format === "txt"
            ? <AdvancedEvaluationTextFileArea
              chosenComment={chosenComment}
              fileText={fileText}/>
            : <AdvancedEvaluationImageArea image={image} chosenComment={chosenComment}></AdvancedEvaluationImageArea>)
        : <Loading></Loading>
        }
    </>
  )
}