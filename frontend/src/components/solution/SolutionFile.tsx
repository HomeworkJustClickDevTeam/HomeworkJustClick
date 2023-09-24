import { useGetFiles } from "../customHooks/useGetFiles"

export function SolutionFile(props: { solutionId: number }) {
  const files = useGetFiles(props.solutionId, "solution")
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
        {files.map((file) => {
              return (<button key={file.name} onClick={handleDownload}>{file.name}</button>)
        })}
      </>
    )
}
