import React, { CanvasHTMLAttributes, MouseEventHandler, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { CommentInterface } from "../../types/CommentInterface"

export const CanvasLogic = ({image, commentsList, setCommentsList, chosenComment, setChosenComment}:{
  image:HTMLImageElement,
  commentsList: AdvancedEvaluationImageCommentInterface[],
  setCommentsList: (comments:AdvancedEvaluationImageCommentInterface[]) => void,
  chosenComment:CommentInterface|undefined,
  setChosenComment: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void}) => {
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const commentsListRef = useRef(commentsList)
  let mouseStartX:null|number = null
  let mouseStartY:null|number = null
  let commentNumber:null|number = null

  const handleNewCommentCreation = () =>{
    if(chosenComment !== undefined && mouseStartX!==null && mouseStartY !== null){
      let newComment:AdvancedEvaluationImageCommentInterface = {...chosenComment, leftTopXY:[mouseStartX, mouseStartY], width: 0, height:0, lineWidth: 2}
      commentNumber = commentsListRef.current.push(newComment) - 1
    }
  }
  const calculateStartingCoordsOnCanvas = (x:number, y:number) => {
    if(canvasContext.current !== null && canvasContext.current !== undefined && canvasRef.current !== null) {
      const canvasOffsets = canvasRef.current.getBoundingClientRect()
      const mouseXOnCanvas = x - canvasOffsets.x
      const mouseYOnCanvas = y - canvasOffsets.y
      return {mouseXOnCanvas, mouseYOnCanvas}
    }
    return {mouseXOnCanvas:0, mouseYOnCanvas:0}
  }
  const updateMouseCoords = (x:number, y:number) => {
    if(canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateStartingCoordsOnCanvas(x, y)
      const moveDiffX = mouseXOnCanvas - mouseStartX
      const moveDiffY = mouseYOnCanvas - mouseStartY
      mouseStartX = mouseXOnCanvas
      mouseStartY = mouseYOnCanvas
      return {moveDiffX, moveDiffY}
    }
    return {moveDiffX: 0, moveDiffY: 0}
  }
  const chooseCommentToMove = () => {
    if(mouseStartX !== null && mouseStartY !== null){
      for (let i = 0; i < commentsList.length; i++){
        const comment = commentsList[i]
        if(
          mouseStartX >= comment.leftTopXY[0] &&
          mouseStartX <= comment.leftTopXY[0] + comment.width &&
          mouseStartY >= comment.leftTopXY[1] &&
          mouseStartY <= comment.leftTopXY[1] + comment.height
        ){
          commentNumber = i
          break
        }
      }
    }
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    if(chosenComment === undefined){
      if(commentNumber!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentNumber].leftTopXY[0] += moveDiffX
        commentsListRef.current[commentNumber].leftTopXY[1] += moveDiffY
        drawBoxes()
      }
    }
    else {
      if(commentNumber!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentNumber].width += moveDiffX
        commentsListRef.current[commentNumber].height += moveDiffY
        drawBoxes()
      }
    }
  }
  const handleMouseDown = (event:React.MouseEvent) => {
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateStartingCoordsOnCanvas(event.clientX, event.clientY)
      mouseStartX = mouseXOnCanvas
      mouseStartY = mouseYOnCanvas
      if (chosenComment === undefined) {
        chooseCommentToMove()
      } else {
        handleNewCommentCreation()
      }
    }
  }
  const handleMouseUp = () =>{
    commentNumber = null
    for(const comment of commentsListRef.current){
      let draw = false
      if(comment.height <= 0){
        comment.height = Math.abs(comment.height)
        comment.leftTopXY[1] -= comment.height
      }
      if(comment.width <= 0){
        comment.width = Math.abs(comment.width)
        comment.leftTopXY[0] -= comment.width
      }
      if(comment.leftTopXY[0] < 0){
        comment.leftTopXY[0] = 0
        draw = true
      }
      if(comment.leftTopXY[1] < 0){
        comment.leftTopXY[1] = 0
        draw = true
      }
      if(comment.leftTopXY[0] + comment.width > image.width){
        comment.leftTopXY[0] = image.width - comment.width
        draw = true
      }
      if(comment.leftTopXY[1] + comment.height > image.height){
        comment.leftTopXY[1] = image.height - comment.height
        draw = true
      }
      if(draw){
        drawBoxes()
      }
    }
    setCommentsList(commentsListRef.current)
    setChosenComment(undefined)
    console.log(commentsListRef)
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
    <img id={"backgroundPhotoLayer"} style={{position:"absolute", zIndex:"0"}} src={image.src}  alt={""} width={image.width} height={image.height}/>
    <canvas style={{position:"absolute", zIndex:"1"}} id={"commentsLayer"} onMouseUp={handleMouseUp} onMouseMove={handleMouseMove} onMouseDown={handleMouseDown} ref={canvasRef} height={image.height} width={image.width}/>
  </div>)
}