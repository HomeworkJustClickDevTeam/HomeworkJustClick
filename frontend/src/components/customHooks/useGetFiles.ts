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
import { useAppDispatch } from "../../types/HooksRedux"

export const useGetFiles = (id: number, by:"assignment" | "solution") => {
  const [files, setFiles] = useState<FileInterface[]>([])
  const dispatch = useAppDispatch()

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
              if(mounted){
                setFiles(files => files.concat({
                  fileData: blob,
                  fileName: mongoResponse.data.name,
                  fileType: fileData.file.type
                }))
              }
            }
          }
        }
      } catch (e) {
        console.log("Error retrieving database file:", e)
      }
    }
    let mounted = true
    dispatch(setIsLoading(true))
    fetchData()
    dispatch(setIsLoading(false))

    return () => {mounted = false}
  }, [id, by])
  return files
}