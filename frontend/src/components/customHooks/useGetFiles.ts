import { useContext, useEffect, useState } from "react"
import { FileFromPostgresInterface } from "../../types/FileFromPostgresInterface"
import {
  getFilesByAssignmentPostgresService,
  getFilesBySolutionPostgresService
} from "../../services/postgresDatabaseServices"
import { getFileMongoService } from "../../services/mongoDatabaseServices"
import { FileInterface } from "../../types/FileInterface"
import { useDispatch } from "react-redux"
import { setIsLoading } from "../../redux/isLoadingSlice"

export const useGetFiles = (id: number, by:"assignment" | "solution") => {
  const [files, setFiles] = useState<FileInterface[]>([])
  const dispatch = useDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      try {
        if(by === "solution"){
          response = await getFilesBySolutionPostgresService(id)
        }
        else if(by === "assignment"){
          response = await getFilesByAssignmentPostgresService(id)
        }
        if(response){
          const databaseFiles:FileFromPostgresInterface[] = response.data
          for (const file of databaseFiles) {
            if(file){
              const mongoResponse = await getFileMongoService(file.mongo_id)
              const fileData = mongoResponse.data
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
                  fileName: mongoResponse.data.name,
                  fileType: fileData.file.type
                }))
                dispatch(setIsLoading(false))
              }
            }
          }
        }
      } catch (e) {
        console.log("Error retrieving database file:", e)
      }
    }
    let ignore = false
    dispatch(setIsLoading(true))
    fetchData()
    dispatch(setIsLoading(false))
    return () => {ignore = true}
  }, [id, by])
  return files
}