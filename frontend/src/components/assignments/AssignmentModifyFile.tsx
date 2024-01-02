import { ChangeEvent, useEffect, useState } from "react"
import {
  createFileWithAssignmentPostgresService,
  deleteFilePostgresService,
  getFilesByAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import { getFileMongoService, postFileMongoService } from "../../services/mongoDatabaseServices"
import { FileFromPostgresInterface } from "../../types/FileFromPostgresInterface"
import {useGetFile} from "../customHooks/useGetFile";
import {CreateFileUrlAndClick} from "../../utils/CreateFileUrlAndClick";

export function AssignmentModifyFile(props: {
  assignmentId: number
  setToNavigate: (navigate: boolean) => void
  toSend: boolean
}) {
  const [file, setFile] = useState<File | null>(null)
  const databaseFile = useGetFile(props.assignmentId, 'assignment')
  const [isChange, setIsChange] = useState<boolean>(false)

  useEffect(() => {
    const updateFileInDb = async () => {
      if (props.toSend && props.assignmentId) {
        if (isChange && file) {
          const formData = new FormData()
          formData.append("file", file)
          if(databaseFile !== undefined){
            try {
              const response = await deleteFilePostgresService(databaseFile.id.toString())
              if(response?.status !== 200 && response?.status !== 404) {
                return
              }
            }
            catch (error) {
              console.log(error)
              return
            }
          }
          postFileMongoService(formData)
            .then((response) => {
              if(response?.status === 200){
                createFileWithAssignmentPostgresService(`${response.data.id}`, response.data.format, response.data.name, `${props.assignmentId}`)
                  .then(() => props.setToNavigate(true))
              }
            })
            .catch(error => console.error(error))
        }
      }
    }
    updateFileInDb()
  }, [props.toSend])

  useEffect(() => {
    const fetchFileData = async () => {
      if (databaseFile !== undefined) {
        try {
          const response = await getFileMongoService(databaseFile.mongoId, {responseType: "arraybuffer"})
          const fileData = response.data
          const type = databaseFile.format
          const file = new File([fileData], databaseFile.name, {type})
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
    <div>
      <input type="file" onChange={handleChangeFile}/><br/>
      {databaseFile && <>Plik w bazie danych: <br/>
          <button onClick={()=>CreateFileUrlAndClick(databaseFile!)}> {`${databaseFile!.name}`}</button></>}
      {" "}
    </div>
  )
}
