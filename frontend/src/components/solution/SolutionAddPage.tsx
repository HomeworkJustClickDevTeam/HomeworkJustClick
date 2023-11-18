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
import { SolutionCreateInterface } from "../../types/SolutionCreateInterface"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { selectUserState } from "../../redux/userStateSlice"
import { useAppDispatch, useAppSelector } from "../../types/HooksRedux"
import { useGetFiles } from "../customHooks/useGetFiles"


interface FileRespondMongoInterface {
  id: string
  name: string
  format: string
}

function SolutionAddPage({assignment}: AssignmentPropsInterface) {
  const navigate = useNavigate()
  const dispatch = useAppDispatch()
  const userState = useAppSelector(selectUserState)
  const [file, setFile] = useState<File>()
  const files = useGetFiles(assignment.id, 'assignment')
  const [solution] = useState<SolutionCreateInterface>({
    creationDatetime: new Date().toISOString(),
    comment: "",
    lastModifiedDatetime: new Date().toISOString(),
  })

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
        let mongoResponse:any = undefined
        let postgresResponse:any = undefined
        postFileMongoService(formData)
          .then((r) => {
            mongoResponse = r.data
            return createSolutionWithUserAndAssignmentPostgresService(userState?.id.toString(), assignment.id.toString(), solution)
          }).then((r) => {
            postgresResponse = r.data
            if(mongoResponse !== undefined && postgresResponse !== undefined){
              return createFileWithSolutionPostgresService(
                mongoResponse.id as string,
                mongoResponse.format as string,
                mongoResponse.name as string,
                postgresResponse.id as number)
            }
            else{
              return Promise.reject("Something went wrong with getting file to the dbs and getting data from them.")
            }
          })
          .then(r => {
            navigate(-1)
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
              className='absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none'>Wyślij
        zadanie
      </button>
    </div>
  )

}
export default SolutionAddPage
