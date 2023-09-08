import { useGetFiles } from "../customHooks/useGetFiles"

export function AssignmentFile(props: { assignmentId: number }) {
  const files = useGetFiles(props.assignmentId, "assignment")
  const handleDownload = () => {
    if (files) {
      const fileUrl = URL.createObjectURL(files[0].fileData)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = files[0].fileName
      link.click()
    }
  }
  return (
      <>
        {files.map((file) => {
          return(<button key={file.fileName} onClick={handleDownload}>{file.fileName}</button>)
        })}
      </>
    )


}
