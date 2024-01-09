import { SolutionFile } from "./SolutionFile"
import { AssignmentModel } from "../../types/Assignment.model"
import { SolutionInterface } from "../../types/SolutionInterface"
import {useGetFile} from "../customHooks/useGetFile";
import {spread} from "axios";
import {deleteFileMongoService} from "../../services/mongoDatabaseServices";
import {toast} from "react-toastify";
import {deleteSolutionPostgresService} from "../../services/postgresDatabaseServices";
import {useNavigate} from "react-router-dom";
import { FaDownload } from "react-icons/fa6";

export default function SolutionUncheckedStudentPage(props: {
  solution: SolutionInterface
  assignment: AssignmentModel
}) {
  const fileFromDb = useGetFile(props.solution.id, "solution")
  const navigate = useNavigate()
  const handleSolutionDeletion = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()
    if(fileFromDb !== undefined) {
      try{
        await deleteFileMongoService(fileFromDb.mongoId)
      }catch (e){
        toast.error("Nie udało się usunąć rozwiązania")
        console.error(e)
        return
      }
    }
    deleteSolutionPostgresService(props.solution.id.toString())
      .catch(error=>{
        if(error !== null && error !== undefined && error.response.status === 400)
          toast.error("Nauczyciel już sprawdził to zadanie, nie można go usunąć")
        else{
          toast.error("Nie udało się usunąć rozwiązania")
          console.error(error)
        }
      })
      .then((r)=>{
        if(r?.status === 200){
          toast.success("Pomyślnie usunięto rozwiązanie")
        }
        navigate("../assignments/")
      })
  }

  return (
      <div className='flex flex-col overflow-y-hidden h-[calc(100dvh-270px)] xl:h-[calc(100dvh-360px)] '>
    <div className="relative mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-fit gap-2 pb-3 box-content overflow-y-auto">

        <div className=' flex flex-col xl:flex-row xl:border-none border-b-2 border-main_blue pb-3 w-fit '>
        <div className='mr-2'>
            <div>
                <span className="font-semibold">Zadanie: </span>
                {props.assignment.title}{" "}
            </div>
              <div className=''>
                  <label className='flex align-top mt-2 w-fit font-semibold mr-2'>Opis zadania: </label>
                  <textarea disabled={true} className='mt-3  border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-80 lg:w-fit lg:min-w-[250px] min-h-[80px] lg:ml-0 ml-32 mb-5 xl:mb-0'>{props.assignment.taskDescription}</textarea>
              </div>
          </div>
            <div className='flex flex-col xl:pl-5 xl:border-l-2 xl:border-main_blue h-fit'>
              <div>{props.solution.comment.length > 0 && (
                <div>
                  <label className="flex align-top w-fit font-semibold mr-2">Twój komentarz: </label>
                    <textarea disabled={true} className='mt-3 ml-2 border border-light_gray rounded-md pl-1 pr-1 pb-3 pt-1 shadow-md w-80 lg:w-fit lg:min-w-[250px] min-h-[80px] lg:ml-0 ml-32 mb-5 xl:mb-0'>{props.solution.comment}</textarea>
                </div>
              )}</div>
              <div>
                <p className="font-semibold mt-2">Twoje przesłane pliki: </p>
                {fileFromDb !== undefined ? (
                    <div className='flex'><FaDownload className='mt-1 mr-2'/><SolutionFile fileFromDb={fileFromDb}/></div>
                ) : (
                  <p>Brak</p>
                )}
              </div>
            </div>
          <button onClick={(event) => handleSolutionDeletion(event)}
                  className='absolute top-5 right-0 mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white'>Usuń
          </button>

        </div>
        <div className="relative mt-6">
            <p>Wynik: </p>
            <p className="font-bold text-xl mt-4">
                ? / {props.assignment.maxPoints} - Praca jeszcze nie została
                sprawdzona{" "}
            </p>
        </div>
        </div>

      </div>
  )
}
