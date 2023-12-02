import { CommentInterface } from "../../types/CommentInterface"
import React, { MutableRefObject, useEffect, useImperativeHandle, useLayoutEffect, useRef, useState } from "react"
import { AdvancedEvaluationImageCommentInterface } from "../../types/AdvancedEvaluationImageCommentInterface"
import { useWindowSize } from "../customHooks/useWindowSize"
import { CanvasActionsType } from "../../types/CanvasActionsType"
import { CommentActionsType } from "../../types/CommentActionsType"
import { cursors } from "../../assets/cursors"
import { he } from "date-fns/locale"
import {
  changeCommentImagePostgresService,
  createCommentImageWithFilePostgresService, deleteCommentImageByCommentFilePostgresService,
  deleteCommentImagePostgresService
} from "../../services/postgresDatabaseServices"
import { SortButtonStateType } from "../../types/SortButtonStateType"
import {useUpdateEffect} from "usehooks-ts";
import {
  drawOnCanvas,
  handleMouseDown,
  handleMouseMove,
  handleMouseUp
} from "../../utils/AdvancedEvaluationImageCanvasLogic";

interface AdvancedEvaluationImageAreaInterface{
  width:number|undefined,
  height:number|undefined,
  deletedCommentId: number|undefined,
  setDeletedCommentId:React.Dispatch<React.SetStateAction<number|undefined>>,
  updatedComment:CommentInterface|undefined,
  setUpdatedComment:React.Dispatch<React.SetStateAction<CommentInterface|undefined>>,
  setSortButtonState:React.Dispatch<React.SetStateAction<SortButtonStateType>>,
  image: HTMLImageElement,
  chosenComment?: CommentInterface|undefined,
  fileId:number
  commentsImage: AdvancedEvaluationImageCommentInterface[]
  setCommentsImage: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentInterface[]>>
  handleCommentHighlighting:(commentId: number) => void
  setRefreshRightPanelUserComments: React.Dispatch<React.SetStateAction<boolean>>
}
export const AdvancedEvaluationImageArea = ({setSortButtonState,
                                              commentsImage,
                                              setCommentsImage,
                                              setUpdatedComment,
                                              setDeletedCommentId,
                                              deletedCommentId,
                                              updatedComment,
                                              setRefreshRightPanelUserComments,
                                              handleCommentHighlighting,
                                              fileId,
                                              image,
                                              chosenComment,
                                              width,
                                              height}: AdvancedEvaluationImageAreaInterface) =>{
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const [mouseStartX, setMouseStartX] = useState<null|number>(null)
  const [mouseStartY, setMouseStartY] = useState<null|number>(null)
  const [commentIndex, setCommentIndex] = useState<null|number>(null)
  const [commentPreviousState, setCommentPreviousState] = useState<null|AdvancedEvaluationImageCommentInterface>(null)
  const [canvasAction, setCanvasAction] = useState<CanvasActionsType>("hovering")
  const [commentAction, setCommentAction] = useState<CommentActionsType>("noHover")
  const [mouseDownTimestamp, setMouseDownTimestamp] = useState<number|null>(null)
  const drawnComments = useRef<AdvancedEvaluationImageCommentInterface[]>(commentsImage)


  useLayoutEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef)
  }, [])
  useEffect(() => {
    drawnComments.current = commentsImage
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef)
  }, [width, height, commentsImage])
  useUpdateEffect(()=> {
    const updateImageList = async (updatedComment:CommentInterface) => {
      if (drawnComments?.current !== undefined && drawnComments?.current !== null) {
        const drawnCommentsTemp = await Promise.all(drawnComments.current.map(async (commentImage) => {
          if(commentImage.commentId === updatedComment.id){
            try {
              const updatedCommentExtended = { ...commentImage, color: updatedComment.color } as AdvancedEvaluationImageCommentInterface
              const response = await changeCommentImagePostgresService(updatedCommentExtended)
              if (response !== undefined && response !== null && response.data !== undefined && response.status === 200) {
                return updatedCommentExtended
              }
              else {
                return commentImage
              }
            }catch (error){
              console.log(error)
              return commentImage
            }
          }
          return commentImage
        }))
        drawnComments.current = drawnCommentsTemp
        drawOnCanvas(canvasContext,
          image,
          drawnComments,
          width,
          height,
          canvasRef)
      }
    }
    if(updatedComment!== undefined)
      updateImageList(updatedComment)
        .then(()=> setUpdatedComment(undefined))
  }, [updatedComment])
  useUpdateEffect(()=> {
    const handleCommentImageDeletion = async (deletedCommentId:number) => {
      if(drawnComments?.current !== undefined && drawnComments?.current !== null) {
        try {
          const response = await deleteCommentImageByCommentFilePostgresService(deletedCommentId.toString(), fileId.toString())
          if(response?.status === 200){
            const drawnCommentsTemp = drawnComments.current.filter( commentImage => !(commentImage.commentId === deletedCommentId))
            drawnComments.current = drawnCommentsTemp
            drawOnCanvas(canvasContext,
              image,
              drawnComments,
              width,
              height,
              canvasRef)
          }
        }catch (error){
          console.log(error)
        }
      }
    }

    if(deletedCommentId!== undefined)
      handleCommentImageDeletion(deletedCommentId)
        .then(()=> setDeletedCommentId(undefined))
  }, [deletedCommentId])
  useUpdateEffect(()=>{
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef)
  }, [mouseStartY, mouseStartX])
  return (
    <canvas onContextMenu={(event) => {
              event.preventDefault();
              event.stopPropagation()
              setCanvasAction("commentDeleting")}}
            onMouseOut={(event)=>handleMouseUp(event, canvasAction, setCanvasAction, setCommentIndex, setMouseStartY, setMouseStartX, setMouseDownTimestamp, setCommentPreviousState, commentIndex, mouseDownTimestamp, handleCommentHighlighting, drawnComments, commentPreviousState, image, canvasContext, canvasRef, setRefreshRightPanelUserComments, setSortButtonState, setCommentAction, commentAction, width, height)}
            id={"commentsLayer"}
            onMouseUp={(event)=>handleMouseUp(event, canvasAction, setCanvasAction, setCommentIndex, setMouseStartY, setMouseStartX, setMouseDownTimestamp, setCommentPreviousState, commentIndex, mouseDownTimestamp, handleCommentHighlighting, drawnComments, commentPreviousState, image, canvasContext, canvasRef, setRefreshRightPanelUserComments, setSortButtonState, setCommentAction, commentAction, width, height)}
            onMouseMove={(event)=>handleMouseMove(event, canvasAction, setCommentIndex, canvasContext, canvasRef, drawnComments, setCommentAction, commentAction, commentIndex, mouseStartX, width, height, mouseStartY, setMouseStartY, setMouseStartX, image)}
            onMouseDown={(event)=>handleMouseDown(event, setCanvasAction, setCommentIndex, commentAction, canvasContext, setMouseStartY, setMouseStartX, setCommentPreviousState, drawnComments, commentIndex, setMouseDownTimestamp, mouseStartX, mouseStartY, canvasRef, fileId, chosenComment)}
            ref={canvasRef}/>)
}