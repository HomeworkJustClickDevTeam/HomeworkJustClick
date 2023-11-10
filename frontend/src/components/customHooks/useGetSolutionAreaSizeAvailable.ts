import { useEffect, useState } from "react"
import { useWindowSize } from "./useWindowSize"

export const useGetSolutionAreaSizeAvailable = () => {
  const {windowHeight, windowWidth} =  useWindowSize()
  const [availableWidth, setAvailableWidth] = useState<number|undefined>(undefined)
  const [availableHeight, setAvailableHeight] = useState<number|undefined>(undefined)

  useEffect(() => {
    const commentPanelWidth = document.getElementById("commentPanelDiv")?.clientWidth
    const backButtonHeight = document.getElementById("backButtonDiv")?.clientHeight
    const headerHeight = document.getElementById("headerLoggedInSection")?.clientHeight
    commentPanelWidth !== undefined && setAvailableWidth(windowWidth-commentPanelWidth)
    headerHeight !== undefined && backButtonHeight !== undefined && setAvailableHeight(windowHeight-headerHeight-backButtonHeight)
  }, [windowWidth, document.getElementById("backButtonDiv")?.clientHeight, windowHeight, document.getElementById("commentPanel")?.clientWidth, document.getElementById("headerLoggedIn")?.clientHeight])

  return {availableHeight: availableHeight, availableWidth:availableWidth}
}