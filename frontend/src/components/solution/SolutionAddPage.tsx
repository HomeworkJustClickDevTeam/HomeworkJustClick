import React, {ChangeEvent, useState} from "react"
import {postFileMongoService} from "../../services/mongoDatabaseServices"
import {useNavigate} from "react-router-dom"
import {
    createFileWithSolutionPostgresService,
    createSolutionWithUserAndAssignmentPostgresService,
} from "../../services/postgresDatabaseServices"
import {AssignmentFile} from "../assignment/AssignmentFile"
import {format, parseISO} from "date-fns"
import {AssignmentPropsInterface} from "../../types/AssignmentPropsInterface"
import {SolutionCreateInterface} from "../../types/SolutionCreateInterface"
import {selectUserState} from "../../redux/userStateSlice"
import {useAppDispatch, useAppSelector} from "../../types/HooksRedux"
import {useGetFile} from "../customHooks/useGetFile"
import { IoDocumentAttachOutline } from "react-icons/io5";
import {toast} from "react-toastify";
import {AdvancedEvaluationExtensionType} from "../../types/AdvancedEvaluationExtensionType";

function SolutionAddPage({assignment}: AssignmentPropsInterface) {
    const navigate = useNavigate()
    useAppDispatch()
    const userState = useAppSelector(selectUserState)
    const [file, setFile] = useState<File>()
    const fileFromDb = useGetFile(assignment.id, "assignment")
    const [solution, setSolution] = useState<SolutionCreateInterface>({
        creationDatetime: new Date().toISOString(),
        comment: "",
        lastModifiedDatetime: new Date().toISOString(),
    })

    function handleChangeFile(e: ChangeEvent<HTMLInputElement>) {
        e.preventDefault()
        if (!e.target.files) {
            return
        }
        if(assignment.advancedEvaluation){
            if(!AdvancedEvaluationExtensionType.includes('.' + e.target.files[0].name.split('.').pop()!)){
                toast.error("Wykładowca włączył funkcję zaawansowanego sprawdzania, ograniczającą dostępne rozszerzenia do: .xml, .txt, .json, .png, .jpg")
                e.target.value = ''
                return;
            }
        }
        setFile(e.target.files[0])
    }

    function handleChangeComment(event: ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target
        setSolution((prevState) => ({
            ...prevState,
            [name]: value,
        }))
    }

    function createSolutionWithFile(file: File, userState: any) {
        const formData = new FormData()
        formData.append("file", file)
        let mongoResponse: any = undefined
        let postgresResponse: any = undefined
        postFileMongoService(formData)
            .then((r) => {
                mongoResponse = r.data
                return createSolutionWithUserAndAssignmentPostgresService(
                    userState?.id.toString(),
                    assignment.id.toString(),
                    solution
                )
            })
            .then((r) => {
                postgresResponse = r.data
                if (mongoResponse !== undefined && postgresResponse !== undefined) {
                    return createFileWithSolutionPostgresService(
                        mongoResponse.id as string,
                        mongoResponse.format as string,
                        mongoResponse.name as string,
                        postgresResponse.id as number
                    )
                } else {
                    return Promise.reject(
                        "Something went wrong with getting file to the dbs and getting data from them."
                    )
                }
            })
            .then(() => {
                toast.success('Zadanie zostało przesłane! 🎉')
            })
            .then(() => {
                navigate(-1)
            })
            .catch((e) => console.log(e))

    }

    function createSolutionWithCommentOnly() {
        if (userState && solution.comment) {
            createSolutionWithUserAndAssignmentPostgresService(
                userState.id.toString(),
                assignment.id.toString(),
                solution
            )
                .then(() => {
                    toast.success('Zadanie zostało przesłane! 🎉')
                })
                .then(() =>
                navigate(-1))
        }

    }

    function handleUploadClick(e: React.MouseEvent<HTMLElement>) {
        e.preventDefault()
        if (!file) {
            createSolutionWithCommentOnly()
        } else {
            if (userState) {
                createSolutionWithFile(file, userState)
            }
        }
    }

    return (
      <div
        className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2">
          <div>
              <span className="font-semibold">Tytuł zadania: </span>
              {assignment.title}
          </div>
          <p>
              <span className="font-semibold">Opis zadania: </span>
              {assignment.taskDescription}
          </p>
          <div>
              <span className="font-semibold">Data ukończenia: </span>
              {format(new Date(assignment.completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}
          </div>
          {fileFromDb !== undefined && <>Plik do zadania:<AssignmentFile assignmentId={assignment.id}/></>}
          <label>
              Moje rozwiązania:
              <br/>
              <input
                name="file"
                type="file"
                className='pl-2'
                accept={assignment.advancedEvaluation ? AdvancedEvaluationExtensionType.join(",") : undefined}
                onChange={(e) => handleChangeFile(e)}/>
          </label>
          <label>
              Komentarz do zadania:
              <input
                className="pl-1 ml-2 border-b-2 border-b-light_gray w-80"
                type="text"
                name="comment"
                onChange={(e) => handleChangeComment(e)}
              />
          </label>
          <button
            type={"submit"}
            onClick={(e) => handleUploadClick(e)}
            className="absolute bg-main_blue text-white px-6 py-1 rounded w-40 bottom-0 left-0 ml-4 mb-6 hover:bg-hover_blue hover:shadow-md active:shadow-none"
          >
              Wyślij zadanie
          </button>
      </div>
    )
}

export default SolutionAddPage
