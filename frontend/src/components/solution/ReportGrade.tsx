import {useState} from "react";

export const ReportGrade = (props:
                                {
                                    evaluationId: number
                                }
) => {
    const [isButtonClicked, setIsButtonClicked] = useState<boolean>(false)
    const reportGrade = () => {
        return undefined;
    }

    const changeComment = () => {
        return undefined;
    }

    return (
        <>
            {isButtonClicked ? (
                <>
                    <input type="text" onChange={changeComment()}/>
                    <button onClick={reportGrade}></button>
                </>
            ) : (
                <button onClick={() => setIsButtonClicked(true)}>Zgłoś ocenę</button>
            )}
        </>
    )
}