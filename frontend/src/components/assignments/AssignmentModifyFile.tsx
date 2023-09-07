import { ChangeEvent, useEffect, useState } from "react"
import {
  createFileWithAssignmentPostgresService,
  deleteFilePostgresService,
  getFilesByAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import { getFileMongoService, postFileMongoService } from "../../services/mongoDatabaseServices"
import { FileFromPostgresInterface } from "../../types/FileFromPostgresInterface"

export function AssignmentModifyFile(props: {
  assignmentId: number
  setToNavigate: (navigate: boolean) => void
  toSend: boolean
}) {
  const [file, setFile] = useState<File | null>(null)
  const [databaseFile, setDatabaseFile] = useState<FileFromPostgresInterface[]>([])
  const [isChange, setIsChange] = useState<boolean>(false)

  useEffect(() => {
    if (props.assignmentId) {
      getFilesByAssignmentPostgresService(props.assignmentId)
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
        deleteFilePostgresService('databaseFile[0].id')
          .then(() => {
            postFileMongoService(formData)
              .then((response) => {
                createFileWithAssignmentPostgresService(`${response.data.id}`, response.data.format, response.data.name, `${props.assignmentId}`)
                  .then(() => props.setToNavigate(true))
              })
              .catch(error => console.log(error))
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
          const response = await getFileMongoService(databaseFile[0]?.mongo_id, {responseType: "arraybuffer"})
          const fileData = response.data
          const type = databaseFile[0].format
          const file = new File([fileData], databaseFile[0].name, {type})
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
  return (
    <>
      <>
        <input type="file" onChange={handleChangeFile}/>
        <div> {file && `${file.name} - ${file.type}`}</div>
        {" "}
      </>
    </>
  )
}
