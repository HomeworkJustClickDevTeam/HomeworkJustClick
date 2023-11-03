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
import { useGetCommentsByUser } from "../customHooks/useGetCommentsByUser"
import { useAppSelector } from "../../types/HooksRedux"
import { selectUserState } from "../../redux/userStateSlice"
import {
  deleteCommentImagePostgresService,
  deleteCommentPostgresService
} from "../../services/postgresDatabaseServices"

export default function AdvancedEvaluationPage() {
  let {state} = useLocation()
  const userState = useAppSelector(selectUserState)
  const file = useGetFiles(state, 'solution')[0]
  const [fileText, setFileText] = useState("")
  const [image, setImage] = useState<HTMLImageElement|undefined>(undefined)
  const [chosenComment, setChosenComment] = useState<CommentInterface|undefined>(undefined)
  const [chosenCommentFrameWidth, setChosenCommentFrameWidth] = useState<number|undefined>(undefined)
  const drawnCommentsRef = useRef<AdvancedEvaluationImageCommentInterface[]>([])
  const {comments: rightPanelUserComments,setComments: setRightPanelUserComments} = useGetCommentsByUser(userState?.id, "")
  const [drawOnCanvasRunner, setDrawOnCanvasRunner] = useState(false)
  const {availableHeight, availableWidth} = useGetSolutionAreaSizeAvailable()



  const handleCommentImageRemoval = async (comment:CommentInterface) => {

    deleteCommentPostgresService(comment.id.toString())
      .then((response)=> {
        if(response.data !== null && response.data !== undefined && response.data.status === 200){
          const userNewComments = rightPanelUserComments.filter(comment => comment.id !== comment.id)
          setRightPanelUserComments(userNewComments)
          drawnCommentsRef.current = drawnCommentsRef.current.filter(commentImage =>
          {
            if(commentImage.commentId === comment.id){
              deleteCommentImagePostgresService(commentImage.commentId.toString())
                .then((response) => {
                  if(response!== null && response!== undefined && response.data.status === 200){
                    return false
                  }
                })
                .catch(error=>{
                  console.log(error)
                  return true
                })
            }
            else {
              return true
            }
          })
          setDrawOnCanvasRunner((prevState)=>!prevState)
        }
      })
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
        handleCommentImageRemoval={handleCommentImageRemoval}
        setRightPanelUserComments={setRightPanelUserComments}
        rightPanelUserComments={rightPanelUserComments}
        setChosenCommentFrameWidth={setChosenCommentFrameWidth}
        setChosenComment={setChosenComment}></AdvancedEvaluationCommentPanel>
      {file
        ? (file.format === "txt"
            ? (<AdvancedEvaluationTextFileArea
              fileId={file.postgresId}
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