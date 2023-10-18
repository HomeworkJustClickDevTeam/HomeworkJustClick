import { CanvasHTMLAttributes, useEffect, useRef } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"

export const CanvasLogic = (props:{image:HTMLImageElement, commentsList: AdvancedEvaluationImageCommentInterface[]}) => {
  const canvasRef = useRef<HTMLCanvasElement>(null)

  useEffect(() => {
    props.image.onload = () =>{
      let canvasContext = null
      if(canvasRef.current !== null){
        canvasContext = canvasRef.current.getContext("2d")
        if(canvasContext !== null){
          const imageWidth = props.image.width
          const imageHeight = props.image.height
          canvasRef.current.width = imageWidth
          canvasRef.current.height = imageHeight
          canvasContext.drawImage(props.image, 0, 0, imageWidth, imageHeight)
        }
      }
    }
  }, [props.image])


  return <canvas ref={canvasRef} height={props.image.height} width={props.image.width}/>
}