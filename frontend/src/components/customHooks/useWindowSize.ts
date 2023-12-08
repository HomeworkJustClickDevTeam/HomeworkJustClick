import { useLayoutEffect, useState } from "react"

export const useWindowSize = () =>{
  const [windowDimensions, setWindowSizeDimensions] = useState<number[]>([0,0])
  useLayoutEffect(() => {
    const updateSize = () => {
      setWindowSizeDimensions([window.innerWidth, window.innerHeight])
    }
    window.addEventListener("resize", updateSize)
    updateSize()
    return () => window.removeEventListener("resize", updateSize)
  }, [])
  return {windowHeight: windowDimensions[1], windowWidth: windowDimensions[0]}
}