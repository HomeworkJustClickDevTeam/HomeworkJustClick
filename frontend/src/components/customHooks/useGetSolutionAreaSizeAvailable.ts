import { useEffect, useState } from "react"
import { useWindowSize } from "./useWindowSize"

export const useGetSolutionAreaSizeAvailable = () => {
  const {windowHeight, windowWidth} =  useWindowSize()
  const [availableWidth, setAvailableWidth] = useState<number|undefined>(undefined)
  const [availableHeight, setAvailableHeight] = useState<number|undefined>(undefined)

  useEffect(() => {
    const commentPanelWidth = document.getElementById("commentPanel")?.clientWidth
    const headerHeight = document.getElementById("headerLoggedIn")?.clientHeight
    commentPanelWidth !== undefined && setAvailableWidth(windowWidth-commentPanelWidth)
    headerHeight !== undefined && setAvailableHeight(windowHeight-headerHeight)
  }, [windowWidth, windowHeight, document.getElementById("commentPanel")?.clientWidth, document.getElementById("headerLoggedIn")?.clientHeight])

  return {availableHeight: availableHeight, availableWidth:availableWidth}
}