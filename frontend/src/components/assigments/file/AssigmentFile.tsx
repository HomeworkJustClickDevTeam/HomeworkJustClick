import { useEffect, useState } from "react"
import { FileFromPost } from "../../../types/types"
import postgresqlDatabase from "../../../services/postgresDatabase"
import mongoDatabase from "../../../services/mongoDatabase"
import Loading from "../../animations/Loading"

export function AssigmentFile(props: { assigmentId: number }) {
  const [databaseFile, setDatabaseFile] = useState<FileFromPost[]>([])
  const [file, setFile] = useState<Blob | null>(null)
  const [fileName, setFileName] = useState<string>("")
  const [isLoading, setIsLoading] = useState<boolean>(true)
  useEffect(() => {
    const fetchData = async () => {
      if (props.assigmentId) {
        try {
          const response = await postgresqlDatabase.get(
            `/files/byAssignment/${props.assigmentId}`
          )
          console.log(response.data)
          setDatabaseFile(response.data)
        } catch (e) {
          console.log("Error retrieving database file:", e)
        }
      }
    }
    fetchData()
  }, [props.assigmentId])
  console.log(databaseFile)
  useEffect(() => {
    const fetchFileData = async () => {
      if (databaseFile) {
        try {
          const response = await mongoDatabase.get(
            `file/${databaseFile[0]?.mongo_id}`,
            {}
          )
          const fileData = response.data
          const type = fileData.file.type
          const decodedData = atob(fileData.file.data)
          const byteArray = new Uint8Array(decodedData.length)
          for (let i = 0; i < decodedData.length; i++) {
            byteArray[i] = decodedData.charCodeAt(i)
          }
          const blob = new Blob([byteArray], { type })
          setFile(blob)
          setFileName(response.data.name)
          setIsLoading(false)
        } catch (error) {
          console.error("Error retrieving file:", error)
        }
      }
    }
    fetchFileData()
      .then()
      .catch((e) => console.log(e))
  }, [databaseFile])

  const handleDownload = () => {
    if (file && fileName) {
      const fileUrl = URL.createObjectURL(file)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = fileName
      link.click()
    }
  }
  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      <div>
        <button onClick={handleDownload}>{fileName}</button>
      </div>
    </>
  )
}
