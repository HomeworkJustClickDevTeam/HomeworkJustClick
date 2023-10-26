import React, { CanvasHTMLAttributes, MouseEventHandler, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { CommentInterface } from "../../types/CommentInterface"
import { cursors } from "../../assets/cursors"
import { CanvasActionsType } from "../../types/CanvasActionsType"
import { ca } from "date-fns/locale"
import { CommentActionsType } from "../../types/CommentActionsType"

export const CanvasLogic = ({image, commentsList, setCommentsList, chosenComment, setChosenComment, editable, windowSize}:{
  image:HTMLImageElement,
  commentsList: AdvancedEvaluationImageCommentInterface[],
  setCommentsList?: (comments:AdvancedEvaluationImageCommentInterface[]) => void,
  chosenComment?:CommentInterface|undefined,
  setChosenComment?: (comment:AdvancedEvaluationImageCommentInterface|undefined) => void,
  editable: boolean,
  windowSize: {windowHeight: number, windowWidth: number}}) => {
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const commentsListRef = useRef(commentsList)
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
          commentsListRef.current[commentIndex.current].leftTopXY[0] += moveDiffX
          commentsListRef.current[commentIndex.current].leftTopXY[1] += moveDiffY
          commentsListRef.current[commentIndex.current].width -= moveDiffX
          commentsListRef.current[commentIndex.current].height -= moveDiffY
          drawBoxes()
          break
        case "topLineHovered":
          commentsListRef.current[commentIndex.current].leftTopXY[1] += moveDiffY
          commentsListRef.current[commentIndex.current].height -= moveDiffY
          drawBoxes()
          break
        case "rightTopCornerHovered":
          commentsListRef.current[commentIndex.current].leftTopXY[1] += moveDiffY
          commentsListRef.current[commentIndex.current].width += moveDiffX
          commentsListRef.current[commentIndex.current].height -= moveDiffY
          drawBoxes()
          break
        case "rightLineHovered":
          commentsListRef.current[commentIndex.current].width += moveDiffX
          drawBoxes()
          break
        case "rightBottomCornerHovered":
          commentsListRef.current[commentIndex.current].width += moveDiffX
          commentsListRef.current[commentIndex.current].height += moveDiffY
          drawBoxes()
          break
        case "bottomLineHovered":
          commentsListRef.current[commentIndex.current].height += moveDiffY
          drawBoxes()
          break
        case "leftBottomCornerHovered":
          commentsListRef.current[commentIndex.current].leftTopXY[0] += moveDiffX
          commentsListRef.current[commentIndex.current].width -= moveDiffX
          commentsListRef.current[commentIndex.current].height += moveDiffY
          drawBoxes()
          break
        case "leftLineHovered":
          commentsListRef.current[commentIndex.current].leftTopXY[0] += moveDiffX
          commentsListRef.current[commentIndex.current].width -= moveDiffX
          drawBoxes()
          break
      }
    }
  }
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
      let newComment:AdvancedEvaluationImageCommentInterface = {...JSON.parse(JSON.stringify(chosenComment)), leftTopXY:[mouseX, mouseY], width: 1, height:1, lineWidth: 2, windowHeight:windowSize.windowHeight, windowWidth:windowSize.windowWidth}
      commentIndex.current = commentsListRef.current.push(newComment) - 1
    }
  }
  const handleCommentSizeChangeWhileItBeingCreated = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      commentsListRef.current[commentIndex.current].width += moveDiffX
      commentsListRef.current[commentIndex.current].height += moveDiffY
      drawBoxes()
    }
  }
  const handleCommentMove = (mouseX:number, mouseY:number) => {
    if(commentIndex.current!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX.current!==null && mouseStartY.current!==null){
      const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY)
      commentsListRef.current[commentIndex.current].leftTopXY[0] += moveDiffX
      commentsListRef.current[commentIndex.current].leftTopXY[1] += moveDiffY
      drawBoxes()
    }
  }
  const handleCommentDeletion = () => {
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      if(commentIndex.current !== null){
        commentsListRef.current.splice(commentIndex.current, 1)
        drawBoxes()
      }
    }
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
    }
    return draw
  }
  const normalizeCommentValues = () => {
    if (commentIndex.current !== null) {
      if (commentsListRef.current[commentIndex.current].height <= 0) {//minus height removal
        commentsListRef.current[commentIndex.current].height = Math.abs(commentsListRef.current[commentIndex.current].height)
        commentsListRef.current[commentIndex.current].leftTopXY[1] -= commentsListRef.current[commentIndex.current].height
      }
      if (commentsListRef.current[commentIndex.current].width <= 0) {//minus width removal
        commentsListRef.current[commentIndex.current].width = Math.abs(commentsListRef.current[commentIndex.current].width)
        commentsListRef.current[commentIndex.current].leftTopXY[0] -= commentsListRef.current[commentIndex.current].width
      }
    }
  }
  const setPartOfCommentHovered = (mouseX:number, mouseY:number, clickedCommentIndex?:number|null) => {
    for (let i = 0; i < commentsListRef.current.length; i++) {
      const hoveredComment = JSON.parse(JSON.stringify(commentsListRef.current[i]))
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
  const runBackCommentValues = (initialDrawValue:boolean) => {
    let draw = initialDrawValue
    if(commentIndex.current !== null && commentPreviousState.current !== null) {
      if(checkCollision(
      commentsListRef.current[commentIndex.current].leftTopXY[0],
      commentsListRef.current[commentIndex.current].leftTopXY[1],
      commentsListRef.current[commentIndex.current].width,
      commentsListRef.current[commentIndex.current].height,
      commentIndex.current))
      {
        commentsListRef.current[commentIndex.current] = JSON.parse(JSON.stringify(commentPreviousState.current))
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
    else if(event.button === 2 && commentAction.current==="centerHovered"){//check if rmb used
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
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(commentsListRef.current[commentIndex.current]))//deep copy
      }
      else if(canvasAction.current === "commentCreating"){
        handleNewCommentCreation(mouseStartX.current, mouseStartY.current)
      }
      else if(canvasAction.current === "commentResizing"){
        commentPreviousState.current = commentIndex.current === null ? null : JSON.parse(JSON.stringify(commentsListRef.current[commentIndex.current]))
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
          commentsListRef.current[commentIndex.current].leftTopXY[0],
          commentsListRef.current[commentIndex.current].leftTopXY[1],
          commentsListRef.current[commentIndex.current].width,
          commentsListRef.current[commentIndex.current].height,
          commentIndex.current))
        {//check if mouse up on existing comment cancel is so (comment that we are creating is already in the array)
          commentsListRef.current.splice(commentIndex.current, 1)
          draw = true
        }
      }
    }
    else if(canvasAction.current === "commentResizing"){
      normalizeCommentValues()
      draw = runBackCommentValues(draw)
    }
    else if(canvasAction.current === "commentDeleting"){
      handleCommentDeletion()
    }
    if (draw) {
      drawBoxes()
    }
    commentIndex.current = null
    commentPreviousState.current = null
    if(setCommentsList !== undefined)
      setCommentsList(commentsListRef.current)
    if(setChosenComment !== undefined)
      setChosenComment(undefined)
    mouseStartY.current = null
    mouseStartX.current = null
    canvasAction.current = "hovering"
    const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY)
    setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas)
    handleCursorAppearanceChange()
    console.log(commentsListRef.current, canvasAction.current)
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
    const scaleCommentsToWindowDimensions = () => {
      for(const comment of commentsListRef.current){
        const commentsProportionsWidth = windowSize.windowWidth/comment.windowWidth
        const commentsProportionsHeight = windowSize.windowHeight/comment.windowHeight
        comment.leftTopXY[0] *= commentsProportionsWidth
        comment.leftTopXY[1] *= commentsProportionsHeight
        comment.width *= commentsProportionsWidth
        comment.height *= commentsProportionsHeight
        comment.windowWidth = windowSize.windowWidth
        comment.windowHeight = windowSize.windowHeight
      }
    }
    scaleCommentsToWindowDimensions()
    drawBoxes()
  }, [windowSize])



  return (<div style={{position: "relative"}}>
    <img id={"backgroundPhotoLayer"} width={1920} height={500} style={{position:"absolute", zIndex:"0"}} src={image.src}  alt={""}/>
    <canvas onContextMenu={handleMouseDown}
            onMouseOut={handleMouseUp}
            style={{position:"absolute", zIndex:"1"}}
            id={"commentsLayer"}
            onMouseUp={handleMouseUp}
            onMouseMove={handleMouseMove}
            onMouseDown={handleMouseDown}
            ref={canvasRef}
            height={document.getElementById("backgroundPhotoLayer")?.clientHeight}
            width={document.getElementById("backgroundPhotoLayer")?.clientWidth}/>
  </div>)
}