import { useEffect, useRef, useState } from "react"
import { useLocation } from "react-router-dom"
import { useGetFiles } from "../customHooks/useGetFiles"
import { AdvancedEvaluationCommentPanel } from "./AdvancedEvaluationCommentPanel"
import { AdvancedEvaluationTextFileArea } from "./AdvancedEvaluationTextFileArea"
import { CommentInterface } from "../../types/CommentInterface"
import { AdvancedEvaluationImageArea } from "./AdvancedEvaluationImageArea"
import Loading from "../animations/Loading"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useGetSolutionAreaSizeAvailable } from "../customHooks/useGetSolutionAreaSizeAvailable"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const [chosenCommentFrameWidth, setChosenCommentFrameWidth] = useState<number|undefined>(undefined)
  const drawnCommentsRef = useRef<AdvancedEvaluationImageCommentInterface[]>([])
  const [rightPanelUserComments, setRightPanelUserComments] = useState<CommentInterface[]>([
    {color:'#ff0000', id:0, description: "tutaj komentarz"},
    {color:'#0068ff', id:1, description: "tutaj inny komentraz"},
    {color:'#fffb00', id:2, description: "ostatni komentarz"}])
  const [drawOnCanvasRunner, setDrawOnCanvasRunner] = useState(false)
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()



  const handleCommentRemoval = (commentId:number) => {
    const userNewComments = rightPanelUserComments.filter(comment => comment.id !== commentId)
    setRightPanelUserComments(userNewComments)
    drawnCommentsRef.current = drawnCommentsRef.current.filter(comment => comment.id !== commentId)
    setDrawOnCanvasRunner((prevState)=>!prevState)
  }


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
    <div id={"advancedEvaluationCommentPanelDiv"}>
      <AdvancedEvaluationCommentPanel
        height={availableHeight}
        handleCommentRemoval={handleCommentRemoval}
        setRightPanelUserComments={setRightPanelUserComments}
        rightPanelUserComments={rightPanelUserComments}
        setChosenCommentFrameWidth={setChosenCommentFrameWidth}
        setChosenComment={setChosenComment}></AdvancedEvaluationCommentPanel>
      {file
        ? (file.format === "txt"
            ? (<AdvancedEvaluationTextFileArea
              chosenComment={chosenComment}
              width = {availableWidth}
              height = {availableHeight}
              fileText={fileText}/>)
            : (image !== undefined ? <AdvancedEvaluationImageArea
              drawOnCanvasRunner={drawOnCanvasRunner}
              width = {availableWidth}
              height = {availableHeight}
              editable={true}
              drawnComments={drawnCommentsRef.current}
              chosenCommentFrameWidth={chosenCommentFrameWidth}
              setChosenComment={setChosenComment}
              image={image}
              chosenComment={chosenComment}></AdvancedEvaluationImageArea>
              : <Loading></Loading>))
        : <Loading></Loading>}
    </div>
  )
}