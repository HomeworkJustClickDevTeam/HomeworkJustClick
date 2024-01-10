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
        <div >
            {isButtonClicked ? (
                <div className='fixed left-[50%] top-[30%] translate-x-[-50%] translate-y-[50%]'>
                <div className='relative ml-8 border-2 border-light_gray px-4 py-2  min-w-[400px] flex flex-col rounded-md text-bottom shadow-md text-center bg-white h-[260px]'>
                    <p className='mb-3 font-semibold'>Zgłoś ocenę</p>
                    <div className='flex'>
                        <div>
                            <label className='flex align-top text-opacity-100'> Komentarz:
                                <textarea
                                  maxLength={1500}
                                  className='min-h-[200px] border rounder-sm border-border_gray ml-3 pl-2 pr-1 max-h-[200px]' onChange={(event) => changeComment(event)} value={comment}/>
                            </label>
                            <button className='h-fit absolute bottom-2 right-2 px-3 py-1 bg-main_blue text-white rounded' onClick={reportGrade}>Wyślij </button>
                            <button className='h-fit absolute top-2 right-2  px-3 py-1 bg-berry_red text-white rounded' onClick={() => setIsButtonClicked(false)}>X</button>
                        </div>
                    </div>
                    </div>
                </div>
            ) : (
                <button className='fixed 2xl:right-[calc(7.5%+25px)] right-[calc(7.5%+125px)] 2xl:top-[425px] xl:mb-0 px-6 py-1 bg-berry_red text-white rounded ' onClick={() => setIsButtonClicked(true)}>Zgłoś ocenę</button>
            )}
        </div>
    )
}