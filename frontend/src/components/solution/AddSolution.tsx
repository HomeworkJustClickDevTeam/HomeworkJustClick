import { ChangeEvent, useContext, useEffect, useState } from "react"
import mongoDatabase from "../../services/mongoDatabase"
import {
  AssigmentProps,
  FileRespondMongo,
  Solution,
  SolutionToSend,
} from "../../types/types"
import { useNavigate, useParams } from "react-router-dom"
import postgresqlDatabase from "../../services/postgresDatabase"
import AssigmentListElement from "../assigments/AssigmentListElement"
import userContext from "../../UserContext"
import { AssigmentFile } from "../assigments/AssigmentFile"
import {format} from "date-fns";

function AddSolution({ assignment }: AssigmentProps) {
  const { idAssigment, id = "" } = useParams()
  const { userState } = useContext(userContext)
  const [file, setFile] = useState<File>()
  const [response, setResponse] = useState<FileRespondMongo>({
    format: "",
    id: "",
    name: "",
  })
  const [solution] = useState<SolutionToSend>({
    creationDatetime: new Date().toISOString(),
    comment: "",
    lastModifiedDatetime: new Date().toISOString(),
  })
  const [solutionFromServer, setSolutionFromServer] = useState<Solution>({
    assignmentId: 0,
    comment: "",
    creationDateTime: "",
    groupId: 0,
    id: 0,
    lastModifiedDateTime: "",
    userId: 0,
  })
  const navigate = useNavigate()
  const [isFile, setIsFile] = useState<boolean>()

  useEffect(() => {
    postgresqlDatabase
      .get(`/files/byAssignment/${idAssigment}`)
      .then(() => setIsFile(true))
      .catch(() => setIsFile(false))
  }, [])

  useEffect(() => {
    if (solutionFromServer.id && response.id) {
      postgresqlDatabase
        .post(`/file/withSolution/${solutionFromServer.id}`, {
          mongo_id: response.id,
          format: response.format,
          name: response.name,
        })
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
    const formData = new FormData()
    formData.append("file", file)
    mongoDatabase
      .post("file", formData)
      .then((r) => {
        console.log(r.data)
        setResponse(r.data)
      })
      .catch()
    postgresqlDatabase
      .post(
        `/solution/withUserAndAssignment/${userState.userId}/${idAssigment}`,
        solution
      )
      .then((r) => {
        console.log(r)
        setSolutionFromServer(r.data)
      })
      .catch()
  }

  return (
    <div className='relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2'>
      <div><span className='font-semibold'>Tytuł zadania: </span>{assignment.title}</div>
      <div><span className='font-semibold'>Data ukończenia: </span>{format(assignment.completionDatetime, "dd.MM.yyyy, HH:mm")}</div>
      {isFile && <AssigmentFile assigmentId={assignment.id} />}
      <input type="file" onChange={handleChangeFile} />
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button onClick={handleUploadClick} className='absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none'>Wyslij zadanie</button>
    </div>
  )
}
export default AddSolution
