import React, { ChangeEvent, useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"

export const AdvancedEvaluationCommentPanelListElement = ({handleCommentClick, comment, handleCommentRemoval}:{
  handleCommentClick: (comment:CommentInterface, commentWidth:number)=>void,
  comment:CommentInterface,
  handleCommentRemoval: (commentId:number)=>void
})=>{
  const [frameWidth, setFrameWidth] = useState<number>(5)
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const [commentReadonly, setCommentReadonly] = useState(true)
  const commentDescriptionInputRef = useRef<HTMLInputElement|null>(null)

  const handleEditButtonBehaviour = () => {
    setCommentReadonly(prevState => !prevState)
    if(commentDescriptionInputRef.current !== null && commentDescriptionInputRef.current !== undefined)
      commentDescriptionInputRef.current.focus()
  }
  const handleColorPickerChange = (event:React.ChangeEvent<HTMLInputElement>) => {
    setCommentState((prevState)=>({...prevState, color:event.target.value}))
    handleCommentClick({...commentState, color:event.target.value}, frameWidth)
}
  const handleSliderChange = (event:React.ChangeEvent<HTMLInputElement>) => {
    setFrameWidth(+event.target.value)
    handleCommentClick(commentState, +event.target.value)
  }

  useEffect(() => {
    const drawOnCanvas = () => {
      if(canvasContext.current!==null && canvasContext.current!==undefined && canvasRef.current!==null && canvasRef.current!==undefined) {
        canvasContext.current.clearRect(0, 0, canvasRef.current.width, canvasRef.current.height)
        canvasContext.current.strokeStyle = commentState.color
        canvasContext.current.lineWidth = frameWidth
        canvasContext.current.strokeRect(5, 5,canvasRef.current.width - 10 , 20)
      }
    }
    drawOnCanvas()
  }, [frameWidth, commentState.color])
  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    if(commentDescriptionInputRef.current !== null && commentDescriptionInputRef.current !== undefined)
      commentDescriptionInputRef.current.blur()
  }, [])

  return(
  <div>
    <div>
      <input ref={commentDescriptionInputRef}
            onChange={(event) => {!commentReadonly &&
              setCommentState((prevState) => ({...prevState, description:event.target.value}))}
            }
             id={"commentDescription"}
             key={commentState.id}
             readOnly={commentReadonly}
             onClick={() => {commentReadonly && handleCommentClick(commentState, frameWidth)}}
             defaultValue={commentState.description}></input>
      <button id={"editButton"} onClick={() => handleEditButtonBehaviour()}>
        {commentReadonly ? "Edytuj kometarz":"Ok"}
      </button>
      <input
        id={"colorPicker"}
        value={commentState.color}
        onChange={(event) => handleColorPickerChange(event)}
        onMouseUp={() => {
          handleCommentClick(commentState, frameWidth)
          if(commentDescriptionInputRef.current !== null && commentDescriptionInputRef.current !== undefined)
            commentDescriptionInputRef.current.focus()
        }}
        type={"color"}></input>
    </div><br/>
    <div>
      <canvas id={"frameWidthPreview"} ref={canvasRef} height={35}></canvas>
      <input type={"range"}
             min={1}
             max={10}
             value={frameWidth}
             onChange={(event) => handleSliderChange(event)}
             onMouseUp={() => {
               if(commentDescriptionInputRef.current !== null && commentDescriptionInputRef.current !== undefined)
                 commentDescriptionInputRef.current.focus()
             }}/><br/>
    Szerokość: {frameWidth}</div>
    <button onClick={() => handleCommentRemoval(commentState.id)} type={"button"}>Usuń komentarz</button>
    <hr/>
  </div>)
}