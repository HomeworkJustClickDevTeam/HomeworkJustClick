import { useLocation } from "react-router-dom"
import { useEffect, useState } from "react"
import { FileFromPost, SolutionExtended } from "../../types/types"
import AssigmentItem from "../assigments/assigmentDisplayer/assigmentItem/AssigmentItem"
import mongoDatabase from "../../services/mongoDatabase"
import postgresqlDatabase from "../../services/postgresDatabase"
import Loading from "../animations/Loading"
import { Rating } from "../evaluation/Rating"

function Solution() {
  let { state } = useLocation()
  const [file, setFile] = useState<Blob | null>(null)
  const [fileName, setFileName] = useState<string>("")
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [databaseFile, setDatabaseFile] = useState<FileFromPost[]>([])
  const [solutionExtended] = useState<SolutionExtended>(state?.solution)
  const [points, setPoints] = useState<number>()
  const [showRating, setShowRating] = useState<boolean>(false)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await postgresqlDatabase.get(
          `/files/bySolution/${solutionExtended.id}`
        )
        console.log(response.data)
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
          console.log("XD")
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
  const handleDisableRating = () => {
    setShowRating(false)
  }
  const handleShowRating = () => {
    setShowRating(true)
  }
  if (isLoading) {
    return <Loading />
  }
  return (
    <>
      <AssigmentItem
        idGroup={`${solutionExtended.assignment.groupId}`}
        assignment={solutionExtended.assignment}
      />
      <h1>
        Points: {points} /{solutionExtended.assignment.max_points}
      </h1>
      <div>
        <button onClick={handleDownload}>{fileName}</button>
      </div>
      {showRating ? (
        <div>
          <Rating
            maxPoints={solutionExtended.assignment.max_points}
            points={points}
            setPoints={setPoints}
            solutionId={solutionExtended.assignment.id}
            groupId={solutionExtended.assignment.groupId}
          />
          <button onClick={handleDisableRating}>Schowaj Punkty</button>
        </div>
      ) : (
        <button onClick={handleShowRating}>Pokaz Punkty</button>
      )}
    </>
  )
}
export default Solution
