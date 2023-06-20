import { ChangeEvent, useEffect, useState } from "react"
import mongoDatabase from "../../../services/mongoDatabase"
import postgresqlDatabase from "../../../services/postgresDatabase"

export function AddAssigmentFile(props: {
  toSend: boolean
  idAssigment: number | undefined
  setToNavigate: (navigate: boolean) => void
}) {
  const [file, setFile] = useState<File>()

  useEffect(() => {
    if (props.toSend && props.idAssigment) {
      if (file) {
        const formData = new FormData()
        formData.append("file", file)
        mongoDatabase
          .post("file", formData)
          .then((response) => {
            postgresqlDatabase
              .post(`file/withAssignment/${props.idAssigment}`, {
                mongo_id: response.data.id,
                format: response.data.format,
                name: response.data.name,
              })
              .then(() => props.setToNavigate(true))
          })
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
