import { ChangeEvent, useContext, useEffect, useState } from "react"
import mongoDatabase from "../../services/mongoDatabase"
import {
  AssigmentProps,
  FileRespondMongo,
  Solution,
  SolutionToSend,
} from "../../types/types"
import { useParams } from "react-router-dom"
import postgresqlDatabase from "../../services/postgresDatabase"
import AssigmentItem from "../assigments/assigmentDisplayer/assigmentItem/AssigmentItem"
import userContext from "../../UserContext"

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

  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      setFile(e.target.files[0])
    }
  }
  useEffect(() => {
    if (solutionFromServer.id && response.id) {
      postgresqlDatabase
        .post(`/file/withSolution/${solutionFromServer.id}`, {
          mongo_id: response.id,
          format: response.format,
          name: response.name,
        })
        .then((r) => {
          console.log(r)
        })
        .catch((e) => console.log(e))
    }
  }, [solutionFromServer, response])
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
    <div>
      <AssigmentItem idGroup={id} assignment={assignment} />
      <input type="file" onChange={handleChangeFile} />
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button onClick={handleUploadClick}>Wyslij zadanie</button>
    </div>
  )
}
export default AddSolution
