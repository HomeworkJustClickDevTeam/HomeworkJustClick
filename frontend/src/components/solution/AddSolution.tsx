import React, {ChangeEvent, useContext, useEffect, useState} from "react"
import {postFileMongoService} from "../../services/mongoDatabaseServices"
import {useNavigate, useParams} from "react-router-dom"
import {
  getFilesByAssignmentPostgresService,
  postFileWithSolutionPostgresService,
  postSolutionWithUserAndAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import {AssigmentFile} from "../assigments/AssigmentFile"
import {format} from "date-fns";
import {AssigmentPropsInterface} from "../../types/AssigmentPropsInterface";
import {SolutionInterface} from "../../types/SolutionInterface";
import {SolutionToSendInterface} from "../../types/SolutionToSendInterface";
import {getUser} from "../../services/otherServices";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";


interface FileRespondMongoInterface {
  id: string
  name: string
  format: string
}

function AddSolution({assignment}: AssigmentPropsInterface) {
  const navigate = useNavigate()
  const {idAssigment} = useParams()
  const {applicationState} = useContext(ApplicationStateContext)
  const [file, setFile] = useState<File>()
  const [response, setResponse] = useState<FileRespondMongoInterface>({
    format: "",
    id: "",
    name: "",
  })
  const [solution] = useState<SolutionToSendInterface>({
    creationDatetime: new Date().toISOString(),
    comment: "",
    lastModifiedDatetime: new Date().toISOString(),
  })

  const [solutionFromServer, setSolutionFromServer] = useState<SolutionInterface | undefined>(undefined)
  const [isFile, setIsFile] = useState<boolean>()


  useEffect(() => {
    getFilesByAssignmentPostgresService(idAssigment as string)
      .then(() => setIsFile(true))
      .catch(() => setIsFile(false))
  }, [])

  useEffect(() => {

    if (solutionFromServer?.id && response.id) {
      postFileWithSolutionPostgresService(
        response.id,
        response.format,
        response.name,
        solutionFromServer.id)
        .then((r) => {
          navigate(-1)
          console.log(r)
        })
        .catch((e) => console.log(e))
    }
  }, [solutionFromServer, response])

  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      setFile(e.target.files[0])
    }
  }

  function handleUploadClick() {
    if (!file) {
      return
    }
    if (applicationState?.userState === undefined) {
      navigate("/")
    } else {
      const formData = new FormData()
      formData.append("file", file)
      postFileMongoService(formData)
        .then((r) => {
          console.log(r.data)
          setResponse(r.data)
        })
        .catch()
      postSolutionWithUserAndAssignmentPostgresService(applicationState?.userState.id.toString(), idAssigment as string, solution)
        .then((r) => {
          console.log(r)
          setSolutionFromServer(r.data)
        })
        .catch()
    }
  }

  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
      <div><span className='font-semibold'>Tytuł zadania: </span>{assignment.title}</div>
      <div><span
        className='font-semibold'>Data ukończenia: </span>{format(assignment.completionDatetime, "dd.MM.yyyy, HH:mm")}
      </div>
      {isFile && <AssigmentFile assigmentId={assignment.id}/>}
      <input type="file" onChange={handleChangeFile}/>
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button onClick={handleUploadClick}
              className='absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none'>Wyslij
        zadanie
      </button>
    </div>
  )
}

export default AddSolution
