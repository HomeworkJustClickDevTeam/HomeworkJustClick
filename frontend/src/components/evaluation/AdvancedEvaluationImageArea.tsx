import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useWindowSize } from "../customHooks/useWindowSize"
import { CanvasActionsType } from "../../types/CanvasActionsType"
import { CommentActionsType } from "../../types/CommentActionsType"
import { cursors } from "../../assets/cursors"
import { he } from "date-fns/locale"

export const AdvancedEvaluationImageArea = ({drawOnCanvasRunner, editable, image, chosenComment, chosenCommentFrameWidth, drawnComments, width, height}:{
  width:number|undefined,
  height:number|undefined,
  image: HTMLImageElement,
  drawOnCanvasRunner:boolean
  editable:boolean,
  chosenComment?: CommentInterface|undefined,
  setChosenComment?: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void,
  chosenCommentFrameWidth?:number|undefined,
  drawnComments:AdvancedEvaluationImageCommentInterface[],}) =>{
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const mouseStartX = useRef<null|number>(null)
  const mouseStartY = useRef<null|number>(null)
  const commentIndex = useRef<null|number>(null)
  const commentPreviousState = useRef<null|AdvancedEvaluationImageCommentInterface>(null)
  const canvasAction = useRef<CanvasActionsType>("hovering")
  const commentAction = useRef<CommentActionsType>("noHover")

  const handleCommentResizing = (mouseX:number, mouseY:number) => {
    if(commentIndex.current !== null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null) {
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      switch (commentAction.current){
        case "leftTopCornerHovered":
          drawnComments[commentIndex.current].leftTopXY[0] += moveDiffX
          drawnComments[commentIndex.current].leftTopXY[1] += moveDiffY
          drawnComments[commentIndex.current].width -= moveDiffX
          drawnComments[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "topLineHovered":
          drawnComments[commentIndex.current].leftTopXY[1] += moveDiffY
          drawnComments[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "rightTopCornerHovered":
          drawnComments[commentIndex.current].leftTopXY[1] += moveDiffY
          drawnComments[commentIndex.current].width += moveDiffX
          drawnComments[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "rightLineHovered":
          drawnComments[commentIndex.current].width += moveDiffX
          drawOnCanvas()
          break
        case "rightBottomCornerHovered":
          drawnComments[commentIndex.current].width += moveDiffX
          drawnComments[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "bottomLineHovered":
          drawnComments[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "leftBottomCornerHovered":
          drawnComments[commentIndex.current].leftTopXY[0] += moveDiffX
          drawnComments[commentIndex.current].width -= moveDiffX
          drawnComments[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "leftLineHovered":
          drawnComments[commentIndex.current].leftTopXY[0] += moveDiffX
          drawnComments[commentIndex.current].width -= moveDiffX
          drawOnCanvas()
          break
      }
    }
  }
  const checkCollision = (topLeftX:number, topLeftY:number, width:number, height:number, movedCommentIndex:number) => {
    let j= 0
    for(const comment of drawnComments){
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
    if(chosenComment !== undefined && canvasRef.current !== undefined && canvasRef.current !== null && chosenCommentFrameWidth!==undefined){
      let newComment:AdvancedEvaluationImageCommentInterface = {...JSON.parse(JSON.stringify(chosenComment)), leftTopXY:[mouseX, mouseY], width: 1, height:1, lineWidth: chosenCommentFrameWidth, canvasHeight:canvasRef.current.height, canvasWidth:canvasRef.current.width}
      commentIndex.current = drawnComments.push(newComment) - 1
    }
  }
  const handleCommentSizeChangeWhileItBeingCreated = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      drawnComments[commentIndex.current].width += moveDiffX
      drawnComments[commentIndex.current].height += moveDiffY
      drawOnCanvas()
    }
  }
  const handleCommentMove = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      drawnComments[commentIndex.current].leftTopXY[0] += moveDiffX
      drawnComments[commentIndex.current].leftTopXY[1] += moveDiffY
      drawOnCanvas()
    }
  }
  const handleCommentDeletion = (draw:boolean) => {
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      if(commentIndex.current !== null){
        drawnComments.splice(commentIndex.current, 1)
        return true
      }
    }
    return draw
  }
  const handleCursorAppearanceChange = () => {
    if (canvasRef.current !== null) {
      switch (commentAction.current) {
        case "leftTopCornerHovered":
          canvasRef.current.style.cursor = cursors.nwseResize
          break
        case "rightTopCornerHovered":
          canvasRef.current.style.cursor = cursors.neswResize
          break
        case "leftBottomCornerHovered":
          canvasRef.current.style.cursor = cursors.neswResize
          break
        case "rightBottomCornerHovered":
          canvasRef.current.style.cursor = cursors.nwseResize
          break
        case "topLineHovered":
          canvasRef.current.style.cursor = cursors.nsResize
          break
        case "bottomLineHovered":
          canvasRef.current.style.cursor = cursors.nsResize
          break
        case "leftLineHovered":
          canvasRef.current.style.cursor = cursors.ewResize
          break
        case "rightLineHovered":
          canvasRef.current.style.cursor = cursors.ewResize
          break
        case "centerHovered":
          canvasRef.current.style.cursor = cursors.move
          break
        case "noHover":
          canvasRef.current.style.cursor = cursors.default
          break
      }
    }
  }
  const calculateCoordsOnCanvas = (x:number, y:number) => {
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
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(x, y)
      const moveDiffX = mouseXOnCanvas - mouseStartX.current
      const moveDiffY = mouseYOnCanvas - mouseStartY.current
      mouseStartX.current = mouseXOnCanvas
      mouseStartY.current = mouseYOnCanvas
      return {moveDiffX, moveDiffY}
    }
    return {moveDiffX: 0, moveDiffY: 0}
  }
  const checkIfOutOfCanvas = (initialDrawValue:boolean) =>{
    let draw = initialDrawValue
    if(commentIndex.current !== null && commentPreviousState.current !== null && canvasRef.current!==null && canvasRef.current!==undefined) {
      if (drawnComments[commentIndex.current].leftTopXY[0] < 0) { //check if out of canvas
        drawnComments[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
        draw = true
      }
      if (drawnComments[commentIndex.current].leftTopXY[1] < 0) {//check if out of canvas
        drawnComments[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
        draw = true
      }
      if (drawnComments[commentIndex.current].leftTopXY[0] + drawnComments[commentIndex.current].width > canvasRef.current.width) {//check if out of canvas
        drawnComments[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
        draw = true
      }
      if (drawnComments[commentIndex.current].leftTopXY[1] + drawnComments[commentIndex.current].height > canvasRef.current.height) {//check if out of canvas
        drawnComments[commentIndex.current].leftTopXY = [...commentPreviousState.current.leftTopXY]
        draw = true
      }
    }
    return draw
  }
  const normalizeCommentValues = () => {
    if (commentIndex.current !== null) {
      if (drawnComments[commentIndex.current].height <= 0) {//minus height removal
        drawnComments[commentIndex.current].height = Math.abs(drawnComments[commentIndex.current].height)
        drawnComments[commentIndex.current].leftTopXY[1] -= drawnComments[commentIndex.current].height
      }
      if (drawnComments[commentIndex.current].width <= 0) {//minus width removal
        drawnComments[commentIndex.current].width = Math.abs(drawnComments[commentIndex.current].width)
        drawnComments[commentIndex.current].leftTopXY[0] -= drawnComments[commentIndex.current].width
      }
    }
  }
  const calculateImageSizeWithAspectRatio = (targetWidth?:number, targetHeight?:number) => {
    const imageProportions = image.width/image.height
    if(targetWidth!==undefined && targetHeight===undefined){
      return targetWidth/imageProportions
    }
    else if (targetWidth===undefined && targetHeight!==undefined){
      return targetHeight*imageProportions
    }
    else{
      return 1
    }
  }
  const setPartOfCommentHovered = (mouseX:number, mouseY:number, clickedCommentIndex?:number|null) => {
    for (let i = 0; i < drawnComments.length; i++) {
      const hoveredComment = JSON.parse(JSON.stringify(drawnComments[i]))
      const hoveredCommentLeftTopXY = [...hoveredComment.leftTopXY]
      const hoveredCommentRightTopXY = [
        hoveredComment.leftTopXY[0] + hoveredComment.width,
        hoveredComment.leftTopXY[1]]
      const hoveredCommentLeftBottomXY = [
        hoveredComment.leftTopXY[0],
        hoveredComment.leftTopXY[1] + hoveredComment.height
      ]
      const hoveredCommentRightBottomXY = [
        hoveredComment.leftTopXY[0] + hoveredComment.width,
        hoveredComment.leftTopXY[1] + hoveredComment.height
      ]
      if(clickedCommentIndex === i) continue
      if ((hoveredCommentLeftTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentLeftTopXY[1] + hoveredComment.lineWidth)) {//left top corner hovered
        commentAction.current = "leftTopCornerHovered"
        return i
      } else if ((hoveredCommentRightTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentRightTopXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightTopXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "rightTopCornerHovered"
        return i
      } else if ((hoveredCommentLeftBottomXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentLeftBottomXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentLeftBottomXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentLeftBottomXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "leftBottomCornerHovered"
        return i
      } else if ((hoveredCommentRightBottomXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "rightBottomCornerHovered"
        return i
      } else if ((hoveredCommentLeftTopXY[0] + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentRightTopXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightTopXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "topLineHovered"
        return i
      } else if ((hoveredCommentLeftBottomXY[0] + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "bottomLineHovered"
        return i
      } else if ((hoveredCommentLeftTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1] + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentLeftBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "leftLineHovered"
        return i
      } else if ((hoveredCommentRightTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentRightTopXY[1] + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "rightLineHovered"
        return i
      } else if ((hoveredCommentLeftTopXY[0] + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1] + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "centerHovered"
        return i
      }
    }
    commentAction.current = "noHover"
    return null
  }
  const scaleCommentsToImageDimensions = () => {
    if(canvasRef.current!==null && canvasRef.current!==undefined){
      for(const comment of drawnComments){
        const commentsProportionsWidth = canvasRef.current.width/comment.canvasWidth
        const commentsProportionsHeight = canvasRef.current.height/comment.canvasHeight
        comment.leftTopXY[0] *= commentsProportionsWidth
        comment.leftTopXY[1] *= commentsProportionsHeight
        comment.width *= commentsProportionsWidth
        comment.height *= commentsProportionsHeight
        comment.canvasWidth = canvasRef.current.width
        comment.canvasHeight = canvasRef.current.height
      }
    }
  }
  const calculateCanvasSize = () => {
    if(height !== undefined && width !== undefined){
      if(image.height <= height && image.width <= width){
        return {calculatedHeight: image.height, calculatedWidth: image.width}
      }
      else if(image.height > height && image.width <= width){
        return {calculatedWidth: calculateImageSizeWithAspectRatio(undefined, height), calculatedHeight: height}
      }
      else if(image.height <= height, image.width > width){
        return {calculatedWidth: width, calculatedHeight: calculateImageSizeWithAspectRatio(width, undefined)}
      }
      else{
        return {calculatedWidth: calculateImageSizeWithAspectRatio(undefined, height), calculatedHeight: height}
      }
    }
    return {calculatedHeight: image.height, calculatedWidth: image.width}
  }
  const runBackCommentValues = (initialDrawValue:boolean) => {
    let draw = initialDrawValue
    if(commentIndex.current !== null && commentPreviousState.current !== null) {
      if(checkCollision(
        drawnComments[commentIndex.current].leftTopXY[0],
        drawnComments[commentIndex.current].leftTopXY[1],
        drawnComments[commentIndex.current].width,
        drawnComments[commentIndex.current].height,
        commentIndex.current))
      {
        drawnComments[commentIndex.current] = JSON.parse(JSON.stringify(commentPreviousState.current))
        draw = true
      }
    }
    return draw
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    event.preventDefault()
    event.stopPropagation()
    if(!editable) return
    console.log(canvasAction.current, commentAction.current)
    if(canvasAction.current === "commentDeleting") {
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY)
      commentIndex.current = setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
      handleCursorAppearanceChange()
    }
    else if(canvasAction.current === "commentDragging"){
      handleCommentMove(event.clientX, event.clientY)
    }
    else if(canvasAction.current === "commentCreating") {
      handleCommentSizeChangeWhileItBeingCreated(event.clientX, event.clientY)
    }
    else if(canvasAction.current === "commentResizing"){
      handleCommentResizing(event.clientX, event.clientY)
    }
    else if(canvasAction.current === "hovering"){
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY)
      commentIndex.current = setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
      handleCursorAppearanceChange()
    }
  }
  const handleMouseDown = (event:React.MouseEvent) => {
    event.preventDefault()
    event.stopPropagation()
    if(!editable) return
    else if(event.button === 2){//check if rmb used
      canvasAction.current = "commentDeleting"
    }
    else if(commentAction.current==="centerHovered"){
      canvasAction.current = "commentDragging"
    }
    else if(commentAction.current==="leftTopCornerHovered" ||
      commentAction.current==="topLineHovered" ||
      commentAction.current==="rightTopCornerHovered" ||
      commentAction.current==="rightLineHovered" ||
      commentAction.current==="rightBottomCornerHovered" ||
      commentAction.current==="bottomLineHovered" ||
      commentAction.current==="leftBottomCornerHovered" ||
      commentAction.current==="leftLineHovered"){
      canvasAction.current = "commentResizing"
    }
    else if (chosenComment !== undefined) {
      canvasAction.current = "commentCreating"
    }
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateCoordsOnCanvas(event.clientX, event.clientY)
      mouseStartX.current = mouseXOnCanvas
      mouseStartY.current = mouseYOnCanvas
      if (canvasAction.current === "commentDragging") {
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(drawnComments[commentIndex.current]))//deep copy
      }
      else if(canvasAction.current === "commentCreating"){
        handleNewCommentCreation(mouseStartX.current, mouseStartY.current)
      }
      else if(canvasAction.current === "commentResizing"){
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(drawnComments[commentIndex.current]))
      }
    }
  }
  const handleMouseUp = (event:React.MouseEvent) =>{
    event.preventDefault()
    event.stopPropagation()
    if(!editable) return
    let draw = false
    if(canvasAction.current === "commentDragging"){
      if(commentIndex.current !== null && commentPreviousState.current !== null) {
        draw = checkIfOutOfCanvas(draw)
        draw = runBackCommentValues(draw)
      }
    }
    else if(canvasAction.current === "commentCreating"){//mouse up while creating new comment
      if(commentIndex.current !== null) {
        normalizeCommentValues()
        if(checkCollision(
          drawnComments[commentIndex.current].leftTopXY[0],
          drawnComments[commentIndex.current].leftTopXY[1],
          drawnComments[commentIndex.current].width,
          drawnComments[commentIndex.current].height,
          commentIndex.current))
        {//check if mouse up on existing comment cancel is so (comment that we are creating is already in the array)
          drawnComments.splice(commentIndex.current, 1)
          draw = true
        }
      }
    }
    else if(canvasAction.current === "commentResizing"){
      normalizeCommentValues()
      draw = runBackCommentValues(draw)
    }
    else if(canvasAction.current === "commentDeleting" && event.button === 2){
      draw = handleCommentDeletion(draw)
    }
    if (draw) {
      drawOnCanvas()
    }
    commentIndex.current = null
    commentPreviousState.current = null
    mouseStartY.current = null
    mouseStartX.current = null
    canvasAction.current = "hovering"
    const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY)
    setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
    handleCursorAppearanceChange()
    console.log(drawnComments, canvasAction.current)
  }
  const drawOnCanvas = () => {
    if(canvasContext.current!==null && canvasContext.current!==undefined){
      const {calculatedHeight, calculatedWidth} = calculateCanvasSize()
      canvasContext.current.canvas.height = calculatedHeight
      canvasContext.current.canvas.width = calculatedWidth
      canvasContext.current.clearRect(0,0, calculatedWidth, calculatedHeight)
      if(image.complete){
        canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
      }
      else
        image.onload = () =>{
          if(canvasContext.current!==null && canvasContext.current!==undefined)
            canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
        }
      for (const comment of drawnComments) {
        canvasContext.current.strokeStyle = comment.defaultColor
        canvasContext.current.lineWidth = comment.lineWidth
        canvasContext.current.strokeRect(comment.leftTopXY[0], comment.leftTopXY[1], comment.width, comment.height)
      }
    }
  }

  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    scaleCommentsToImageDimensions()
    drawOnCanvas()
  }, [])

  useEffect(() => {
    scaleCommentsToImageDimensions()
    drawOnCanvas()
  }, [drawOnCanvasRunner, width, height])



  return (
    <canvas onContextMenu={(event) => {event.preventDefault(); event.stopPropagation()}}
            onMouseOut={handleMouseUp}
            id={"commentsLayer"}
            onMouseUp={handleMouseUp}
            onMouseMove={handleMouseMove}
            onMouseDown={handleMouseDown}
            ref={canvasRef}
    />)
}