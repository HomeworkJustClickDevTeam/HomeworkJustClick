import { CommentInterface } from "../../types/CommentInterface"
import React, {
  MutableRefObject,
  SetStateAction,
  useEffect,
  useImperativeHandle,
  useLayoutEffect,
  useRef,
  useState
} from "react"
import { AdvancedEvaluationImageCommentModel } from "../../types/AdvancedEvaluationImageComment.model"
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
import {useAppSelector} from "../../types/HooksRedux";
import {selectUserState} from "../../redux/userStateSlice";
import {FileInterface} from "../../types/FileInterface";

interface AdvancedEvaluationImageAreaInterface{
  width:number|undefined,
  height:number|undefined,
  deletedCommentId?: number|undefined,
  setDeletedCommentId?:React.Dispatch<React.SetStateAction<number|undefined>>,
  updatedComment?:CommentInterface|undefined,
  setUpdatedComment?:React.Dispatch<React.SetStateAction<CommentInterface|undefined>>,
  setSortButtonState?:React.Dispatch<React.SetStateAction<SortButtonStateType>>,
  image: HTMLImageElement,
  chosenComment?: CommentInterface|undefined,
  file?: FileInterface
  commentsImage: AdvancedEvaluationImageCommentModel[]
  setCommentsImage?: React.Dispatch<React.SetStateAction<AdvancedEvaluationImageCommentModel[]>>
  handleCommentHighlighting:(commentId: number) => void
  setRefreshRightPanelUserComments?: React.Dispatch<React.SetStateAction<boolean>>
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
                                              file,
                                              image,
                                              chosenComment,
                                              width,
                                              height}: AdvancedEvaluationImageAreaInterface) =>{
  const canvasRef = useRef<HTMLCanvasElement|null>(null)
  const canvasContext = useRef(canvasRef.current?.getContext("2d"))
  const [mouseStartX, setMouseStartX] = useState<null|number>(null)
  const [mouseStartY, setMouseStartY] = useState<null|number>(null)
  const [commentIndex, setCommentIndex] = useState<null|number>(null)
  const [commentPreviousState, setCommentPreviousState] = useState<null|AdvancedEvaluationImageCommentModel>(null)
  const [canvasAction, setCanvasAction] = useState<CanvasActionsType>("hovering")
  const [commentAction, setCommentAction] = useState<CommentActionsType>("noHover")
  const [mouseDownTimestamp, setMouseDownTimestamp] = useState<number|null>(null)
  const drawnComments = useRef<AdvancedEvaluationImageCommentModel[]>(commentsImage)


  useLayoutEffect(() => {
    canvasContext.current = canvasRef.current?.getContext("2d")
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef,
      setCommentsImage)
  }, [])
  useEffect(() => {
    drawnComments.current = commentsImage
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef,
      setCommentsImage)
  }, [width, height, commentsImage, image])
  useUpdateEffect(()=> {
    const updateImageList = async (updatedComment:CommentInterface) => {
      if (drawnComments?.current !== undefined && drawnComments?.current !== null) {
        const drawnCommentsTemp = await Promise.all(drawnComments.current.map(async (commentImage) => {
          if(commentImage.comment.id === updatedComment.id){
            try {
              const updatedCommentExtended: AdvancedEvaluationImageCommentModel = {...commentImage, color: updatedComment.color}
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
          canvasRef,
          setCommentsImage)
      }
    }
    if(updatedComment!== undefined && setUpdatedComment !== undefined)
      updateImageList(updatedComment)
        .then(()=> setUpdatedComment(undefined))
  }, [updatedComment])
  useUpdateEffect(()=> {
    const handleCommentImageDeletion = async (deletedCommentId:number) => {
      if(drawnComments?.current !== undefined && drawnComments?.current !== null) {
        try {
          const response = await deleteCommentImageByCommentFilePostgresService(deletedCommentId.toString(), file!.id.toString())
          if(response?.status === 200){
            const drawnCommentsTemp = drawnComments.current.filter( commentImage => !(commentImage.comment.id === deletedCommentId))
            drawnComments.current = drawnCommentsTemp
            drawOnCanvas(canvasContext,
              image,
              drawnComments,
              width,
              height,
              canvasRef,
              setCommentsImage)
          }
        }catch (error){
          console.log(error)
        }
      }
    }

    if(deletedCommentId!== undefined && setDeletedCommentId!==undefined && file !== undefined)
      handleCommentImageDeletion(deletedCommentId)
        .then(()=> setDeletedCommentId(undefined))
  }, [deletedCommentId])
  useUpdateEffect(()=>{
    drawOnCanvas(canvasContext,
      image,
      drawnComments,
      width,
      height,
      canvasRef,
      setCommentsImage)
  }, [mouseStartY, mouseStartX])
  return (
    <canvas onContextMenu={(event) => {
              event.preventDefault();
              event.stopPropagation()
              setUpdatedComment !== undefined && setCanvasAction("commentDeleting")}}
            onMouseOut={async (event)=>{
              await handleMouseUp(event,
                canvasAction,
                setCanvasAction,
                setCommentIndex,
                setMouseStartY,
                setMouseStartX,
                setMouseDownTimestamp,
                setCommentPreviousState,
                commentIndex,
                mouseDownTimestamp,
                handleCommentHighlighting,
                drawnComments,
                commentPreviousState,
                image,
                canvasContext,
                canvasRef,
                setCommentAction,
                commentAction,
                width,
                height,
                setSortButtonState,
                setCommentsImage,
                setRefreshRightPanelUserComments)
            }}
            id={"commentsLayer"}
            onMouseUp={async (event)=>{
              await handleMouseUp(event,
                canvasAction,
                setCanvasAction,
                setCommentIndex,
                setMouseStartY,
                setMouseStartX,
                setMouseDownTimestamp,
                setCommentPreviousState,
                commentIndex,
                mouseDownTimestamp,
                handleCommentHighlighting,
                drawnComments,
                commentPreviousState,
                image,
                canvasContext,
                canvasRef,
                setCommentAction,
                commentAction,
                width,
                height,
                setSortButtonState,
                setCommentsImage,
                setRefreshRightPanelUserComments)
            }}
            onMouseMove={(event)=>{
              handleMouseMove(event,
                canvasAction,
                setCommentIndex,
                canvasContext,
                canvasRef,
                drawnComments,
                setCommentAction,
                commentAction,
                commentIndex,
                mouseStartX,
                width,
                height,
                mouseStartY,
                setMouseStartY,
                setMouseStartX,
                image,
                setCommentsImage)
            }}
            onMouseDown={(event)=>{
              handleMouseDown(event,
                setCanvasAction,
                setCommentIndex,
                commentAction,
                canvasContext,
                setMouseStartY,
                setMouseStartX,
                setCommentPreviousState,
                drawnComments,
                commentIndex,
                setMouseDownTimestamp,
                canvasRef,
                file,
                chosenComment)
            }}
            ref={canvasRef}/>)
}