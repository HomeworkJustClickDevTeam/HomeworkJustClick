import { MutableRefObject, useEffect } from "react"
import { inflate } from "zlib"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const useDrawCanvas = (commentsList:AdvancedEvaluationImageCommentInterface[], image:HTMLImageElement, canvasContext:MutableRefObject<CanvasRenderingContext2D|null|undefined>) => {
  useEffect(() => {
    image.onload = () => {
      if (canvasContext.current !== null && canvasContext.current !== undefined) {
        canvasContext.current.canvas.width = image.width
        canvasContext.current.canvas.height = image.height
        canvasContext.current.drawImage(image, 0, 0, canvasContext.current.canvas.width, canvasContext.current.canvas.height)
        const windowWidth = window.innerWidth
        const windowHeight = window.innerHeight
        const canvasX = canvasContext.current.canvas.offsetLeft
        const canvasY = canvasContext.current.canvas.offsetTop
        for (const comment of commentsList) {
          const leftTopX = comment.leftTopXY[0] * windowWidth - canvasX
          const leftTopY = comment.leftTopXY[1] * windowHeight - canvasY
          const width = (comment.rightTopXY[0] - canvasX) * windowWidth - (comment.leftTopXY[0] - canvasX) * windowWidth
          const height = (comment.rightBottomXY[1] - canvasY) * windowHeight - (comment.rightTopXY[1] - canvasY) * windowHeight
          console.log(leftTopX, leftTopY, width, height, canvasX, canvasY, windowWidth, windowHeight)
          canvasContext.current.strokeStyle = comment.color
          canvasContext.current.rect(leftTopX, leftTopY, width, height)
          canvasContext.current.stroke()
        }
      }
    }
  }, [image, commentsList])
}