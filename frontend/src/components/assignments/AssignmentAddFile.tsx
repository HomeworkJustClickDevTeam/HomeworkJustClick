import {ChangeEvent, useEffect, useState} from "react"
import {postFilesMongoService} from "../../services/mongoDatabaseServices"
import {createFileWithAssignmentPostgresService} from "../../services/postgresDatabaseServices"
import {AssignmentAddFilePropsInterface} from "../../types/AssignmentAddFilePropsInterface"

export function AssignmentAddFile(props: AssignmentAddFilePropsInterface) {
    const [files, setFiles] = useState<File[]>([])

    useEffect(() => {
        if (props.toSend && props.idAssignment) {
            if (files.length > 0) {
                const fileList = new FormData()
                files.forEach((file, index) => {
                    fileList.append(`file${index}`, file)
                    }
                )
                postFilesMongoService(fileList)
                    .then((response) =>
                        createFileWithAssignmentPostgresService(
                            `${response.data.id}`,
                            response.data.format,
                            response.data.name,
                            `${props.idAssignment}`
                        )
                    )
                    .catch()
                props.setToNavigate(true)
            }
        }
    }, [props.toSend])

    function handleChangeFiles(e: ChangeEvent<HTMLInputElement>) {
        if (e.target.files) {
            const selectedFiles = Array.from(e.target.files)
            setFiles(selectedFiles)
        }
    }

    return (
        <>
            <input type="file" onChange={handleChangeFiles} multiple/>
            <div>{files.map((file, index) => (
                <div key={index}>{`${file.name} - ${file.type}`}</div>
            ))} </div>
            {" "}
        </>
    )
}
