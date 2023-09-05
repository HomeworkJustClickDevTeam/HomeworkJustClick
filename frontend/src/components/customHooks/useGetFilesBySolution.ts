import {useEffect, useState} from "react";
import {FileFromPostInterface} from "../../types/FileFromPostInterface";
import {getFilesBySolutionPostgresService} from "../../services/postgresDatabaseServices";
import {getFileMongoService} from "../../services/mongoDatabaseServices";

export const useGetFilesBySolution = (props:{solutionId: number}) => {
  const [files, setFiles] = useState<FileFromPostInterface[]>([])
  const [databseFile, setDatabaseFile] = useState()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getFilesBySolutionPostgresService(props.solutionId)
        setDatabaseFile(response.data)
      } catch (e) {
        console.log("Error retrieving database file:", e)
      }
    }
    fetchData()
  }, [])
  useEffect(() => {
    const fetchFileData = async () => {
      if (databaseFile) {
        try {
          const response = await getFileMongoService(databaseFile[0]?.mongo_id)
          const fileData = response.data
          const type = fileData.file.type
          const decodedData = window.atob(fileData.file.data)
          const byteArray = new Uint8Array(decodedData.length)
          for (let i = 0; i < decodedData.length; i++) {
            byteArray[i] = decodedData.charCodeAt(i)
          }
          const blob = new Blob([byteArray], {type})
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

}