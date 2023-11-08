import React, { ChangeEvent, useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"
import { changeCommentPostgresService } from "../../services/postgresDatabaseServices"
import { Simulate } from "react-dom/test-utils"
import error = Simulate.error

export const AdvancedEvaluationCommentPanelListElement = (
  {highlightedCommentId, chosenCommentId, updateCommentsLists, fileType, setChosenComment, comment, handleCommentRemoval}:{
  setChosenComment: (comment:CommentInterface|undefined)=>void,
  updateCommentsLists: (comment: CommentInterface, commentWidth?:number) => void,
  comment:CommentInterface,
  fileType:"txt"|"img",
  handleCommentRemoval: (commentToBeRemoved:CommentInterface)=>void
  chosenCommentId:number|undefined
  highlightedCommentId:number|undefined
})=>{
  const [frameWidth, setFrameWidth] = useState<number>(5)
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const [commentReadonly, setCommentReadonly] = useState(true)

  const handleCommentChoice = () => {
    if(chosenCommentId === comment.id) {
      setChosenComment(undefined)
    }
    else {
      setChosenComment(comment)
    }
  }
  const handleEditButtonBehaviour = async () => {
    if(!commentReadonly){
      changeCommentPostgresService(commentState)
        .catch((error)=> console.log(error))
    }
    setCommentReadonly(prevState => !prevState)
  }
  const handleSliderChange = (event:React.ChangeEvent<HTMLInputElement>) => {
    setFrameWidth(+event.target.value)
  }

  useEffect(() => {
    const drawWidthPreviewOnCanvas = () => {
      if(canvasContext.current!==null && canvasContext.current!==undefined && canvasRef.current!==null && canvasRef.current!==undefined) {
        canvasContext.current.clearRect(0, 0, canvasRef.current.width, canvasRef.current.height)
        canvasContext.current.strokeStyle = commentState.color
        canvasContext.current.lineWidth = frameWidth
        canvasContext.current.strokeRect(5, 5,canvasRef.current.width - 10 , 20)
      }
    }
    drawWidthPreviewOnCanvas()
  }, [frameWidth, commentState.color])
  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
  }, [])

  return(
  <div style={{backgroundColor: highlightedCommentId === comment.id ? comment.color+"80" : "white"}}>
    <input type={"checkbox"} checked={comment.id === chosenCommentId} onChange={() => handleCommentChoice()}/>
    <div>
      <input
            onChange={(event) => {!commentReadonly &&
              setCommentState((prevState) => ({...prevState, description:event.target.value}))}
            }
             id={"commentDescription"}
             key={commentState.id}
             readOnly={commentReadonly}
             onClick={() => {
               commentReadonly && (
                 fileType === "txt" ?
                   updateCommentsLists(commentState)
                   :
                   updateCommentsLists(commentState, frameWidth)
               )
             }}
             defaultValue={commentState.description}></input>
      <button id={"editButton"} onClick={() => handleEditButtonBehaviour()}>
        {commentReadonly ? "Edytuj kometarz":"Ok"}
      </button>
      <form
        onSubmit={(event) => {
          event.preventDefault()
          fileType === "txt" ?
            updateCommentsLists(commentState)
            :
            updateCommentsLists(commentState, frameWidth)}}>
        <input
          id={"colorPicker"}
          value={commentState.color}
          onChange={(event) =>
            setCommentState((prevState)=>({...prevState, color:event.target.value}))}
          type={"color"}/>
        <button type={"submit"}>Potwierdz zmiane koloru</button>
      </form>

    </div><br/>
    {fileType === "img" &&
      <div>
        <canvas id={"frameWidthPreview"} ref={canvasRef} height={35}></canvas>
        <input type={"range"}
               min={1}
               max={10}
               value={frameWidth}
               onChange={(event) => handleSliderChange(event)}
               onMouseUp={() => { updateCommentsLists(commentState, frameWidth)}}/><br/>
        Szerokość: {frameWidth}
      </div>
    }
    <button onClick={() => handleCommentRemoval(commentState)} type={"button"}>Usuń komentarz</button>
    <hr/>
  </div>)
}