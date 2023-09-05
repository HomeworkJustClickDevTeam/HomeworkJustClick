import React, {ChangeEvent, useContext, useEffect, useState} from "react"
import {postFileMongoService} from "../../services/mongoDatabaseServices"
import {useNavigate, useParams} from "react-router-dom"
import {
  getFilesByAssignmentPostgresService,
  createFileWithSolutionPostgresService,
  createSolutionWithUserAndAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import {AssignmentFile} from "../assignments/AssignmentFile"
import {format} from "date-fns";
import {AssignmentPropsInterface} from "../../types/AssignmentPropsInterface";
import {SolutionInterface} from "../../types/SolutionInterface";
import {SolutionToSendInterface} from "../../types/SolutionToSendInterface";
import {getUser} from "../../services/otherServices";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";
import Loading from "../animations/Loading";


interface FileRespondMongoInterface {
  id: string
  name: string
  format: string
}

function SolutionAdd({assignment}: AssignmentPropsInterface) {
  const navigate = useNavigate()
  const [loading, setLoading] = useState<boolean>(true)
  const {idAssignment} = useParams()
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
    setLoading(true)
    let ignore = false
    getFilesByAssignmentPostgresService(idAssignment as string)
      .then(() =>{
        if(!ignore){
          setIsFile(true)
          setLoading(false)
        }

      } )
      .catch(() => setIsFile(false))
    return () => {
      ignore = true
    }
  }, [])

  useEffect(() => {

    if (solutionFromServer?.id && response.id) {
      setLoading(true)
      let ignore = false
      createFileWithSolutionPostgresService(
        response.id,
        response.format,
        response.name,
        solutionFromServer.id)
        .then((r) => {
          if(!ignore){
            navigate(-1)
            setLoading(false)
            console.log(r)
          }
        })
        .catch((e) => console.log(e))
      return () =>{
        ignore = true
      }
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
      createSolutionWithUserAndAssignmentPostgresService(applicationState?.userState.id.toString(), idAssignment as string, solution)
        .then((r) => {
          console.log(r)
          setSolutionFromServer(r.data)
        })
        .catch()
    }
  }

  if(loading){
    return <Loading></Loading>
  }

  return (
    <div
      className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
      <div><span className='font-semibold'>Tytuł zadania: </span>{assignment.title}</div>
      <div><span
        className='font-semibold'>Data ukończenia: </span>{format(assignment.completionDatetime, "dd.MM.yyyy, HH:mm")}
      </div>
      {isFile && <AssignmentFile assignmentId={assignment.id}/>}
      <input type="file" onChange={handleChangeFile}/>
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button onClick={handleUploadClick}
              className='absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none'>Wyslij
        zadanie
      </button>
    </div>
  )
}

export default SolutionAdd
