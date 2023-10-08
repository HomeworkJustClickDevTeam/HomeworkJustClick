interface AdvancedEvaluationTextFileAreaInterface{
  text: string
  letterColor: (undefined|string)[]
}
export const AdvancedEvaluationTextFileArea = ({letterColor, text }:AdvancedEvaluationTextFileAreaInterface) => {
  return <>{text.split('').map((letter:string, index)=>{
    return <span id={index.toString()} style={{backgroundColor: letterColor[index] ? letterColor[index] : 'white'}} key={index}>{letter}</span>
  })}</>
}