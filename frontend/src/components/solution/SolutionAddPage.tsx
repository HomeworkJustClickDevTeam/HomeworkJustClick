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
import { FaDownload } from "react-icons/fa6";

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

    function handleChangeComment(event: ChangeEvent<HTMLTextAreaElement>) {
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
        <section className='flex flex-col overflow-y-hidden h-[calc(100dvh-270px)] xl:h-[calc(100dvh-360px)]'>
      <div className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-96 gap-2 box-content overflow-y-auto">
          <div className='pl-3 border-l-2 border-main_blue lg:border-none pb-12 lg:pb-3'>
              <div className='flex flex-col lg:flex-row w-full h-[calc(100dvh-410px)] '>
                  <div className='flex flex-col mr-5 gap-2 '>
                      <div className='flex flex-row'>
                          <p className="pr-3 w-28 font-semibold">Tytuł zadania: </p>
                          <p className='max-w-48'>{assignment.title}</p>
                      </div>
                      <div>
                          <p className="font-semibold mb-1">Opis zadania: </p>
                          <p className='border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-80 lg:w-full'>{assignment.taskDescription}</p>
                      </div>
                  </div>
                  <div className='flex flex-col gap-3  lg:pl-5 lg:border-l-2 lg:border-main_blue w-fit'>
                          <div className='flex flex-row'>
                              <p className="font-semibold mr-1 w-48">Data ukończenia: </p>
                              {format(new Date(assignment.completionDatetime.toString()), "dd.MM.yyyy, HH:mm")}
                          </div>
                          <div className= 'flex flex-row'>{fileFromDb !== undefined && <><span className='w-48'>Plik do zadania:</span><FaDownload className='mt-1 mr-2'/><AssignmentFile assignmentId={assignment.id}/></>}</div>
                          <div className=''>
                              <label >
                                  Moje rozwiązania (max. 1):</label>

                                  <input
                                    name="file"
                                    type="file"
                                    className='pl-2'
                                    accept={assignment.advancedEvaluation ? AdvancedEvaluationExtensionType.join(",") : undefined}
                                    onChange={(e) => handleChangeFile(e)}/>
                          </div>
                          <div>
                          <label className='flex align-top'>
                              Twój komentarz do zadania: </label>
                              <textarea
                                className="pl-2 ml-1 border border-light_gray rounded-md shadow-md w-80 min-h-[125px] mt-2 px-2 py-1"
                                name="comment"

                                onChange={(e) => handleChangeComment(e)} />

                          </div>
                      </div>
                      <div className=''>
                          <button
                            type={"submit"}
                            onClick={(e) => handleUploadClick(e)}
                            className="fixed mb-2 md:bottom-16 bottom-16 right-[calc(7.5%+28px)]  bg-main_blue text-white px-4 py-1 rounded   hover:bg-hover_blue hover:shadow-md active:shadow-none"
                          >
                              Wyślij rozwiązanie
                          </button>
                      </div>
                  </div>
          </div>
      </div>
        </section>
    )
}

export default SolutionAddPage
