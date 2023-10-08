import React, { ChangeEvent, useEffect, useState } from "react"
import { postFileMongoService } from "../../services/mongoDatabaseServices"
import { useNavigate } from "react-router-dom"
import {
  createFileWithSolutionPostgresService,
  createSolutionWithUserAndAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import { AssignmentFile } from "../assignments/AssignmentFile"
import { format } from "date-fns"
import { AssignmentPropsInterface } from "../../types/AssignmentPropsInterface"
import { SolutionInterface } from "../../types/SolutionInterface"
import { SolutionToSendInterface } from "../../types/SolutionToSendInterface"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { useGetFiles } from "../customHooks/useGetFiles"


interface FileRespondMongoInterface {
  id: string
  name: string
  format: string
}

function SolutionAdd({assignment}: AssignmentPropsInterface) {
  const navigate = useNavigate()
  const dispatch = useAppDispatch()
  const userState = useAppSelector(selectUserState)
  const [file, setFile] = useState<File>()
  const [response, setResponse] = useState<FileRespondMongoInterface | undefined>(undefined)
  const files = useGetFiles(assignment.id, 'assignment')
  const [solution] = useState<SolutionToSendInterface>({
    creationDatetime: new Date().toISOString(),
    comment: "",
    lastModifiedDatetime: new Date().toISOString(),
  })

  const [solutionFromServer, setSolutionFromServer] = useState<SolutionInterface | undefined>(undefined)


  useEffect(() => {
    dispatch(setIsLoading(true))
    let mounted = true
    if (mounted) {
      if((response && solutionFromServer) !== undefined) {
        createFileWithSolutionPostgresService(
          response?.id as string,
          response?.format as string,
          response?.name as string,
          solutionFromServer?.id as number)
          .then((r) => {
            if (mounted) {
              navigate(-1)
            }
          })
          .catch((e) => console.log(e))
      }
    }
    dispatch(setIsLoading(false))
    return () => {
      mounted = false
    }
  }, [solutionFromServer?.id, response?.id])


  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    e.preventDefault()
    if (e.target.files) {
      setFile(e.target.files[0])
    }
  }

  function handleUploadClick(e: React.MouseEvent<HTMLElement>) {
    e.preventDefault()
    if (!file) {
      return
    } else {
      if (userState) {
        const formData = new FormData()
        formData.append("file", file)
        postFileMongoService(formData)
          .then((r) => {
            setResponse(r.data)
          })
          .catch((e) => console.log(e))
        createSolutionWithUserAndAssignmentPostgresService(userState?.id.toString(), assignment.id.toString(), solution)
          .then((r) => {
            setSolutionFromServer(r.data)
          })
          .catch((e) => console.log(e))
      }
    }
  }


  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
      <div><span className='font-semibold'>Tytuł zadania: </span>{assignment.title}</div>
      <div><span
        className='font-semibold'>Data ukończenia: </span>{format(assignment.completionDatetime, "dd.MM.yyyy, HH:mm")}
      </div>
      {files.length > 0 && <AssignmentFile assignmentId={assignment.id} />}
      <input type="file" onChange={(e)=>handleChangeFile(e)} />
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button type={"submit"} onClick={(e) => handleUploadClick(e)}
              className='absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none'>Wyslij
        zadanie
      </button>
    </div>
  )

}
export default SolutionAdd
