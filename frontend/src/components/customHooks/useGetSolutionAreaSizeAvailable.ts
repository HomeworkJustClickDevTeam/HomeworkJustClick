import {useCallback, useEffect, useState} from "react"
import { useWindowSize } from "./useWindowSize"

export const useGetSolutionAreaSizeAvailable = () => {
  const {windowHeight, windowWidth} =  useWindowSize()
  const [availableWidth, setAvailableWidth] = useState<number|undefined>(undefined)
  const [availableHeight, setAvailableHeight] = useState<number|undefined>(undefined)
  const [commentPanelWidth, setCommentPanelWidth] = useState<number|undefined>(undefined)
  const [backButtonHeight, setBackButtonHeight] = useState<number|undefined>(undefined)


  const onCommentPanelListRefChange = useCallback((node:HTMLDivElement)=>{
    if(node !== null)
      setCommentPanelWidth(node.offsetWidth)
  },[])

  const onBackButtonRefChange = useCallback((node:HTMLDivElement)=>{
    if(node !== null)
      setBackButtonHeight(node.offsetHeight)
  },[])

  useEffect(() => {
    const headerLoggedInSection = document.getElementById("headerLoggedInSection")
    if(commentPanelWidth=== undefined || backButtonHeight=== undefined || headerLoggedInSection === null) return
    const resizeObserver = new ResizeObserver(()=> {
      setAvailableWidth(windowWidth-commentPanelWidth-30)
      headerLoggedInSection !== null && backButtonHeight !== null && setAvailableHeight(windowHeight-headerLoggedInSection.offsetHeight-backButtonHeight)
    })
    resizeObserver.observe(headerLoggedInSection!)

    return () => resizeObserver.disconnect()


  }, [windowWidth, windowHeight, commentPanelWidth, backButtonHeight])

  return {availableHeight, availableWidth, onCommentPanelListRefChange, onBackButtonRefChange}
}