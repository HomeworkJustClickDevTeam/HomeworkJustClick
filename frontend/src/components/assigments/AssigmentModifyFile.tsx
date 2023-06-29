import { ChangeEvent, useEffect, useState } from "react"
import { FileFromPost } from "../../types/types"
import postgresqlDatabase from "../../services/postgresDatabase"
import mongoDatabase from "../../services/mongoDatabase"

export function AssigmentModifyFile(props: {
  assignmentId: number
  setToNavigate: (navigate: boolean) => void
  toSend: boolean
}) {
  const [file, setFile] = useState<File | null>(null)
  const [databaseFile, setDatabaseFile] = useState<FileFromPost[]>([])
  const [isChange, setIsChange] = useState<boolean>(false)

  useEffect(() => {
    if (props.assignmentId) {
      postgresqlDatabase
        .get(`/files/byAssignment/${props.assignmentId}`)
        .then((response) => {
          setDatabaseFile(response.data)
        })
        .catch((error) => {
          console.error("Error retrieving file data:", error)
        })
    }
  }, [props.assignmentId])
  useEffect(() => {
    if (props.toSend && props.assignmentId) {
      if (isChange && file) {
        const formData = new FormData()
        formData.append("file", file)
        postgresqlDatabase
          .delete(`file/${databaseFile[0].id}`)
          .then(() => {
            mongoDatabase
              .post("file", formData)
              .then((response) => {
                postgresqlDatabase
                  .post(`file/withAssignment/${props.assignmentId}`, {
                    mongo_id: response.data.id,
                    format: response.data.format,
                    name: response.data.name,
                  })
                  .then(() => props.setToNavigate(true))
              })
              .catch()
          })
          .catch((e) => console.log(e))
      } else {
        props.setToNavigate(true)
      }
    }
  }, [props.toSend])

  useEffect(() => {
    const fetchFileData = async () => {
      if (databaseFile.length > 0) {
        try {
          const response = await mongoDatabase.get(
            `file/${databaseFile[0]?.mongo_id}`,
            {
              responseType: "arraybuffer",
            }
          )

          const fileData = response.data
          const type = databaseFile[0].format
          const file = new File([fileData], databaseFile[0].name, { type })
          setFile(file)
        } catch (error) {
          console.error("Error retrieving file:", error)
        }
      }
    }

    fetchFileData()
  }, [databaseFile])
  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      setFile(e.target.files[0])
      setIsChange(true)
    }
  }
  console.log(isChange)
  return (
    <>
      <>
        <input type="file" onChange={handleChangeFile} />
        <div> {file && `${file.name} - ${file.type}`}</div>{" "}
      </>
    </>
  )
}
