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
              toast.success("Pomyślnie zgłoszono zadanie, jako niepoprawnie sprawdzone.")
            navigate(`/group/${group!.id}/assignments/done`)
        })
          .catch((error) => {
              toast.error("Coś poszło nie tak.")
              console.log(error)
          })
    }

    const changeComment = (event: ChangeEvent<HTMLInputElement>) => {
        const {value} = event.target
        setComment(value)
    }

    return (
        <div>
            {isButtonClicked ? (
                <>
                    <>
                        <label> Komentarz
                            <input type="text" onChange={(event) => changeComment(event)} value={comment}/>
                        </label>
                        <button onClick={reportGrade}>Wyślij zgłoszenie</button>
                        <button onClick={() => setIsButtonClicked(false)}>Anuluj</button>
                    </>
                </>
            ) : (
                <button onClick={() => setIsButtonClicked(true)}>Zgłoś ocenę</button>
            )}
        </div>
    )
}