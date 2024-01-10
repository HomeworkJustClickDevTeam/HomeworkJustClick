import React, { ChangeEvent, useEffect, useState } from "react"
import {
  createFileWithAssignmentPostgresService,
  deleteFilePostgresService,
  getFilesByAssignmentPostgresService
} from "../../services/postgresDatabaseServices"
import { getFileMongoService, postFileMongoService } from "../../services/mongoDatabaseServices"
import { FileFromPostgresInterface } from "../../types/FileFromPostgresInterface"
import {useGetFile} from "../customHooks/useGetFile";
import {CreateFileUrlAndClick} from "../../utils/CreateFileUrlAndClick";
import {FileInterface} from "../../types/FileInterface";

export function AssignmentFile(props: {
  assignmentId: number
  databaseFile?: FileInterface
  setNewFile: React.Dispatch<React.SetStateAction<File|undefined>>
}) {
  function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
    if (e.target.files) {
      props.setNewFile(e.target.files[0])
    }
  }

  return (
    <div>
      <input type="file" onChange={(event)=>handleChangeFile(event)}/><br/>
      {props.databaseFile && <div className='flex mt-2'><span className='mr-3'>Aktualnie wybrany plik: </span>
          <button onClick={()=>CreateFileUrlAndClick(props.databaseFile!)}> {`${props.databaseFile!.name}`}</button></div>}
      {" "}
    </div>
  )
}
