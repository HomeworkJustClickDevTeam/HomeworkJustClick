import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useEffect, useImperativeHandle, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useWindowSize } from "../customHooks/useWindowSize"
import { CanvasActionsType } from "../../types/CanvasActionsType"
import { CommentActionsType } from "../../types/CommentActionsType"
import { cursors } from "../../assets/cursors"
import { he } from "date-fns/locale"
import {
  changeCommentImagePostgresService,
  createCommentImageWithFilePostgresService,
  deleteCommentImagePostgresService
} from "../../services/postgresDatabaseServices"

interface AdvancedEvaluationImageAreaInterface{
  width:number|undefined,
  height:number|undefined,
  image: HTMLImageElement,
  editable:boolean,
  chosenComment?: CommentInterface|undefined,
  chosenCommentFrameWidth?:number|undefined,
  drawnComments:MutableRefObject<AdvancedEvaluationImageCommentInterface[]>
  fileId:number
}
export const AdvancedEvaluationImageArea = React.forwardRef<any, AdvancedEvaluationImageAreaInterface>(({
                                                                                                          fileId,
                                                                                                          editable,
                                                                                                          image,
                                                                                                          chosenComment,
                                                                                                          chosenCommentFrameWidth,
                                                                                                          drawnComments,
                                                                                                          width,
                                                                                                          height}, advancedEvaluationImageAreaRef) =>{
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const mouseStartX = useRef<null|number>(null)
  const mouseStartY = useRef<null|number>(null)
  const commentIndex = useRef<null|number>(null)
  const commentPreviousState = useRef<null|AdvancedEvaluationImageCommentInterface>(null)
  const canvasAction = useRef<CanvasActionsType>("hovering")
  const commentAction = useRef<CommentActionsType>("noHover")
  const mouseDownTimestamp = useRef<number|null>(null)

  useImperativeHandle(advancedEvaluationImageAreaRef, () => ({
    drawOnCanvas() {
      drawOnCanvas()
    }
  }))//use with caution
  const handleCommentResizing = (mouseX:number, mouseY:number) => {
    if(commentIndex.current !== null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null) {
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      switch (commentAction.current){
        case "leftTopCornerHovered":
          drawnComments.current[commentIndex.current].leftTopX += moveDiffX
          drawnComments.current[commentIndex.current].leftTopY += moveDiffY
          drawnComments.current[commentIndex.current].width -= moveDiffX
          drawnComments.current[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "topLineHovered":
          drawnComments.current[commentIndex.current].leftTopY += moveDiffY
          drawnComments.current[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "rightTopCornerHovered":
          drawnComments.current[commentIndex.current].leftTopY += moveDiffY
          drawnComments.current[commentIndex.current].width += moveDiffX
          drawnComments.current[commentIndex.current].height -= moveDiffY
          drawOnCanvas()
          break
        case "rightLineHovered":
          drawnComments.current[commentIndex.current].width += moveDiffX
          drawOnCanvas()
          break
        case "rightBottomCornerHovered":
          drawnComments.current[commentIndex.current].width += moveDiffX
          drawnComments.current[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "bottomLineHovered":
          drawnComments.current[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "leftBottomCornerHovered":
          drawnComments.current[commentIndex.current].leftTopX += moveDiffX
          drawnComments.current[commentIndex.current].width -= moveDiffX
          drawnComments.current[commentIndex.current].height += moveDiffY
          drawOnCanvas()
          break
        case "leftLineHovered":
          drawnComments.current[commentIndex.current].leftTopX += moveDiffX
          drawnComments.current[commentIndex.current].width -= moveDiffX
          drawOnCanvas()
          break
      }
    }
  }
  const updateCommentImageInDb = async (draw:boolean) => {
    if (commentIndex.current !== undefined && commentIndex.current !== null) {
      try {
        const response = await changeCommentImagePostgresService(drawnComments.current[commentIndex.current])
        if(!(response?.data !== undefined && response?.data !== null && response?.status === 200)){
          drawnComments.current[commentIndex.current] = JSON.parse(JSON.stringify(commentPreviousState.current))
          return true
        }
      }
      catch (error)
      {
        drawnComments.current[commentIndex.current] = JSON.parse(JSON.stringify(commentPreviousState.current))
        return true
      }
    }
    return draw
  }
  const saveCommentImageToDb = async (draw:boolean) => {
    if (commentIndex.current !== undefined && commentIndex.current !== null) {
      try {
        const response = await createCommentImageWithFilePostgresService(drawnComments.current[commentIndex.current])
        if (response?.data !== undefined && response?.data !== null && response?.status === 201) {
          drawnComments.current[commentIndex.current].id = response.data.id
        }
        else{
          drawnComments.current.splice(commentIndex.current, 1)
          return true
        }
      }
      catch (error)
        {
          drawnComments.current.splice(commentIndex.current, 1)
          return true
        }
    }
    return draw
  }
  const checkCollision = (topLeftX:number, topLeftY:number, width:number, height:number, movedCommentIndex:number) => {
    let j= 0
    for(const comment of drawnComments.current){
      if((movedCommentIndex !== j) &&
        (topLeftX <= comment.leftTopX + comment.width) &&
        (comment.leftTopX <= topLeftX + width) &&
        (topLeftY <= comment.leftTopY + comment.height) &&
        (comment.leftTopY <= topLeftY + height)){
        return true
      }
      j++
    }
    return false
  }
  const handleNewCommentCreation = (mouseX:number, mouseY:number) =>{
    if(chosenComment !== undefined && canvasRef.current !== undefined && canvasRef.current !== null && chosenCommentFrameWidth!==undefined){
      let newComment:AdvancedEvaluationImageCommentInterface = {
        id:-1, color: chosenComment.color, fileId: fileId,commentId:chosenComment.id, leftTopX: mouseX, leftTopY:mouseY, width: 1, height:1, lineWidth: chosenCommentFrameWidth, imgHeight:canvasRef.current.height, imgWidth:canvasRef.current.width}
      commentIndex.current = drawnComments.current.push(newComment) - 1

    }
  }
  const handleCommentSizeChangeWhileItBeingCreated = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      drawnComments.current[commentIndex.current].width += moveDiffX
      drawnComments.current[commentIndex.current].height += moveDiffY
      drawOnCanvas()
    }
  }
  const handleCommentMove = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      drawnComments.current[commentIndex.current].leftTopX += moveDiffX
      drawnComments.current[commentIndex.current].leftTopY += moveDiffY
      drawOnCanvas()
    }
  }
  const handleCommentDeletion = async (draw:boolean) => {
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      if(commentIndex.current !== null){
        const response = await deleteCommentImagePostgresService(drawnComments.current[commentIndex.current].id.toString())
        if(response.data !== undefined && response.data !== null && response.status === 200) {
          drawnComments.current.splice(commentIndex.current, 1)
          return true
        }
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
      if (drawnComments.current[commentIndex.current].leftTopX < 0) { //check if out of canvas
        drawnComments.current[commentIndex.current].leftTopX = commentPreviousState.current.leftTopX
        drawnComments.current[commentIndex.current].leftTopY = commentPreviousState.current.leftTopY
        draw = true
      }
      if (drawnComments.current[commentIndex.current].leftTopY < 0) {//check if out of canvas
        drawnComments.current[commentIndex.current].leftTopX = commentPreviousState.current.leftTopX
        drawnComments.current[commentIndex.current].leftTopY = commentPreviousState.current.leftTopY
        draw = true
      }
      if (drawnComments.current[commentIndex.current].leftTopX + drawnComments.current[commentIndex.current].width > canvasRef.current.width) {//check if out of canvas
        drawnComments.current[commentIndex.current].leftTopX = commentPreviousState.current.leftTopX
        drawnComments.current[commentIndex.current].leftTopY = commentPreviousState.current.leftTopY
        draw = true
      }
      if (drawnComments.current[commentIndex.current].leftTopY + drawnComments.current[commentIndex.current].height > canvasRef.current.height) {//check if out of canvas
        drawnComments.current[commentIndex.current].leftTopX = commentPreviousState.current.leftTopX
        drawnComments.current[commentIndex.current].leftTopY = commentPreviousState.current.leftTopY
        draw = true
      }
    }
    return draw
  }
  const normalizeCommentValues = () => {
    if (commentIndex.current !== null) {
      if (drawnComments.current[commentIndex.current].height <= 0) {//minus height removal
        drawnComments.current[commentIndex.current].height = Math.abs(drawnComments.current[commentIndex.current].height)
        drawnComments.current[commentIndex.current].leftTopY -= drawnComments.current[commentIndex.current].height
      }
      if (drawnComments.current[commentIndex.current].width <= 0) {//minus width removal
        drawnComments.current[commentIndex.current].width = Math.abs(drawnComments.current[commentIndex.current].width)
        drawnComments.current[commentIndex.current].leftTopX -= drawnComments.current[commentIndex.current].width
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
    for (let i = 0; i < drawnComments.current.length; i++) {
      const hoveredComment = JSON.parse(JSON.stringify(drawnComments.current[i]))
      const hoveredCommentLeftTopXY = [hoveredComment.leftTopX, hoveredComment.leftTopY]
      const hoveredCommentRightTopXY = [
        hoveredComment.leftTopX + hoveredComment.width,
        hoveredComment.leftTopY]
      const hoveredCommentLeftBottomXY = [
        hoveredComment.leftTopX,
        hoveredComment.leftTopY + hoveredComment.height
      ]
      const hoveredCommentRightBottomXY = [
        hoveredComment.leftTopX + hoveredComment.width,
        hoveredComment.leftTopY + hoveredComment.height
      ]
      if(clickedCommentIndex === i) continue
      if ((hoveredCommentLeftTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0]  + hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1]  - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentLeftTopXY[1]  + hoveredComment.lineWidth)) {//left top corner hovered
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
      } else if ((hoveredCommentLeftTopXY[0]  + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentRightTopXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightTopXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "topLineHovered"
        return i
      } else if ((hoveredCommentLeftBottomXY[0] + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] + hoveredComment.lineWidth)) {
        commentAction.current = "bottomLineHovered"
        return i
      } else if ((hoveredCommentLeftTopXY[0]  - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0]  + hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1]  + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentLeftBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "leftLineHovered"
        return i
      } else if ((hoveredCommentRightTopXY[0] - hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] + hoveredComment.lineWidth) &&
        (hoveredCommentRightTopXY[1] + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "rightLineHovered"
        return i
      } else if ((hoveredCommentLeftTopXY[0]  + hoveredComment.lineWidth <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - hoveredComment.lineWidth) &&
        (hoveredCommentLeftTopXY[1]  + hoveredComment.lineWidth <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - hoveredComment.lineWidth)) {
        commentAction.current = "centerHovered"
        return i
      }
    }
    commentAction.current = "noHover"
    return null
  }
  const scaleCommentsToImageDimensions = () => {
    if(canvasRef.current!==null && canvasRef.current!==undefined){
      for(const comment of drawnComments.current){
        const commentsProportionsWidth = canvasRef.current.width/comment.imgWidth
        const commentsProportionsHeight = canvasRef.current.height/comment.imgHeight
        comment.leftTopX *= commentsProportionsWidth
        comment.leftTopY *= commentsProportionsHeight
        comment.width *= commentsProportionsWidth
        comment.height *= commentsProportionsHeight
        comment.imgWidth = canvasRef.current.width
        comment.imgHeight = canvasRef.current.height
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
      else if(image.height <= height && image.width > width){
        return {calculatedWidth: width, calculatedHeight: calculateImageSizeWithAspectRatio(width, undefined)}
      }
      else{
        return {calculatedWidth: width, calculatedHeight: calculateImageSizeWithAspectRatio(width, undefined)}
      }
    }
    else{
      return {calculatedHeight: image.height, calculatedWidth: image.width}
    }
  }
  const runBackCommentValues = (initialDrawValue:boolean) => {
    let draw = initialDrawValue
    if(commentIndex.current !== null && commentPreviousState.current !== null) {
      if(checkCollision(
        drawnComments.current[commentIndex.current].leftTopX,
        drawnComments.current[commentIndex.current].leftTopY,
        drawnComments.current[commentIndex.current].width,
        drawnComments.current[commentIndex.current].height,
        commentIndex.current))
      {
        drawnComments.current[commentIndex.current] = JSON.parse(JSON.stringify(commentPreviousState.current))
        draw = true
      }
    }
    return draw
  }
  const handleMouseMove = (event:React.MouseEvent) => {
    event.preventDefault()
    event.stopPropagation()
    if(!editable) return
    if(canvasAction.current === "commentDeleting") {
      const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY)
      commentIndex.current = setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
      handleCursorAppearanceChange()
    }
    else if(canvasAction.current === "commentHolding"){
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
      canvasAction.current = "commentHolding"
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
      if (canvasAction.current === "commentHolding") {
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(drawnComments.current[commentIndex.current]))//deep copy
      }
      else if(canvasAction.current === "commentCreating"){
        handleNewCommentCreation(mouseStartX.current, mouseStartY.current)
      }
      else if(canvasAction.current === "commentResizing"){
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(drawnComments.current[commentIndex.current]))
      }
    }
    mouseDownTimestamp.current = event.timeStamp
  }
  const handleMouseUp = async (event: React.MouseEvent) => {
    event.preventDefault()
    event.stopPropagation()
    if (!editable) return
    let draw = false
    if (canvasAction.current === "commentHolding") {
      if(mouseDownTimestamp?.current !== null && (event.timeStamp - mouseDownTimestamp.current) < 150){
      }
      else if (commentIndex.current !== null && commentPreviousState.current !== null) {
        draw = checkIfOutOfCanvas(draw)
        draw = runBackCommentValues(draw)
        if(!draw)
          draw = await updateCommentImageInDb(draw)
      }
    } else if (canvasAction.current === "commentCreating") {//mouse up while creating new comment
      if (commentIndex.current !== null) {
        normalizeCommentValues()
        if (checkCollision(
          drawnComments.current[commentIndex.current].leftTopX,
          drawnComments.current[commentIndex.current].leftTopY,
          drawnComments.current[commentIndex.current].width,
          drawnComments.current[commentIndex.current].height,
          commentIndex.current)) {//check if mouse up on existing comment cancel is so (comment that we are creating is already in the array)
          drawnComments.current.splice(commentIndex.current, 1)
          draw = true
        } else {
          draw = await saveCommentImageToDb(draw)
        }
      }
    } else if (canvasAction.current === "commentResizing") {
      normalizeCommentValues()
      draw = runBackCommentValues(draw)
      if(!draw)
        draw = await updateCommentImageInDb(draw)
    } else if (canvasAction.current === "commentDeleting" && event.button === 2) {
      draw = await handleCommentDeletion(draw)
    }
    if (draw) {
      drawOnCanvas()
    }
    commentIndex.current = null
    commentPreviousState.current = null
    mouseStartY.current = null
    mouseStartX.current = null
    mouseDownTimestamp.current = null
    canvasAction.current = "hovering"
    const { mouseXOnCanvas, mouseYOnCanvas } = calculateCoordsOnCanvas(event.clientX, event.clientY)
    setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
    handleCursorAppearanceChange()
    console.log(drawnComments.current, canvasAction.current)
  }
  const drawOnCanvas = () => {
    scaleCommentsToImageDimensions()
    if(canvasContext.current!==null && canvasContext.current!==undefined){
      const {calculatedHeight, calculatedWidth} = calculateCanvasSize()
      canvasContext.current.canvas.height = calculatedHeight
      canvasContext.current.canvas.width = calculatedWidth
      canvasContext.current.globalAlpha = 1
      canvasContext.current.clearRect(0,0, calculatedWidth, calculatedHeight)
      if(image.complete){
        canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
      }
      else
        image.onload = () =>{
          if(canvasContext.current!==null && canvasContext.current!==undefined)
            canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
        }
      for (const comment of drawnComments.current) {
        canvasContext.current.fillStyle = comment.color
        canvasContext.current.lineWidth = comment.lineWidth
        canvasContext.current.globalAlpha = 0.5
        canvasContext.current.fillRect(comment.leftTopX, comment.leftTopY, comment.width, comment.height)
      }
    }
  }

  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    drawOnCanvas()
  }, [])

  useEffect(() => {
    drawOnCanvas()
  }, [width, height])



  return (
    <canvas onContextMenu={(event) => {
              event.preventDefault();
              event.stopPropagation()
              canvasAction.current = "commentDeleting"}}
            onMouseOut={handleMouseUp}
            id={"commentsLayer"}
            onMouseUp={handleMouseUp}
            onMouseMove={handleMouseMove}
            onMouseDown={handleMouseDown}
            ref={canvasRef}
    />)
})