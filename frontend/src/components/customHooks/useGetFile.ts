import { useEffect, useState } from "react"
import { FileFromPostgresInterface } from "../../types/FileFromPostgresInterface"
import {
  getFilesByAssignmentPostgresService,
  getFilesBySolutionPostgresService
} from "../../services/postgresDatabaseServices"
import { getFileMongoService } from "../../services/mongoDatabaseServices"
import { FileInterface } from "../../types/FileInterface"
import { setIsLoading } from "../../redux/isLoadingSlice"
import { useAppDispatch } from "../../types/HooksRedux"

export const useGetFile = (filterId: number|undefined|null, filter:"assignment" | "solution") => {
  const [file, setFile] = useState<FileInterface|undefined>(undefined)
  const dispatch = useAppDispatch()

  useEffect(() => {
    const fetchData = async () => {
      let response = null
      if(filterId !== undefined && filterId !== null) {
        try {
          if(filter === "solution"){
            response = await getFilesBySolutionPostgresService(filterId)
          }
          else if(filter === "assignment"){
            response = await getFilesByAssignmentPostgresService(filterId)
          }
          if(response !== null && response !== undefined){
            const databaseFiles:FileFromPostgresInterface[] = response.data
            let updatedFiles:FileInterface[] = []
            for (const file of databaseFiles) {
              if(file){
                const mongoResponse = await getFileMongoService(file.mongoId)
                if(mongoResponse !== undefined && mongoResponse!==null){
                  const fileData = mongoResponse.data
                  const type = fileData.file.type
                  const decodedData = window.atob(fileData.file.data)
                  const byteArray = new Uint8Array(decodedData.length)
                  for (let i = 0; i < decodedData.length; i++) {
                    byteArray[i] = decodedData.charCodeAt(i)
                  }
                  const blob = new Blob([byteArray], {type})
                  updatedFiles.push({
                    data: blob,
                    name: mongoResponse.data.name,
                    format: fileData.format,
                    mongoId: file.mongoId,
                    id: file.id
                  })
                }
              }
            }
            if(mounted){
              if(updatedFiles.length > 0) setFile(updatedFiles[0])
              else setFile(undefined)
            }
          }
        }catch (error:any) {
          if(error !== null && error!== undefined && error.response !== null && error.response!==undefined && error.response.status === 404){
            if(mounted){setFile(undefined)}
          }
          else{
            console.log("Error retrieving database file:", error)
          }
        }

      }
    }
    let mounted = true
    dispatch(setIsLoading(true))
    fetchData()
    dispatch(setIsLoading(false))
    return () => {mounted = false}
  }, [filterId, filter])
  return file
}