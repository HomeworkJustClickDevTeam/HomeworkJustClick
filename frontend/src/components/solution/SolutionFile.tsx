import { useGetFile } from "../customHooks/useGetFile"

export function SolutionFile(props: { solutionId: number }) {
  const fileFromDb = useGetFile(props.solutionId, "solution")
  const handleDownload = () => {
    if (fileFromDb) {
      const fileUrl = URL.createObjectURL(fileFromDb.data)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = fileFromDb.name
      link.click()
    }
  }
    return (<>
      {fileFromDb !== undefined &&
      <button key={fileFromDb.name} onClick={handleDownload}>{fileFromDb.name}</button>}
    </>)
}
