import {useContext, useEffect, useState} from "react";
import {FileFromPostInterface} from "../../types/FileFromPostInterface";
import {
  getFilesByAssignmentPostgresService,
  getFilesBySolutionPostgresService
} from "../../services/postgresDatabaseServices";
import {getFileMongoService} from "../../services/mongoDatabaseServices";
import {FileInterface} from "../../types/FileInterface";
import {LoadingContext} from "../../contexts/LoadingContext";

export const useGetFiles = (id: number, by:"assignment" | "solution") => {
  const [files, setFiles] = useState<FileInterface[]>([])
  const [databaseFiles, setDatabaseFiles] = useState<FileFromPostInterface[] | undefined>(undefined)
  const {setIsLoading} = useContext(LoadingContext)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setIsLoading(true)
        if(by === "solution"){
          const response = await getFilesBySolutionPostgresService(id)
          if(!ignore) {
            setDatabaseFiles(response.data)
            setIsLoading(false)
          }
        }
        else if(by === "assignment"){
          const response = await getFilesByAssignmentPostgresService(id)
          if(!ignore) {
            setDatabaseFiles(response.data)
            setIsLoading(false)
          }
        }
      } catch (e) {
        console.log("Error retrieving database file:", e)
      }
    }
    let ignore = false
    fetchData()
    return () => {ignore = true}
  }, [id])
  useEffect(() => {
    const fetchFileData = async () => {
      if (databaseFiles) {
        setIsLoading(true)
        for (const file of databaseFiles) {
          if(file !== undefined) {
            try {
              const response = await getFileMongoService(file.mongo_id)
              const fileData = response.data
              const type = fileData.file.type
              const decodedData = window.atob(fileData.file.data)
              const byteArray = new Uint8Array(decodedData.length)
              for (let i = 0; i < decodedData.length; i++) {
                byteArray[i] = decodedData.charCodeAt(i)
              }
              const blob = new Blob([byteArray], {type})
              if(!ignore){
                setFiles(files => files.concat({
                  fileData: blob,
                  fileName: response.data.name,
                  fileType: fileData.file.type
                }))
              }
            } catch (error) {
              console.error("Error retrieving file:", error)
            }
          }
        }
      }
    }
    let ignore = false
    fetchFileData()
      .then()
      .catch((e) => console.log(e))
    return () => {ignore = true}
  }, [databaseFiles])
  return files
}