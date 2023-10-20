import React, { CanvasHTMLAttributes, MouseEventHandler, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { CommentInterface } from "../../types/CommentInterface"

export const CanvasLogic = ({image, commentsList, setCommentsList, chosenComment}:{
  image:HTMLImageElement,
  commentsList: AdvancedEvaluationImageCommentInterface[],
  setCommentsList: (comments:AdvancedEvaluationImageCommentInterface[]) => void,
  chosenComment:CommentInterface|undefined}) => {
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const commentsListRef = useRef(commentsList)
  let mouseStartX:null|number = null
  let mouseStartY:null|number = null
  let commentNumber:null|number = null

  const chooseCommentToMove = () => {
    if(mouseStartX !== null && mouseStartY !== null){
      for (let i = 0; i < commentsList.length; i++){
        const comment = commentsList[i]
        if(
          mouseStartX >= comment.leftTopXY[0] &&
          mouseStartX <= comment.leftTopXY[0] + comment.width &&
          mouseStartY >= comment.leftTopXY[1] &&
          mouseStartX <= comment.leftTopXY[1] + comment.height
        ){
          commentNumber = i
          break
        }
      }
    }
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    if(commentNumber!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
      const mouseXNow = event.clientX - canvasContext.current.canvas.clientLeft
      const mouseYNow = event.clientY - canvasContext.current.canvas.clientTop
      const moveDiffX = mouseXNow - mouseStartX
      const moveDiffY = mouseYNow - mouseStartY
      mouseStartX = mouseXNow
      mouseStartY = mouseYNow
      commentsListRef.current[commentNumber].leftTopXY[0] += moveDiffX
      commentsListRef.current[commentNumber].leftTopXY[1] += moveDiffY
      drawBoxes()
    }
  }
  const handleMouseDown = (event:React.MouseEvent) => {
    if(chosenComment === undefined){
      if(canvasContext.current !== null && canvasContext.current !== undefined){
        mouseStartX = event.clientX - canvasContext.current.canvas.clientLeft
        mouseStartY = event.clientY - canvasContext.current.canvas.clientTop
        chooseCommentToMove()
      }
    }
    else{
      //create new comment
    }
  }
  const handleMouseUp = () =>{
    commentNumber = null
    setCommentsList(commentsListRef.current)
  }
  const drawBoxes = () => {
    if(canvasContext.current!==null && canvasContext.current!==undefined){
      canvasContext.current.clearRect(0,0, image.width, image.height)
      canvasContext.current.canvas.width = image.width
      canvasContext.current.canvas.height = image.height
      for (const comment of commentsListRef.current) {
        canvasContext.current.strokeStyle = comment.color
        canvasContext.current.lineWidth = comment.lineWidth
        canvasContext.current.strokeRect(comment.leftTopXY[0], comment.leftTopXY[1], comment.width, comment.height)
      }
    }
  }
  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    drawBoxes()
  }, [])



  return (<div style={{position: "relative"}}>
    <img id={"backgroundPhotoLayer"} style={{position:"absolute", left: "0", top:"0", zIndex:"0"}} src={image.src}  alt={""} width={image.width} height={image.height}/>
    <canvas style={{position:"absolute", left: "0", top:"0", zIndex:"1"}} id={"commentsLayer"} onMouseUp={handleMouseUp} onMouseMove={handleMouseMove} onMouseDown={handleMouseDown} ref={canvasRef} height={image.height} width={image.width}/>
  </div>)
}