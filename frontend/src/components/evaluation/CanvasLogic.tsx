import { CanvasHTMLAttributes, useEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useDrawCanvas } from "../customHooks/useDrawCanvas"

export const CanvasLogic = ({image, commentsList}:{image:HTMLImageElement, commentsList: AdvancedEvaluationImageCommentInterface[]}) => {
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))

  useEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
  }, [])

  useDrawCanvas(commentsList, image, canvasContext)




  return <canvas ref={canvasRef} height={image.height} width={image.width}/>
}