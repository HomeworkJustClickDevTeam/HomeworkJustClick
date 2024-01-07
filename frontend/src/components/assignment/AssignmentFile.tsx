import { useGetFile } from "../customHooks/useGetFile"
import {useState} from "react";
import {CreateFileUrlAndClick} from "../../utils/CreateFileUrlAndClick";

export function AssignmentFile(props: { assignmentId: number }) {
  const file = useGetFile(props.assignmentId, "assignment")
  return (
    <div>
      {file!==undefined &&
      <button key={file.name} onClick={() => CreateFileUrlAndClick(file)}>
        {file.name}
      </button>}
    </div>
  )
}
