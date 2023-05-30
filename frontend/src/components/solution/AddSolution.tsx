import { ChangeEvent, useState } from "react"
import file_sender from "../../services/file-sender"
import { FileRespondMongo, SolutionToSend } from "../../types/types"
import { useParams } from "react-router-dom"
import common_request from "../../services/default-request-database"

function AddSolution() {
  const [file, setFile] = useState<File>()
  const [response, setResponse] = useState<FileRespondMongo>()
  const [solution, setSolution] = useState<SolutionToSend>({
    creationDatetime: new Date().toISOString(),
    comment: "",
    lastModifiedDatetime: new Date().toISOString(),
  })
  const { idAssigment } = useParams()
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
    file_sender
      .post("file", formData)
      .then((r) => setResponse(r.data))
      .catch((e) => console.log(e.toLocaleString()))
    common_request
      .post(
        `/solution/withUserAndAssignment/${localStorage.getItem(
          "id"
        )}/${idAssigment}`,
        solution
      )
      .then((r) => console.log(r.data))
      .catch((e) => console.log(e))
    // common_request.post(`/file/withSolution/${id}`,response).then(r=>console.log(r)).catch(e => console.log(e))
  }

  return (
    <div>
      <input type="file" onChange={handleChangeFile} />
      <div> {file && `${file.name} - ${file.type}`}</div>
      <button onClick={handleUploadClick}>Wyslij zadanie</button>
    </div>
  )
}
export default AddSolution
