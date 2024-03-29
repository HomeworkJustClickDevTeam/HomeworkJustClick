import {
  changeCommentImagePostgresService,
  createCommentImageWithFilePostgresService, deleteCommentImagePostgresService
} from "../services/postgresDatabaseServices";
import {AdvancedEvaluationImageCommentModel} from "../types/AdvancedEvaluationImageComment.model";
import {cursors} from "../assets/cursors";
import React, {SetStateAction} from "react";
import {CanvasActionsType} from "../types/CanvasActionsType";
import {CommentActionsType} from "../types/CommentActionsType";
import {CommentInterface} from "../types/CommentInterface";
import {SortCommentsButtonStateType} from "../types/SortCommentsButtonStateType";
import {he} from "date-fns/locale";
import {FileInterface} from "../types/FileInterface";

export const handleCommentResizing = (mouseX:number,
                                      mouseY:number,
                                      commentIndex:number|null,
                                      canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                      mouseStartX: number|null,
                                      mouseStartY: number|null,
                                      drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                      commentAction:CommentActionsType,
                                      image:HTMLImageElement,
                                      width:number|undefined,
                                      height:number|undefined,
                                      setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                      setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                      canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                      setCommentsImage: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>) => {
  if(commentIndex !== null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null) {
    const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY, setMouseStartY, setMouseStartX, canvasContext, mouseStartX, mouseStartY, canvasRef)
    switch (commentAction){
      case "leftTopCornerHovered":
        drawnComments.current[commentIndex].leftTopX += moveDiffX
        drawnComments.current[commentIndex].leftTopY += moveDiffY
        drawnComments.current[commentIndex].width -= moveDiffX
        drawnComments.current[commentIndex].height -= moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "topLineHovered":
        drawnComments.current[commentIndex].leftTopY += moveDiffY
        drawnComments.current[commentIndex].height -= moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "rightTopCornerHovered":
        drawnComments.current[commentIndex].leftTopY += moveDiffY
        drawnComments.current[commentIndex].width += moveDiffX
        drawnComments.current[commentIndex].height -= moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "rightLineHovered":
        drawnComments.current[commentIndex].width += moveDiffX
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "rightBottomCornerHovered":
        drawnComments.current[commentIndex].width += moveDiffX
        drawnComments.current[commentIndex].height += moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "bottomLineHovered":
        drawnComments.current[commentIndex].height += moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "leftBottomCornerHovered":
        drawnComments.current[commentIndex].leftTopX += moveDiffX
        drawnComments.current[commentIndex].width -= moveDiffX
        drawnComments.current[commentIndex].height += moveDiffY
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
      case "leftLineHovered":
        drawnComments.current[commentIndex].leftTopX += moveDiffX
        drawnComments.current[commentIndex].width -= moveDiffX
        drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
        break
    }
  }
}
export const updateCommentImageInDb = async (draw:boolean,
                                             commentIndex:number|null,
                                             drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                             commentPreviousState:AdvancedEvaluationImageCommentModel|null,
                                             ) => {

  if (commentIndex !== undefined && commentIndex !== null) {
    try {
      const response = await changeCommentImagePostgresService(drawnComments.current[commentIndex])
      if(!(response?.data !== undefined && response?.data !== null && response?.status === 200)){
        drawnComments.current[commentIndex] = JSON.parse(JSON.stringify(commentPreviousState))
        return true
      }
    }
    catch (error)
    {
      drawnComments.current[commentIndex] = JSON.parse(JSON.stringify(commentPreviousState))
      return true
    }
  }
  return draw
}
export const saveCommentImageToDb = async (draw:boolean,
                                           drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                           commentIndex:number|null,
                                           setRefreshRightPanelUserComments: React.Dispatch<SetStateAction<boolean>>,
                                           setSortButtonState:React.Dispatch<SetStateAction<SortCommentsButtonStateType>>) => {
  if (commentIndex !== undefined && commentIndex !== null) {
    try {
      const response = await createCommentImageWithFilePostgresService(
        {
          leftTopY: drawnComments.current[commentIndex].leftTopY,
          leftTopX: drawnComments.current[commentIndex].leftTopX,
          width: drawnComments.current[commentIndex].width,
          height: drawnComments.current[commentIndex].height,
          imgWidth: drawnComments.current[commentIndex].imgWidth,
          imgHeight: drawnComments.current[commentIndex].imgHeight,
          color: drawnComments.current[commentIndex].color,
          commentId: drawnComments.current[commentIndex].comment.id,
          fileId: drawnComments.current[commentIndex].file.id
        })
      if (response?.data !== undefined && response?.data !== null && response?.status === 201) {
        drawnComments.current[commentIndex].id = response.data.id
        setRefreshRightPanelUserComments(prevState => !prevState)
        setSortButtonState("lastUsedDate,desc")
      }
      else{
        drawnComments.current.splice(commentIndex, 1)
        return true
      }
    }
    catch (error)
    {
      drawnComments.current.splice(commentIndex, 1)
      return true
    }
  }
  return draw
}
export const checkCollision = (topLeftX:number,
                               topLeftY:number,
                               width:number,
                               height:number,
                               movedCommentIndex:number,
                               drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>) => {
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
export const handleNewCommentCreation = (mouseX:number|null,
                                         mouseY:number|null,
                                         chosenComment: CommentInterface|undefined,
                                         canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                         setCommentIndex: React.Dispatch<SetStateAction<number|null>>,
                                         drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                         file:FileInterface) =>{
  if(chosenComment !== undefined && canvasRef.current !== undefined && canvasRef.current !== null && mouseX!==null && mouseY!==null){

    let newComment:AdvancedEvaluationImageCommentModel = {
      id:-1, color: chosenComment.color, file: file, comment:chosenComment, leftTopX: mouseX, leftTopY:mouseY, width: 1, height:1, imgHeight:canvasRef.current.height, imgWidth:canvasRef.current.width}
    setCommentIndex(drawnComments.current.push(newComment) - 1)
  }
}
export const handleCommentSizeChangeWhileItBeingCreated = (mouseX:number,
                                                           mouseY:number,
                                                           commentIndex:number|null,
                                                           canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                                           drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                                           mouseStartX: number|null,
                                                           mouseStartY: number|null,
                                                           width:number|undefined,
                                                           height:number|undefined,
                                                           setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                                           setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                                           canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                                           image:HTMLImageElement,
                                                           setCommentsImage: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>) => {
  if(commentIndex!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
    const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY, setMouseStartY, setMouseStartX, canvasContext, mouseStartX, mouseStartY, canvasRef)
    drawnComments.current[commentIndex].width += moveDiffX
    drawnComments.current[commentIndex].height += moveDiffY
    drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
  }
}
export const handleCommentMove = (mouseX:number,
                                  mouseY:number,
                                  commentIndex:number|null,
                                  canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                  mouseStartX: number|null,
                                  mouseStartY: number|null,
                                  drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                  setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                  setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                  canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                  width:number|undefined,
                                  height:number|undefined,
                                  image:HTMLImageElement,
                                  setCommentsImage: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>) => {
  if(commentIndex!==null && canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
    const {moveDiffX, moveDiffY} = updateMouseCoords(mouseX, mouseY, setMouseStartY, setMouseStartX, canvasContext, mouseStartX, mouseStartY, canvasRef)
    drawnComments.current[commentIndex].leftTopX += moveDiffX
    drawnComments.current[commentIndex].leftTopY += moveDiffY
    drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
  }
}
export const handleDrawnCommentDeletion = async (draw:boolean,
                                                 canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                                 drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                                 commentIndex:number|null,) => {
  if (canvasContext.current !== null && canvasContext.current !== undefined) {
    if(commentIndex !== null){
      const response = await deleteCommentImagePostgresService(drawnComments.current[commentIndex].id.toString())
      if(response.data !== undefined && response.data !== null && response.status === 200) {
        drawnComments.current.splice(commentIndex, 1)
        return true
      }
    }
  }
  return draw
}
export const handleCursorAppearanceChange = (canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                             commentAction:CommentActionsType) => {
  if (canvasRef.current !== null) {
    switch (commentAction) {
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
export const calculateCoordsOnCanvas = (x:number,
                                        y:number,
                                        canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                        canvasRef: React.MutableRefObject<HTMLCanvasElement | null>) => {
  if(canvasContext.current !== null && canvasContext.current !== undefined && canvasRef.current !== null) {
    const canvasOffsets = canvasRef.current.getBoundingClientRect()
    const mouseXOnCanvas = x - canvasOffsets.x
    const mouseYOnCanvas = y - canvasOffsets.y
    return {mouseXOnCanvas, mouseYOnCanvas}
  }
  return {mouseXOnCanvas:0, mouseYOnCanvas:0}
}
export const updateMouseCoords = (x:number,
                                  y:number,
                                  setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                  setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                  canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                  mouseStartX: number|null,
                                  mouseStartY: number|null,
                                  canvasRef: React.MutableRefObject<HTMLCanvasElement | null>) => {
  if(canvasContext.current !== null && canvasContext.current !== undefined && mouseStartX!==null && mouseStartY!==null){
    const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(x, y, canvasContext, canvasRef)
    const moveDiffX = mouseXOnCanvas - mouseStartX
    const moveDiffY = mouseYOnCanvas - mouseStartY
    setMouseStartX(mouseXOnCanvas)
    setMouseStartY(mouseYOnCanvas)
    return {moveDiffX, moveDiffY}
  }
  return {moveDiffX: 0, moveDiffY: 0}
}
export const checkIfOutOfCanvas = (initialDrawValue:boolean,
                                   commentIndex:number|null,
                                   drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                   commentPreviousState:AdvancedEvaluationImageCommentModel|null,
                                   canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,) =>{
  let draw = initialDrawValue
  if(commentIndex !== null && commentPreviousState !== null && canvasRef.current!==null && canvasRef.current!==undefined) {
    if (drawnComments.current[commentIndex].leftTopX < 0) { //check if out of canvas
      drawnComments.current[commentIndex].leftTopX = commentPreviousState.leftTopX
      drawnComments.current[commentIndex].leftTopY = commentPreviousState.leftTopY
      draw = true
    }
    if (drawnComments.current[commentIndex].leftTopY < 0) {//check if out of canvas
      drawnComments.current[commentIndex].leftTopX = commentPreviousState.leftTopX
      drawnComments.current[commentIndex].leftTopY = commentPreviousState.leftTopY
      draw = true
    }
    if (drawnComments.current[commentIndex].leftTopX + drawnComments.current[commentIndex].width > canvasRef.current.width) {//check if out of canvas
      drawnComments.current[commentIndex].leftTopX = commentPreviousState.leftTopX
      drawnComments.current[commentIndex].leftTopY = commentPreviousState.leftTopY
      draw = true
    }
    if (drawnComments.current[commentIndex].leftTopY + drawnComments.current[commentIndex].height > canvasRef.current.height) {//check if out of canvas
      drawnComments.current[commentIndex].leftTopX = commentPreviousState.leftTopX
      drawnComments.current[commentIndex].leftTopY = commentPreviousState.leftTopY
      draw = true
    }
  }
  return draw
}
export const normalizeCommentValues = (drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                       commentIndex:number|null,) => {
  if (commentIndex !== null) {
    if (drawnComments.current[commentIndex].height <= 0) {//minus height removal
      drawnComments.current[commentIndex].height = Math.abs(drawnComments.current[commentIndex].height)
      drawnComments.current[commentIndex].leftTopY -= drawnComments.current[commentIndex].height
    }
    if (drawnComments.current[commentIndex].width <= 0) {//minus width removal
      drawnComments.current[commentIndex].width = Math.abs(drawnComments.current[commentIndex].width)
      drawnComments.current[commentIndex].leftTopX -= drawnComments.current[commentIndex].width
    }
  }
}
export const calculateImageSizeWithAspectRatio = (image:HTMLImageElement,
                                                  targetWidth?:number,
                                                  targetHeight?:number) => {
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
export const setPartOfCommentHovered = (mouseX:number,
                                        mouseY:number,
                                        drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                        setCommentAction:React.Dispatch<SetStateAction<CommentActionsType>>,
                                        clickedCommentIndex?:number|null) => {
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
    if ((hoveredCommentLeftTopXY[0] - 2 <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0]  + 2) &&
      (hoveredCommentLeftTopXY[1]  - 2 <= mouseY) && (mouseY <= hoveredCommentLeftTopXY[1]  + 2)) {//left top corner hovered
      setCommentAction("leftTopCornerHovered")
      return i
    } else if ((hoveredCommentRightTopXY[0] - 2 <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] + 2) &&
      (hoveredCommentRightTopXY[1] - 2 <= mouseY) && (mouseY <= hoveredCommentRightTopXY[1] + 2)) {
      setCommentAction("rightTopCornerHovered")
      return i
    } else if ((hoveredCommentLeftBottomXY[0] - 2 <= mouseX) && (mouseX <= hoveredCommentLeftBottomXY[0] + 2) &&
      (hoveredCommentLeftBottomXY[1] - 2 <= mouseY) && (mouseY <= hoveredCommentLeftBottomXY[1] + 2)) {
      setCommentAction("leftBottomCornerHovered")
      return i
    } else if ((hoveredCommentRightBottomXY[0] - 2 <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] + 2) &&
      (hoveredCommentRightBottomXY[1] - 2 <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] + 2)) {
      setCommentAction("rightBottomCornerHovered")
      return i
    } else if ((hoveredCommentLeftTopXY[0]  + 2 <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] - 2) &&
      (hoveredCommentRightTopXY[1] - 2 <= mouseY) && (mouseY <= hoveredCommentRightTopXY[1] + 2)) {
      setCommentAction("topLineHovered")
      return i
    } else if ((hoveredCommentLeftBottomXY[0] + 2 <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - 2) &&
      (hoveredCommentRightBottomXY[1] - 2 <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] + 2)) {
      setCommentAction("bottomLineHovered")
      return i
    } else if ((hoveredCommentLeftTopXY[0]  - 2 <= mouseX) && (mouseX <= hoveredCommentLeftTopXY[0]  + 2) &&
      (hoveredCommentLeftTopXY[1]  + 2 <= mouseY) && (mouseY <= hoveredCommentLeftBottomXY[1] - 2)) {
      setCommentAction("leftLineHovered")
      return i
    } else if ((hoveredCommentRightTopXY[0] - 2 <= mouseX) && (mouseX <= hoveredCommentRightTopXY[0] + 2) &&
      (hoveredCommentRightTopXY[1] + 2 <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - 2)) {
      setCommentAction("rightLineHovered")
      return i
    } else if ((hoveredCommentLeftTopXY[0]  + 2 <= mouseX) && (mouseX <= hoveredCommentRightBottomXY[0] - 2) &&
      (hoveredCommentLeftTopXY[1]  + 2 <= mouseY) && (mouseY <= hoveredCommentRightBottomXY[1] - 2)) {
      setCommentAction("centerHovered")
      return i
    }
  }
  setCommentAction("noHover")
  return null
}
export const scaleCommentsToImageDimensions = (canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                               drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>) => {
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
export const calculateCanvasSize = (width:number|undefined,
                                    height:number|undefined,
                                    image:HTMLImageElement) => {
  if(height !== undefined && width !== undefined){
    if(image.height <= height && image.width <= width){
      return {calculatedHeight: image.height, calculatedWidth: image.width}
    }
    else if(image.height > height && image.width <= width){
      return {calculatedWidth: calculateImageSizeWithAspectRatio(image, undefined, height), calculatedHeight: height}
    }
    else if(image.height <= height && image.width > width){
      return {calculatedWidth: width, calculatedHeight: calculateImageSizeWithAspectRatio(image, width, undefined)}
    }
    else{
      return {calculatedWidth: width, calculatedHeight: calculateImageSizeWithAspectRatio(image, width, undefined)}
    }
  }
  else{
    return {calculatedHeight: undefined, calculatedWidth: undefined}
  }
}
export const runBackCommentValues = (initialDrawValue:boolean,
                                     commentIndex:number|null,
                                     commentPreviousState:AdvancedEvaluationImageCommentModel|null,
                                     drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,) => {
  let draw = initialDrawValue
  if(commentIndex !== null && commentPreviousState !== null) {
    if(checkCollision(
      drawnComments.current[commentIndex].leftTopX,
      drawnComments.current[commentIndex].leftTopY,
      drawnComments.current[commentIndex].width,
      drawnComments.current[commentIndex].height,
      commentIndex,
      drawnComments))
    {
      drawnComments.current[commentIndex] = JSON.parse(JSON.stringify(commentPreviousState))
      draw = true
    }
  }
  return draw
}
export const handleMouseMove = (event:React.MouseEvent,
                                canvasAction:CanvasActionsType,
                                setCommentIndex:React.Dispatch<SetStateAction<number|null>>,
                                canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                setCommentAction:React.Dispatch<SetStateAction<CommentActionsType>>,
                                commentAction: CommentActionsType,
                                commentIndex:number|null,
                                mouseStartX: number|null,
                                width:number|undefined,
                                height:number|undefined,
                                mouseStartY: number|null,
                                setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                image:HTMLImageElement,
                                setCommentsImage?: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>) => {
  event.preventDefault()
  event.stopPropagation()

  if(setCommentsImage !== undefined && canvasAction === "commentDeleting") {
    const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
    setCommentIndex(setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas, drawnComments, setCommentAction))
    handleCursorAppearanceChange(canvasRef, commentAction)
  }
  else if(setCommentsImage !== undefined && canvasAction === "commentHolding"){
    handleCommentMove(event.clientX, event.clientY, commentIndex, canvasContext, mouseStartX, mouseStartY, drawnComments, setMouseStartY, setMouseStartX, canvasRef,width, height, image, setCommentsImage)
  }
  else if(setCommentsImage !== undefined && canvasAction === "commentCreating") {
    handleCommentSizeChangeWhileItBeingCreated(event.clientX, event.clientY, commentIndex, canvasContext, drawnComments, mouseStartX, mouseStartY,width, height, setMouseStartY, setMouseStartX, canvasRef, image, setCommentsImage)
  }
  else if(setCommentsImage !== undefined && canvasAction === "commentResizing"){
    handleCommentResizing(event.clientX, event.clientY, commentIndex, canvasContext, mouseStartX, mouseStartY, drawnComments, commentAction, image,width, height, setMouseStartY, setMouseStartX, canvasRef, setCommentsImage)
  }
  else if(canvasAction === "hovering"){
    const {mouseXOnCanvas, mouseYOnCanvas} = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
    setCommentIndex(setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas, drawnComments, setCommentAction))
    setCommentsImage !== undefined && handleCursorAppearanceChange(canvasRef, commentAction)
  }
}
export const handleMouseDown = (event:React.MouseEvent,
                                setCanvasAction:React.Dispatch<SetStateAction<CanvasActionsType>>,
                                setCommentIndex:React.Dispatch<SetStateAction<number|null>>,
                                commentAction:CommentActionsType,
                                canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                setCommentPreviousState:React.Dispatch<SetStateAction<AdvancedEvaluationImageCommentModel|null>>,
                                drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                commentIndex:number|null,
                                setMouseDownTimestamp:React.Dispatch<SetStateAction<number|null>>,
                                canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                                file?: FileInterface,
                                chosenComment?: CommentInterface|undefined) => {
  event.preventDefault()
  event.stopPropagation()
  setMouseDownTimestamp(event.timeStamp)
  if(event.button === 2 && file !== undefined){//check if rmb used
    setCanvasAction("commentDeleting")
  }
  else if(commentAction==="centerHovered"){
    setCanvasAction("commentHolding")
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const {
        mouseXOnCanvas,
        mouseYOnCanvas
      } = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
      setMouseStartX(mouseXOnCanvas)
      setMouseStartY(mouseYOnCanvas)
      setCommentPreviousState(commentIndex === null ? null : JSON.parse(JSON.stringify(drawnComments.current[commentIndex])))//deep copy
    }
  }
  else if((commentAction==="leftTopCornerHovered" ||
    commentAction==="topLineHovered" ||
    commentAction==="rightTopCornerHovered" ||
    commentAction==="rightLineHovered" ||
    commentAction==="rightBottomCornerHovered" ||
    commentAction==="bottomLineHovered" ||
    commentAction==="leftBottomCornerHovered" ||
    commentAction==="leftLineHovered") && file !== undefined){
    setCanvasAction("commentResizing")
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
      setMouseStartX(mouseXOnCanvas)
      setMouseStartY(mouseYOnCanvas)
      setCommentPreviousState(commentIndex === null ? null : JSON.parse(JSON.stringify(drawnComments.current[commentIndex])))
    }
  }
  else if (chosenComment !== undefined && file !== undefined) {
    setCanvasAction("commentCreating")
    if (canvasContext.current !== null && canvasContext.current !== undefined) {
      const { mouseXOnCanvas, mouseYOnCanvas } = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
      setMouseStartX(mouseXOnCanvas)
      setMouseStartY(mouseYOnCanvas)

      handleNewCommentCreation(mouseXOnCanvas, mouseYOnCanvas, chosenComment, canvasRef, setCommentIndex, drawnComments, file)
    }
  }
}
export const handleMouseUp = async (event: React.MouseEvent,
                                    canvasAction:CanvasActionsType,
                                    setCanvasAction:React.Dispatch<SetStateAction<CanvasActionsType>>,
                                    setCommentIndex:React.Dispatch<SetStateAction<number|null>>,
                                    setMouseStartY:React.Dispatch<SetStateAction<number|null>>,
                                    setMouseStartX:React.Dispatch<SetStateAction<number|null>>,
                                    setMouseDownTimestamp:React.Dispatch<SetStateAction<number|null>>,
                                    setCommentPreviousState:React.Dispatch<SetStateAction<AdvancedEvaluationImageCommentModel|null>>,
                                    commentIndex:number|null,
                                    mouseDownTimestamp:number|null,
                                    handleCommentHighlighting: (commentIdToHighlight: number) => void,
                                    drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                                    commentPreviousState: AdvancedEvaluationImageCommentModel|null,
                                    image:HTMLImageElement,
                                    canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                                    canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,

                                    setCommentAction:React.Dispatch<SetStateAction<CommentActionsType>>,
                                    commentAction:CommentActionsType,
                                    width:number|undefined,
                                    height:number|undefined,
                                    setSortButtonState?:React.Dispatch<SetStateAction<SortCommentsButtonStateType>>,
                                    setCommentsImage?: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>,
                                    setRefreshRightPanelUserComments?: React.Dispatch<SetStateAction<boolean>>,
                                    ) => {
  event.preventDefault()
  event.stopPropagation()

  let draw = false
  if (canvasAction === "commentHolding" && commentIndex !== null) {
    if(mouseDownTimestamp !== null && (event.timeStamp - mouseDownTimestamp) < 150){
      handleCommentHighlighting(drawnComments.current[commentIndex].comment.id)
      drawnComments.current[commentIndex] = JSON.parse(JSON.stringify(commentPreviousState))
      draw = true
    }
    else if (commentPreviousState !== null && setRefreshRightPanelUserComments !== undefined) {
      draw = checkIfOutOfCanvas(draw, commentIndex, drawnComments, commentPreviousState, canvasRef)
      draw = runBackCommentValues(draw, commentIndex, commentPreviousState, drawnComments)
      if(!draw)
        draw = await updateCommentImageInDb(draw, commentIndex, drawnComments, commentPreviousState)
    }
  } else if (canvasAction === "commentCreating" && setRefreshRightPanelUserComments !== undefined) {//mouse up while creating new comment
    if (commentIndex !== null) {
      normalizeCommentValues(drawnComments, commentIndex)
      if (checkCollision(
        drawnComments.current[commentIndex].leftTopX,
        drawnComments.current[commentIndex].leftTopY,
        drawnComments.current[commentIndex].width,
        drawnComments.current[commentIndex].height,
        commentIndex,
        drawnComments)) {//check if mouse up on existing comment cancel if so (comment that we are creating is already in the array)
        drawnComments.current.splice(commentIndex, 1)
        draw = true
      } else if(setRefreshRightPanelUserComments !== undefined && setSortButtonState !== undefined) {
        draw = await saveCommentImageToDb(draw, drawnComments, commentIndex, setRefreshRightPanelUserComments, setSortButtonState)
      }
    }
  } else if (canvasAction === "commentResizing" && setRefreshRightPanelUserComments !== undefined) {
    normalizeCommentValues(drawnComments, commentIndex)
    draw = runBackCommentValues(draw, commentIndex, commentPreviousState, drawnComments)
    if(!draw)
      draw = await updateCommentImageInDb(draw, commentIndex, drawnComments, commentPreviousState)
  } else if (canvasAction === "commentDeleting" && event.button === 2 && setRefreshRightPanelUserComments !== undefined) {
    draw = await handleDrawnCommentDeletion(draw, canvasContext, drawnComments, commentIndex)
  }
  if (draw) {
    drawOnCanvas(canvasContext, image, drawnComments, width, height, canvasRef, setCommentsImage)
  }
  setCommentIndex(null)
  setCommentPreviousState(null)
  setMouseStartY(null)
  setMouseStartX(null)
  setMouseDownTimestamp(null)
  setCanvasAction("hovering")
  const { mouseXOnCanvas, mouseYOnCanvas } = calculateCoordsOnCanvas(event.clientX, event.clientY, canvasContext, canvasRef)
  setPartOfCommentHovered(mouseXOnCanvas, mouseYOnCanvas, drawnComments, setCommentAction)
  setSortButtonState !== undefined && handleCursorAppearanceChange(canvasRef, commentAction)
}
export const drawOnCanvas = (canvasContext:React.MutableRefObject<CanvasRenderingContext2D|undefined|null>,
                             image:HTMLImageElement,
                             drawnComments:React.MutableRefObject<AdvancedEvaluationImageCommentModel[]>,
                             width:number|undefined,
                             height:number|undefined,
                             canvasRef: React.MutableRefObject<HTMLCanvasElement | null>,
                             setCommentsImage?: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>) => {
  if(canvasContext.current!==null && canvasContext.current!==undefined){
    const {calculatedHeight, calculatedWidth} = calculateCanvasSize(width, height, image)
    if(calculatedHeight === undefined || calculatedWidth === undefined) return

    canvasContext.current.canvas.height = calculatedHeight
    canvasContext.current.canvas.width = calculatedWidth
    canvasContext.current.globalAlpha = 1
    canvasContext.current.clearRect(0,0, calculatedWidth, calculatedHeight)
    if(image.complete){
      canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
      canvasContext.current.strokeStyle = 'black'
      canvasContext.current.lineWidth = 3
      canvasContext.current.strokeRect(0,0,calculatedWidth,calculatedHeight)

    }
    else
      image.onload = () =>{
        if(canvasContext.current!==null && canvasContext.current!==undefined){
          canvasContext.current.drawImage(image,0,0,  canvasContext.current.canvas.width, canvasContext.current.canvas.height)
          canvasContext.current.strokeStyle = 'black'
          canvasContext.current.lineWidth = 3
          canvasContext.current.strokeRect(0,0,calculatedWidth,calculatedHeight)

        }
      }
    scaleCommentsToImageDimensions(canvasRef, drawnComments)
    for (const comment of drawnComments.current) {
      canvasContext.current.fillStyle = comment.color
      canvasContext.current.globalAlpha = 0.2
      canvasContext.current.fillRect(comment.leftTopX, comment.leftTopY, comment.width, comment.height)
    }
    if(setCommentsImage!==undefined)
      setCommentsImage(drawnComments.current)
  }
}