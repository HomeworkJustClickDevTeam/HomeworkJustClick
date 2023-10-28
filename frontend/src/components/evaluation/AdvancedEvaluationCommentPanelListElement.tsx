import React, { useEffect, useRef, useState } from "react"
import { CommentInterface } from "../../types/CommentInterface"
import { colorToHex } from "../../helpers"

export const AdvancedEvaluationCommentPanelListElement = ({handleCommentClick, comment}:{
  handleCommentClick: (comment:CommentInterface, commentWidth:number)=>void,
  comment:CommentInterface
})=>{
  const [frameWidth, setFrameWidth] = useState<number>(5)
  const [commentState, setCommentState] = useState<CommentInterface>(comment)
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))

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
  }, [])

  return(
  <div>
    <div><button type={'button'} onClick={() => handleCommentClick(commentState, frameWidth)} style={{backgroundColor: commentState.color}}>{commentState.description}</button>
      <input value={commentState.color} onChange={(event) => setCommentState((prevState)=>({...prevState, color:event.target.value}))} type={"color"}></input></div><br/>
    <div>
      <canvas ref={canvasRef} height={35}></canvas>
      <input type={"range"} min={1} max={10} value={frameWidth} onChange={(event) => setFrameWidth(+event.target.value)}/><br/>
    Szerokość: {frameWidth}</div>
    <hr/>
  </div>)
}