import { useGetFile } from "../customHooks/useGetFile"
import {FileInterface} from "../../types/FileInterface";
import {CreateFileUrlAndClick} from "../../utils/CreateFileUrlAndClick";

export function SolutionFile({fileFromDb}: { fileFromDb: FileInterface }) {
    return (<>
      {fileFromDb !== undefined &&
      <button key={fileFromDb.name} onClick={()=>CreateFileUrlAndClick(fileFromDb!)}>{fileFromDb.name}</button>}
    </>)
}
