import {useNavigate, useParams} from "react-router-dom";
import {ChangeEvent, useState} from "react";
import { AssigmentToSend} from "../../types/types";
import common_request from "../../services/default-request-database";
import ReactDatePicker from "react-datepicker";
import 'react-datepicker/dist/react-datepicker.css';

function AddAssigment() {
    const navigate = useNavigate()
    const {id} = useParams();
    const [assigment, setAssigment] = useState<AssigmentToSend>({
        title: "",
        completionDatetime: new Date(),
        taskDescription: "",
        visible: false,
    });

    const handleChange = (event: ChangeEvent<HTMLInputElement> | Date) => {
        if (event instanceof Date) {
            setAssigment((prevState) => ({
                ...prevState,
                completionDatetime: event,
            }));
        } else {
            const { name, value, type, checked } = event.target;
            setAssigment((prevState) => ({
                ...prevState,
                [name]: type === 'checkbox' ? checked : value,
            }));
        }
    };

    function handleSubmit(event: React.FormEvent) {
        event.preventDefault();
        try {
            console.log(assigment)
            common_request.post(`/assignment/withUserAndGroup/${localStorage.getItem("id")}/${id}`, assigment)
            navigate(`/group/${id}/assignments/`)

        }catch (e) {
            console.log(e)
        }

    }

    return (
        <>
            <form onSubmit={handleSubmit}>
                <label> Tytuł
                <input name="title" type="text" onChange={handleChange} />
                </label>
                <label> Opis zadania
                <input name="taskDescription" type="text" onChange={handleChange}/>
                </label>
                <ReactDatePicker
                    name="completionDatetime"
                    selected={assigment.completionDatetime}
                    onChange={date => handleChange(date as Date)}
                    showTimeSelect
                    timeFormat="HH:mm"
                    timeIntervals={15}
                    dateFormat="yyyy-MM-dd HH:mm"
                />
                <label>
                    Visible:
                    <input
                        name="visible"
                        type="checkbox"
                        checked={assigment.visible}
                        onChange={handleChange}
                    />
                </label>
                <button type="submit">Dodaj zadanie domowe</button>
            </form>
        </>
    );
}
export default AddAssigment