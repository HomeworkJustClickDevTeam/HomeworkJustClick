import {ChangeEvent, useState} from "react";
import {EvaluationReport} from "../../types/EvaluationReport.model";
import {addEvaluationReport} from "../../services/postgresDatabaseServices";

export const ReportGrade = (props:
                                {
                                    evaluationId: number
                                }
) => {
    const [isButtonClicked, setIsButtonClicked] = useState<boolean>(false)
    const [comment, setComment] = useState<string>("")
    const reportGrade = () => {
        const evaluationReport = new EvaluationReport(comment, props.evaluationId)
        addEvaluationReport(evaluationReport).then(() => {
            setComment("")
            setIsButtonClicked(false)
        }).catch((error) => console.log(error))
    }

    const changeComment = (event: ChangeEvent<HTMLInputElement>) => {
        const {value} = event.target
        setComment(value)
    }

    return (
        <>
            {isButtonClicked ? (
                <>
                    <div>
                        <label> Komentarz
                            <input type="text" onChange={(event) => changeComment(event)} value={comment}/>
                        </label>
                        <button onClick={reportGrade}>Wyślij zgłoszenie</button>
                        <button onClick={() => setIsButtonClicked(false)}>Anuluj</button>
                    </div>
                </>
            ) : (
                <button onClick={() => setIsButtonClicked(true)}>Zgłoś ocenę</button>
            )}
        </>
    )
}