import { useLocation } from "react-router-dom"
import { useEffect, useState } from "react"
import { FileFromPost, UserWithAssignment } from "../../types/types"
import AssigmentItem from "../assigments/assigmentDisplayer/assigmentItem/AssigmentItem"
import mongoDatabase from "../../services/mongoDatabase"
import postgresqlDatabase from "../../services/postgresDatabase"

function Solution() {
  let { state } = useLocation()
  const [file, setFile] = useState<Blob | null>(null)
  const [fileName, setFileName] = useState<string>("")
  const [isLoading, setIsLoading] = useState<boolean>()
  const [databaseFile, setDatabaseFile] = useState<FileFromPost[]>([])
  const [userWithAssignment, setUserWithAssignment] =
    useState<UserWithAssignment>(state?.userWithAssignment)

  useEffect(() => {
    if (databaseFile) {
      console.log(databaseFile)
      mongoDatabase
        .get(`file/${databaseFile[0]?.mongo_id}`, {})
        .then((response) => {
          const fileData = response.data
          const type = fileData.file.type
          const decodedData = atob(fileData.file.data)
          const byteArray = new Uint8Array(decodedData.length)
          for (let i = 0; i < decodedData.length; i++) {
            byteArray[i] = decodedData.charCodeAt(i)
          }
          const blob = new Blob([byteArray], {
            type,
          })
          setFile(blob)
          setFileName(response.data.name)
        })
        .catch((e) => console.error("Error retrieving file:", e))
    }
  }, [databaseFile])
  useEffect(() => {
    postgresqlDatabase
      .get(`/files/bySolution/${userWithAssignment.solution.id}`)
      .then((r) => setDatabaseFile(r.data))
  }, [])

  const handleDownload = () => {
    if (file && fileName) {
      const fileUrl = URL.createObjectURL(file)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = fileName
      link.click()
    }
  }
  return (
    <>
      <AssigmentItem
        idGroup={`${userWithAssignment.assignment.groupId}`}
        assignment={userWithAssignment.assignment}
      />
      <div>
        <button onClick={handleDownload}>{fileName}</button>
      </div>
    </>
  )
}
export default Solution
