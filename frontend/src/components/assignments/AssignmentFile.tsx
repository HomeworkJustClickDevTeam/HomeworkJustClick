import { useGetFile } from "../customHooks/useGetFile"
import {useState} from "react";

export function AssignmentFile(props: { assignmentId: number }) {
  const file = useGetFile(props.assignmentId, "assignment")
  const handleDownload = () => {
    if (file) {
      const fileUrl = URL.createObjectURL(file.data)
      const link = document.createElement("a")
      link.href = fileUrl
      link.download = file.name
      link.click()
      URL.revokeObjectURL(fileUrl)
    }
  }
  return (
    <div>
      {file!==undefined &&
      <button key={file.name} onClick={handleDownload}>
        {file.name}
      </button>}
    </div>
  )
}
