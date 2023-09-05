import {useContext, useEffect, useState} from "react"
import {getFilesBySolutionPostgresService} from "../../services/postgresDatabaseServices"
import {getFileMongoService} from "../../services/mongoDatabaseServices"
import {FileFromPostInterface} from "../../types/FileFromPostInterface";
import {useGetFiles} from "../customHooks/useGetFiles";
import Loading from "../animations/Loading";
import {LoadingContext} from "../../contexts/LoadingContext";

export function SolutionFile(props: { solutionId: number }) {
  const files = useGetFiles(props.solutionId, "solution")
  console.log(files)
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
              <button key={file.fileName} onClick={handleDownload}>{file.fileName}</button>
        })}
      </>
    )
}
