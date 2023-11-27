import { useGetFiles } from "../customHooks/useGetFiles"

export function AssignmentFile(props: { assignmentId: number }) {
  const files = useGetFiles(props.assignmentId, "assignment")
  const handleDownload = () => {
    if (files) {
      const fileUrl = URL.createObjectURL(files[0].data)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = files[0].name
      link.click()
    }
  }
  return (
    <>
      <h1>Załączniki</h1>
      {files.map((file) => {
        return (
          <button key={file.name} onClick={handleDownload}>
            {file.name}
          </button>
        )
      })}
    </>
  )
}
