import { CanvasHTMLAttributes, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const CanvasLogic = ({image, commentsList}:{image:HTMLImageElement, commentsList: AdvancedEvaluationImageCommentInterface[]}) => {
  const canvasRef = useRef<HTMLCanvasElement>(null)

  useEffect(() => {
    const windowWidth = window.innerWidth
    const windowHeight = window.innerHeight
    const canvas = canvasRef.current
    image.onload = () =>{
      if(canvas !== null && canvas !== undefined){
        const canvasContext = canvas.getContext("2d")
        if(canvasContext !== null && canvasContext !== undefined){
          const imageWidth = image.width
          const imageHeight = image.height

          const canvasPosition = canvas.getBoundingClientRect()
          const canvasX = canvasPosition.left
          const canvasY = canvasPosition.top
          canvas.width = imageWidth
          canvas.height = imageHeight
          canvasContext.drawImage(image, 0, 0, imageWidth, imageHeight)
          for(const comment of commentsList) {
            const leftTopX = comment.leftTopXY[0] * windowWidth - canvasX
            const leftTopY = comment.leftTopXY[1] * windowHeight - canvasY

            const width = (comment.rightTopXY[0]- canvasX) * windowWidth - (comment.leftTopXY[0]- canvasX) * windowWidth
            const height = (comment.rightBottomXY[1]- canvasY) * windowHeight - (comment.rightTopXY[1]- canvasY) * windowHeight
            canvasContext.strokeStyle = comment.color
            canvasContext.rect(leftTopX, leftTopY, width, height)
            canvasContext.stroke()
          }
        }
      }
    }
  }, [image])


  return <canvas ref={canvasRef} height={image.height} width={image.width}/>
}