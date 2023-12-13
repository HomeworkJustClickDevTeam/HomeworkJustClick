import { useGetFile } from "../customHooks/useGetFile"
import {FileInterface} from "../../types/FileInterface";

export function SolutionFile({fileFromDb}: { fileFromDb: FileInterface }) {

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
