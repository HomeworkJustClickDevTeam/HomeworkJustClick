import { ChangeEvent, useEffect, useState } from "react"
import {postFileMongoService} from "../../services/mongoDatabase"
import postgresqlDatabase, {postFileWithAssignmentPostgresService} from "../../services/postgresDatabase"
import {AssignmentAddFilePropsInterface} from "../../types/AssignmentAddFilePropsInterface";

export function AssigmentAddFile(props: AssignmentAddFilePropsInterface) {
  const [file, setFile] = useState<File>()

  useEffect(() => {
    if (props.toSend && props.idAssigment) {
      if (file) {
        const formData = new FormData()
        formData.append("file", file)
        postFileMongoService(formData)
          .then((response) => postFileWithAssignmentPostgresService(`${response.data.id}`, response.data.format, response.data.name, `${props.idAssigment}`))
          .catch()
      } else {
        props.setToNavigate(true)
      }
    }
  }, [props.toSend])
  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      setFile(e.target.files[0])
    }
  }
  return (
    <>
      <input type="file" onChange={handleChangeFile} />
      <div> {file && `${file.name} - ${file.type}`}</div>{" "}
    </>
  )
}
