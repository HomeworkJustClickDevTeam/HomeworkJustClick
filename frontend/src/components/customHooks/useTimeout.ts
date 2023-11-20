import { useCallback, useEffect, useRef } from "react"

export const useTimeout  = (functionToRun: ()=>void, delay:number)=> {
  const functionToRunRef = useRef(functionToRun)
  const timeoutRef = useRef<NodeJS.Timeout>()
  useEffect(() => {
    functionToRunRef.current = functionToRun
  }, [functionToRunRef])
  const set = useCallback(()=> {
    timeoutRef.current = setTimeout(() => {
      functionToRunRef.current()
    }, delay)
  }, [delay])
  const clear = useCallback(() => {
    timeoutRef.current && clearTimeout(timeoutRef.current)
  }, [])
  useEffect(() => {
    set()
    return clear
  }, [delay, set, clear])
  const reset = useCallback(()=>{
    clear()
    set()
  }, [clear,set])
  return {reset, clear}
}