import { ChangeEvent, useEffect, useState } from "react"
import { postFileMongoService } from "../../services/mongoDatabaseServices"
import { createFileWithAssignmentPostgresService } from "../../services/postgresDatabaseServices"
import { AssignmentAddFilePropsInterface } from "../../types/AssignmentAddFilePropsInterface"

export function AssignmentAddFile(props: AssignmentAddFilePropsInterface) {
  const [file, setFile] = useState<File>()

  useEffect(() => {
    if (props.toSend && props.idAssignment) {
      if (file) {
        const formData = new FormData()
        formData.append("file", file)
        postFileMongoService(formData)
          .then((response) =>
            createFileWithAssignmentPostgresService(
              `${response.data.id}`,
              response.data.format,
              response.data.name,
              `${props.idAssignment}`
            )
          )
          .catch()
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
    <div>
      <input type="file" onChange={handleChangeFile} />
    </div>
  )
}
