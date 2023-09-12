import { useState } from "react"

export const AdvancedEvaluationTextFileArea = ({ text, color }:{text:string, color:string|undefined}) => {
  const [highlightStart, setHighlightStart] = useState<number|null>(null);
  const [highlightEnd, setHighlightEnd] = useState<number|null>(null);

  const handleMouseUp = (index:number) => {
    setHighlightEnd(index); //because end word is the last one before cursor ends
  }
  const handleMouseDown = (index:number) => {
    setHighlightStart(null)
    setHighlightEnd(null)
    setHighlightStart(index); //because start word is the one after cursor starts
  };

  return <>{text.split('').map((letter:string, index)=>{
    return <div style={{backgroundColor: (highlightEnd && highlightStart&& color && (index - 1 < Math.max(highlightStart, highlightEnd)) && (index + 1 > Math.min(highlightStart, highlightEnd)) ? color : 'white'), display:"inline", userSelect:'none'}} onMouseDown={(e) => handleMouseDown(index)} onMouseUp={(e) => handleMouseUp(index)} key={index}>{letter}</div>
  })}</>
}