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
  const mouseStartX = useRef<null|number>(null)
  const mouseStartY = useRef<null|number>(null)
  const commentNumber = useRef<null|number>(null)
  const commentUnchangedLeftTopXY = useRef<null|number[]>(null)

  const checkCollision = (topLeftX:number, topLeftY:number, width:number, height:number, movedCommentId:number) => {
    const commentToCheckX = [topLeftX, topLeftX+width, topLeftX+width, topLeftX]
    const commentToCheckY = [topLeftY, topLeftY, topLeftY+height, topLeftY+height]
    for(const comment of commentsListRef.current){
      for(let i=0; i<4; i++){
        if(
          (comment.leftTopXY[0] <= commentToCheckX[i]) && (commentToCheckX[i] <= (comment.leftTopXY[0] + comment.width)) &&
          (comment.leftTopXY[1] <= commentToCheckY[i]) && (commentToCheckY[i] <= (comment.leftTopXY[1] + comment.height)) &&
          (comment.id !== movedCommentId)
        ){
          return true
        }
      }
    }
    return false
  }
  const handleNewCommentCreation = (mouseX:number, mouseY:number) =>{
    if(chosenComment !== undefined){
      let newComment:AdvancedEvaluationImageCommentInterface = {...chosenComment, leftTopXY:[mouseX, mouseY], width: 1, height:1, lineWidth: 2}
      commentNumber.current = commentsListRef.current.push(newComment) - 1
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
    if(canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateStartingCoordsOnCanvas(x, y)
      const moveDiffX = mouseXOnCanvas - mouseStartX.current
      const moveDiffY = mouseYOnCanvas - mouseStartY.current
      mouseStartX.current = mouseXOnCanvas
      mouseStartY.current = mouseYOnCanvas
      return {moveDiffX, moveDiffY}
    }
    return {moveDiffX: 0, moveDiffY: 0}
  }
  const chooseCommentToMove = (mouseX:number, mouseY:number) => {

    for (let i = 0; i < commentsListRef.current.length; i++){
      const comment = commentsListRef.current[i]
      if(
        mouseX >= comment.leftTopXY[0] &&
        mouseX <= comment.leftTopXY[0] + comment.width &&
        mouseY >= comment.leftTopXY[1] &&
        mouseY <= comment.leftTopXY[1] + comment.height
      ){
        commentUnchangedLeftTopXY.current = [...comment.leftTopXY]
        return i
      }
    }
    return null
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    if(chosenComment === undefined){
      if(commentNumber.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentNumber.current].leftTopXY[0] += moveDiffX
        commentsListRef.current[commentNumber.current].leftTopXY[1] += moveDiffY
        drawBoxes()
      }
    }
    else {
      if(commentNumber.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentNumber.current].width += moveDiffX
        commentsListRef.current[commentNumber.current].height += moveDiffY
        drawBoxes()
      }
    }
  }
  const handleMouseDown = (event:React.MouseEvent) => {
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateStartingCoordsOnCanvas(event.clientX, event.clientY)
      mouseStartX.current = mouseXOnCanvas
      mouseStartY.current = mouseYOnCanvas
      if (chosenComment === undefined) {
        commentNumber.current = chooseCommentToMove(mouseStartX.current, mouseStartY.current)
      } else {
        if(chooseCommentToMove(mouseStartX.current, mouseStartY.current) === null){
          handleNewCommentCreation(mouseStartX.current, mouseStartY.current)
        }
      }
    }
  }
  const handleMouseUp = () =>{
    let draw = false
    if(chosenComment === undefined){
      if(commentNumber.current !== null && commentUnchangedLeftTopXY.current !== null) {
        if (commentsListRef.current[commentNumber.current].leftTopXY[0] < 0) { //check if out of canvas
          commentsListRef.current[commentNumber.current].leftTopXY = [...commentUnchangedLeftTopXY.current]
          draw = true
        }
        if (commentsListRef.current[commentNumber.current].leftTopXY[1] < 0) {//check if out of canvas
          commentsListRef.current[commentNumber.current].leftTopXY = [...commentUnchangedLeftTopXY.current]
          draw = true
        }
        if (commentsListRef.current[commentNumber.current].leftTopXY[0] + commentsListRef.current[commentNumber.current].width > image.width) {//check if out of canvas
          commentsListRef.current[commentNumber.current].leftTopXY = [...commentUnchangedLeftTopXY.current]
          draw = true
        }
        if (commentsListRef.current[commentNumber.current].leftTopXY[1] + commentsListRef.current[commentNumber.current].height > image.height) {//check if out of canvas
          commentsListRef.current[commentNumber.current].leftTopXY = [...commentUnchangedLeftTopXY.current]
          draw = true
        }
        if(checkCollision(commentsListRef.current[commentNumber.current].leftTopXY[0],
          commentsListRef.current[commentNumber.current].leftTopXY[1],
          commentsListRef.current[commentNumber.current].width,
          commentsListRef.current[commentNumber.current].height,
          commentsListRef.current[commentNumber.current].id))
        {
          commentsListRef.current[commentNumber.current].leftTopXY = [...commentUnchangedLeftTopXY.current]
          draw = true
        }
      }
    }
    else{//mouse up while creating new comment
      if(commentNumber.current !== null) {
        if (commentsListRef.current[commentNumber.current].height <= 0) {//minus height removal
          commentsListRef.current[commentNumber.current].height = Math.abs(commentsListRef.current[commentNumber.current].height)
          commentsListRef.current[commentNumber.current].leftTopXY[1] -= commentsListRef.current[commentNumber.current].height
        }
        if (commentsListRef.current[commentNumber.current].width <= 0) {//minus width removal
          commentsListRef.current[commentNumber.current].width = Math.abs(commentsListRef.current[commentNumber.current].width)
          commentsListRef.current[commentNumber.current].leftTopXY[0] -= commentsListRef.current[commentNumber.current].width
        }
        if(mouseStartX.current !== null && mouseStartY.current !== null){
          if (chooseCommentToMove(mouseStartX.current, mouseStartY.current) !== commentNumber.current) {//check if mouse up on existing comment cancel is so (comment that we are creating is already in the array)
            commentsListRef.current.splice(commentNumber.current, 1)
            draw = true
          }
        }
      }
    }
    if (draw) {
      drawBoxes()
    }
    commentNumber.current = null
    commentUnchangedLeftTopXY.current = null
    setCommentsList(commentsListRef.current)
    setChosenComment(undefined)
    mouseStartY.current = null
    mouseStartX.current = null
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
    <canvas onMouseOut={handleMouseUp} style={{position:"absolute", zIndex:"1"}} id={"commentsLayer"} onMouseUp={handleMouseUp} onMouseMove={handleMouseMove} onMouseDown={handleMouseDown} ref={canvasRef} height={image.height} width={image.width}/>
  </div>)
}