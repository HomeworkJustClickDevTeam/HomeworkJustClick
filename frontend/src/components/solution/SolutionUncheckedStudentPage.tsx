import { SolutionFile } from "./SolutionFile"
import { AssignmentModel } from "../../types/Assignment.model"
import { SolutionInterface } from "../../types/SolutionInterface"
import {useGetFile} from "../customHooks/useGetFile";
import {spread} from "axios";
import {deleteFileMongoService} from "../../services/mongoDatabaseServices";
import {toast} from "react-toastify";
import {deleteSolutionPostgresService} from "../../services/postgresDatabaseServices";
import {useNavigate} from "react-router-dom";

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
    <div
      className="relative flex flex-col mx-[7.5%] mt-4 border border-border_gray border-1 rounded-md pt-4 px-4 h-80 gap-2">
      <p>
        <span className="font-semibold">Zadanie: </span>
        {props.assignment.title}{" "}
      </p>
      <p>
        <span className="font-semibold">Opis zadania: </span>
        {props.assignment.taskDescription}
      </p>
      {props.solution.comment.length > 0 && (
        <p>
          <span className="font-semibold">Twój komentarz: </span>
          {props.solution.comment}
        </p>
      )}
      <div>
        <p className="font-semibold">Przesłane pliki: </p>
        {fileFromDb !== undefined ? (
          <SolutionFile fileFromDb={fileFromDb}/>
        ) : (
          <p>Brak</p>
        )}
      </div>
      <br/>
      <div className="absolute bottom-0 left-0 mb-6 ml-4">
        <p>Wynik: </p>
        <p className="font-bold text-xl mt-4">
          ? / {props.assignment.maxPoints} - Praca jeszcze nie została
          sprawdzona{" "}
        </p>
      </div>
      <button onClick={(event) => handleSolutionDeletion(event)}
              className='absolute top-5 right-0 mr-6 mb-4 px-4 py-1 rounded-lg bg-berry_red text-white'>Usuń
      </button>
    </div>
  )
}
