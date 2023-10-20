import { useEffect, useRef, useState } from "react"
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
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)

  useEffect(() => {
    if(file && file.format !== "jpg" && file.format !== "png"){
      file.data.text()
        .then((text) => {
          setFileText(text)})
    }
    else if(file){
      let imageTemp = document.createElement("img")
      imageTemp.src = URL.createObjectURL(file.data)
      imageTemp.onload = () => {
        setImage(imageTemp)
      }
    }
  }, [file])



  return (
    <>
      <AdvancedEvaluationCommentPanel chosenComment={chosenComment} setChosenComment={setChosenComment}></AdvancedEvaluationCommentPanel>
      <br/>
      {file
        ? (file.format === "txt"
            ? (<AdvancedEvaluationTextFileArea
              chosenComment={chosenComment}
              fileText={fileText}/>)
            : (image !== undefined ? <AdvancedEvaluationImageArea image={image} chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))
        : <Loading></Loading>}
    </>
  )
}