import { CanvasLogic } from "./CanvasLogic"

export const AdvancedEvaluationImageArea = (props:{image: HTMLImageElement}) =>{
  return <CanvasLogic image={props.image} commentsList={[]}></CanvasLogic>
}