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
  const commentIndex = useRef<null|number>(null)
  const commentPreviousState = useRef<null|AdvancedEvaluationImageCommentInterface>(null)

  const checkCollision = (topLeftX:number, topLeftY:number, width:number, height:number, movedCommentIndex:number) => {
    let j= 0
    for(const comment of commentsListRef.current){
      if((movedCommentIndex !== j) &&
        (topLeftX <= comment.leftTopXY[0] + comment.width) &&
        (comment.leftTopXY[0] <= topLeftX + width) &&
        (topLeftY <= comment.leftTopXY[1] + comment.height) &&
        (comment.leftTopXY[1] <= topLeftY + height)){
        return true
      }
      j++
    }
    return false
  }
  const handleNewCommentCreation = (mouseX:number, mouseY:number) =>{
    if(chosenComment !== undefined){
      let newComment:AdvancedEvaluationImageCommentInterface = {...chosenComment, leftTopXY:[mouseX, mouseY], width: 1, height:1, lineWidth: 2}
      commentIndex.current = commentsListRef.current.push(newComment) - 1
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
  const chooseWhichCommentGotClicked = (mouseX:number, mouseY:number, clickedCommentIndex?:number|null) => {
    for (let i = 0; i < commentsListRef.current.length; i++){
      const comment = commentsListRef.current[i]
      if(
        clickedCommentIndex !== i &&
        mouseX >= comment.leftTopXY[0] &&
        mouseX <= comment.leftTopXY[0] + comment.width &&
        mouseY >= comment.leftTopXY[1] &&
        mouseY <= comment.leftTopXY[1] + comment.height
      ){
        commentPreviousState.current = JSON.parse(JSON.stringify(comment))//deep copy
        return i
      }
    }
    return null
  }
  const handleRightClick = (event:React.MouseEvent) =>{
    event.preventDefault()
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateStartingCoordsOnCanvas(event.clientX, event.clientY)
      const rightClickedComment = chooseWhichCommentGotClicked(mouseXOnCanvas, mouseYOnCanvas)
      if(rightClickedComment !== null){
        commentsListRef.current.splice(rightClickedComment, 1)
        drawBoxes()
      }
    }
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    if(chosenComment === undefined){
      if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentIndex.current].leftTopXY[0] += moveDiffX
        commentsListRef.current[commentIndex.current].leftTopXY[1] += moveDiffY
        drawBoxes()
      }
    }
    else {
      if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
        const {moveDiffX, moveDiffY} = updateMouseCoords(event.clientX, event.clientY)
        commentsListRef.current[commentIndex.current].width += moveDiffX
        commentsListRef.current[commentIndex.current].height += moveDiffY
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
        commentIndex.current = chooseWhichCommentGotClicked(mouseStartX.current, mouseStartY.current, commentIndex.current)
      } else {
        if(chooseWhichCommentGotClicked(mouseStartX.current, mouseStartY.current, commentIndex.current) === null){
          handleNewCommentCreation(mouseStartX.current, mouseStartY.current)
        }
      }
    }
  }
  const handleMouseUp = () =>{
    let draw = false
    if(chosenComment === undefined){
      if(commentIndex.current !== null && commentPreviousState.current !== null) {
        if (commentsListRef.current[commentIndex.current].leftTopXY[0] < 0) { //check if out of canvas
          commentsListRef.current[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
          draw = true
        }
        if (commentsListRef.current[commentIndex.current].leftTopXY[1] < 0) {//check if out of canvas
          commentsListRef.current[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
          draw = true
        }
        if (commentsListRef.current[commentIndex.current].leftTopXY[0] + commentsListRef.current[commentIndex.current].width > image.width) {//check if out of canvas
          commentsListRef.current[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
          draw = true
        }
        if (commentsListRef.current[commentIndex.current].leftTopXY[1] + commentsListRef.current[commentIndex.current].height > image.height) {//check if out of canvas
          commentsListRef.current[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
          draw = true
        }
        if(checkCollision(commentsListRef.current[commentIndex.current].leftTopXY[0],
          commentsListRef.current[commentIndex.current].leftTopXY[1],
          commentsListRef.current[commentIndex.current].width,
          commentsListRef.current[commentIndex.current].height,
          commentIndex.current))
        {
          commentsListRef.current[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
          draw = true
        }
      }
    }
    else{//mouse up while creating new comment
      if(commentIndex.current !== null) {
        if (commentsListRef.current[commentIndex.current].height <= 0) {//minus height removal
          commentsListRef.current[commentIndex.current].height = Math.abs(commentsListRef.current[commentIndex.current].height)
          commentsListRef.current[commentIndex.current].leftTopXY[1] -= commentsListRef.current[commentIndex.current].height
        }
        if (commentsListRef.current[commentIndex.current].width <= 0) {//minus width removal
          commentsListRef.current[commentIndex.current].width = Math.abs(commentsListRef.current[commentIndex.current].width)
          commentsListRef.current[commentIndex.current].leftTopXY[0] -= commentsListRef.current[commentIndex.current].width
        }
        if(mouseStartX.current !== null && mouseStartY.current !== null){
          if (chooseWhichCommentGotClicked(mouseStartX.current, mouseStartY.current, commentIndex.current) !== null) {//check if mouse up on existing comment cancel is so (comment that we are creating is already in the array)
            commentsListRef.current.splice(commentIndex.current, 1)
            draw = true
          }
        }
      }
    }
    if (draw) {
      drawBoxes()
    }
    commentIndex.current = null
    commentPreviousState.current = null
    setCommentsList(commentsListRef.current)
    setChosenComment(undefined)
    mouseStartY.current = null
    mouseStartX.current = null
    console.log(commentsListRef.current)
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
    <canvas onContextMenu={handleRightClick} onMouseOut={handleMouseUp} style={{position:"absolute", zIndex:"1"}} id={"commentsLayer"} onMouseUp={handleMouseUp} onMouseMove={handleMouseMove} onMouseDown={handleMouseDown} ref={canvasRef} height={image.height} width={image.width}/>
  </div>)
}