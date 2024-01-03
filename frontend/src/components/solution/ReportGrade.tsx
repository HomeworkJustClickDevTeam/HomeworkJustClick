import {ChangeEvent, useState} from "react";
import {EvaluationReport} from "../../types/EvaluationReport.model";
import {addEvaluationReport} from "../../services/postgresDatabaseServices";
import {useNavigate} from "react-router-dom";
import {useAppSelector} from "../../types/HooksRedux";
import {selectGroup} from "../../redux/groupSlice";
import {toast} from "react-toastify";

export const ReportGrade = (props:
                                {
                                    evaluationId: number
                                }
) => {
    const [isButtonClicked, setIsButtonClicked] = useState<boolean>(false)
    const [comment, setComment] = useState<string>("")
    const navigate  = useNavigate()
    const group = useAppSelector(selectGroup)
    const reportGrade = () => {
        const evaluationReport = new EvaluationReport(comment, props.evaluationId)
        addEvaluationReport(evaluationReport)
          .then(() => {
              toast.success("Pomyślnie zgłoszono zadanie")
            navigate(`/group/${group!.id}/assignments/done`)
        })
          .catch((error) => {
              toast.error("Coś poszło nie tak.")
              console.log(error)
          })
    }

    const changeComment = (event: ChangeEvent<HTMLTextAreaElement>) => {
        const {value} = event.target
        setComment(value)
    }

    return (
        <>
            {isButtonClicked ? (
                <div className=' ml-8 border-2 border-berry_red px-4 py-2 h-36 w-96 flex rounded-md text-bottom'>
                    <>
                        <label className='flex align-top text-opacity-100'> Komentarz:
                            <textarea className='h-32 border rounder-sm border-border_gray ml-3 pl-2 pr-1' onChange={(event) => changeComment(event)} value={comment}/>
                        </label>
                        <button className='h-fit absolute bottom-2 right-2 px-2 py-1 bg-main_blue text-white rounded' onClick={reportGrade}>Wyślij </button>
                        <button className='h-fit absolute top-2 right-2 h-fit px-2 py-1 bg-berry_red text-white rounded' onClick={() => setIsButtonClicked(false)}>X</button>
                    </>
                </div>
            ) : (
                <button className=' px-6 py-1 bg-berry_red text-white rounded mt-[-5px]' onClick={() => setIsButtonClicked(true)}>Zgłoś ocenę</button>
            )}
        </>
    )
}